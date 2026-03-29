package com.ruoyi.framework.web.service;

import java.io.IOException;
import java.io.InputStream;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.MinioProperties;
import com.ruoyi.common.exception.file.FileNameLengthLimitExceededException;
import com.ruoyi.common.exception.file.FileSizeLimitExceededException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

/**
 * MinIO 上传与删除
 *
 * @author ruoyi
 */
@Service
@ConditionalOnProperty(prefix = "minio", name = "enabled", havingValue = "true")
public class MinioService
{
    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    public MinioService(MinioClient minioClient, MinioProperties minioProperties)
    {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @PostConstruct
    public void ensureBucket() throws Exception
    {
        String bucket = minioProperties.getBucketName();
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists)
        {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    /**
     * 通用文件上传（与本地 FileUploadUtils 相同校验规则）
     *
     * @param file 上传文件
     * @param allowedExtension 允许的后缀
     * @param folder 桶内目录前缀，如 upload
     * @return 浏览器可访问的完整 URL
     */
    public String upload(MultipartFile file, String[] allowedExtension, String folder)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        FileUploadUtils.assertAllowed(file, allowedExtension);
        String relativeName = FileUploadUtils.extractFilename(file);
        String objectName = folder + "/" + relativeName;
        return putObject(file, objectName);
    }

    /**
     * 头像上传（UUID 文件名）
     */
    public String uploadAvatar(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        FileUploadUtils.assertAllowed(file, allowedExtension);
        String relativeName = FileUploadUtils.uuidFilename(file);
        String objectName = "avatar/" + relativeName;
        return putObject(file, objectName);
    }

    private String putObject(MultipartFile file, String objectName) throws IOException
    {
        String bucket = minioProperties.getBucketName();
        String contentType = StringUtils.isNotEmpty(file.getContentType()) ? file.getContentType()
                : "application/octet-stream";
        try (InputStream in = file.getInputStream())
        {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(in, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        }
        catch (Exception e)
        {
            throw new IOException("MinIO 上传失败: " + e.getMessage(), e);
        }
        return buildObjectUrl(objectName);
    }

    private String buildObjectUrl(String objectName)
    {
        String base = minioProperties.getPublicBaseUrl();
        return base + "/" + minioProperties.getBucketName() + "/" + objectName;
    }

    /**
     * 根据入库的完整 URL 删除对象（仅删除属于本 MinIO 桶的地址）
     */
    public void removeByStoredUrl(String storedUrl)
    {
        if (StringUtils.isEmpty(storedUrl) || !storedUrl.startsWith("http"))
        {
            return;
        }
        String publicBase = minioProperties.getPublicBaseUrl();
        if (!storedUrl.startsWith(publicBase))
        {
            return;
        }
        try
        {
            java.net.URI uri = java.net.URI.create(storedUrl);
            String path = uri.getPath();
            if (path.startsWith("/"))
            {
                path = path.substring(1);
            }
            int idx = path.indexOf('/');
            if (idx < 0)
            {
                return;
            }
            String bucket = path.substring(0, idx);
            String objectName = path.substring(idx + 1);
            if (!minioProperties.getBucketName().equals(bucket) || StringUtils.isEmpty(objectName))
            {
                return;
            }
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
        }
        catch (Exception e)
        {
            // 删除失败不影响主流程
        }
    }
}
