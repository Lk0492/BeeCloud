package com.ruoyi.web.controller.common;

import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.MinioProperties;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.MinioService;
import com.ruoyi.system.domain.SysAttachment;
import com.ruoyi.system.service.ISysAttachmentService;

/**
 * 通用请求处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ObjectProvider<MinioService> minioServiceProvider;

    @Autowired
    private ISysAttachmentService attachmentService;

    @Autowired
    private ObjectProvider<MinioProperties> minioPropertiesProvider;

    private static final String FILE_DELIMITER = ",";

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = RuoYiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            MinioService minioService = minioServiceProvider.getIfAvailable();
            MinioProperties minioProperties = minioPropertiesProvider.getIfAvailable();
            String fileName;
            String url;
            String storageType;
            String bucketName = null;
            String objectName = null;
            if (minioService != null)
            {
                fileName = minioService.upload(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "upload");
                url = fileName;
                storageType = "minio";
                bucketName = minioProperties != null ? minioProperties.getBucketName() : "ruoyi";
                objectName = parseObjectNameFromUrl(fileName);
            }
            else
            {
                String filePath = RuoYiConfig.getUploadPath();
                fileName = FileUploadUtils.upload(filePath, file);
                url = serverConfig.getUrl() + fileName;
                storageType = "local";
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            // 上传成功后写入附件记录
            SysAttachment attachment = buildAttachment(file, url, fileName, storageType, bucketName, objectName);
            attachmentService.insertAttachment(attachment);
            ajax.put("fileId", attachment.getFileId());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            MinioService minioService = minioServiceProvider.getIfAvailable();
            MinioProperties minioProperties = minioPropertiesProvider.getIfAvailable();
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            List<Long> fileIds = new ArrayList<Long>();
            for (MultipartFile file : files)
            {
                String fileName;
                String url;
                String storageType;
                String bucketName = null;
                String objectName = null;
                if (minioService != null)
                {
                    fileName = minioService.upload(file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, "upload");
                    url = fileName;
                    storageType = "minio";
                    bucketName = minioProperties != null ? minioProperties.getBucketName() : "ruoyi";
                    objectName = parseObjectNameFromUrl(fileName);
                }
                else
                {
                    fileName = FileUploadUtils.upload(filePath, file);
                    url = serverConfig.getUrl() + fileName;
                    storageType = "local";
                }
                SysAttachment attachment = buildAttachment(file, url, fileName, storageType, bucketName, objectName);
                attachmentService.insertAttachment(attachment);
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
                fileIds.add(attachment.getFileId());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMITER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMITER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMITER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMITER));
            ajax.put("fileIds", StringUtils.join(fileIds, FILE_DELIMITER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 从 MinIO 完整 URL 中解析出 objectName（去掉 http(s)://host:port/bucket/ 前缀）
     */
    private String parseObjectNameFromUrl(String url)
    {
        if (StringUtils.isEmpty(url) || !url.startsWith("http"))
        {
            return null;
        }
        try
        {
            java.net.URI uri = new java.net.URI(url);
            String path = uri.getPath();
            if (path != null && path.startsWith("/"))
            {
                path = path.substring(1);
            }
            int idx = path != null ? path.indexOf('/') : -1;
            if (idx < 0)
            {
                return null;
            }
            return path.substring(idx + 1);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 构建附件记录对象，从当前登录用户获取上传者信息
     */
    private SysAttachment buildAttachment(MultipartFile file, String accessUrl, String filename,
            String storageType, String bucketName, String objectName)
    {
        SysAttachment attachment = new SysAttachment();
        attachment.setOriginalFilename(file.getOriginalFilename());
        attachment.setFilename(FileUtils.getName(filename));
        attachment.setFileSuffix(FileUploadUtils.getExtension(file));
        attachment.setFileSize(file.getSize());
        attachment.setFileType(categorizeFileType(file.getContentType(), FileUploadUtils.getExtension(file)));
        attachment.setContentType(file.getContentType());
        attachment.setAccessUrl(accessUrl);
        attachment.setStorageType(storageType);
        attachment.setBucketName(bucketName);
        attachment.setObjectName(objectName);
        attachment.setUuid(IdUtils.fastSimpleUUID());
        attachment.setDownloadCount(0L);
        attachment.setStatus("0");
        attachment.setDelFlag("0");
        try
        {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null)
            {
                attachment.setCreateBy(loginUser.getUsername());
                attachment.setUploaderUsername(loginUser.getUsername());
                attachment.setDeptId(loginUser.getDeptId());
            }
            else
            {
                attachment.setCreateBy("anonymous");
                attachment.setUploaderUsername("anonymous");
            }
        }
        catch (Exception e)
        {
            attachment.setCreateBy("anonymous");
            attachment.setUploaderUsername("anonymous");
        }
        return attachment;
    }

    /**
     * 根据 MIME 类型和文件后缀判断文件类别
     */
    private String categorizeFileType(String contentType, String fileSuffix)
    {
        if (contentType == null)
        {
            contentType = "";
        }
        String lower = contentType.toLowerCase();
        if (lower.startsWith("image/") || isImageExtension(fileSuffix))
        {
            return "image";
        }
        else if (lower.startsWith("video/") || isVideoExtension(fileSuffix))
        {
            return "video";
        }
        else if (lower.startsWith("audio/") || isAudioExtension(fileSuffix))
        {
            return "audio";
        }
        else if (isDocumentExtension(fileSuffix))
        {
            return "document";
        }
        else if (isArchiveExtension(fileSuffix))
        {
            return "archive";
        }
        return "other";
    }

    private boolean isImageExtension(String ext)
    {
        if (ext == null) return false;
        String e = ext.toLowerCase();
        return e.equals("jpg") || e.equals("jpeg") || e.equals("png") || e.equals("gif")
                || e.equals("bmp") || e.equals("webp") || e.equals("svg") || e.equals("ico");
    }

    private boolean isVideoExtension(String ext)
    {
        if (ext == null) return false;
        String e = ext.toLowerCase();
        return e.equals("mp4") || e.equals("avi") || e.equals("rmvb") || e.equals("mkv")
                || e.equals("mov") || e.equals("wmv") || e.equals("flv") || e.equals("webm");
    }

    private boolean isAudioExtension(String ext)
    {
        if (ext == null) return false;
        String e = ext.toLowerCase();
        return e.equals("mp3") || e.equals("wav") || e.equals("ogg") || e.equals("aac")
                || e.equals("flac") || e.equals("wma") || e.equals("m4a");
    }

    private boolean isDocumentExtension(String ext)
    {
        if (ext == null) return false;
        String e = ext.toLowerCase();
        return e.equals("doc") || e.equals("docx") || e.equals("xls") || e.equals("xlsx")
                || e.equals("ppt") || e.equals("pptx") || e.equals("pdf") || e.equals("txt")
                || e.equals("html") || e.equals("htm") || e.equals("csv");
    }

    private boolean isArchiveExtension(String ext)
    {
        if (ext == null) return false;
        String e = ext.toLowerCase();
        return e.equals("zip") || e.equals("rar") || e.equals("7z") || e.equals("tar")
                || e.equals("gz") || e.equals("bz2") || e.equals("xz");
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = RuoYiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + FileUtils.stripPrefix(resource);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
