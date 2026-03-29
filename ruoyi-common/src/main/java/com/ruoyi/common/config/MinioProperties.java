package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO 对象存储配置
 *
 * @author ruoyi
 */
@ConfigurationProperties(prefix = "minio")
public class MinioProperties
{
    /** 是否启用 MinIO（启用后通用上传、头像等写入桶，不再写本地 profile） */
    private boolean enabled = false;

    /** API 地址，如 http://192.168.88.128:9000 */
    private String endpoint = "http://127.0.0.1:9000";

    /**
     * 浏览器访问对象时使用的根地址，一般与 endpoint 相同；
     * 若经 Nginx/HTTPS 代理对外访问，请填代理后的公网根地址
     */
    private String url;

    private String accessKey = "minioadmin";

    private String secretKey = "minioadmin";

    /** 存储桶名称（不存在时启动会自动创建） */
    private String bucketName = "ruoyi";

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getAccessKey()
    {
        return accessKey;
    }

    public void setAccessKey(String accessKey)
    {
        this.accessKey = accessKey;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getBucketName()
    {
        return bucketName;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    /** 对外访问根地址，未配置 url 时使用 endpoint */
    public String getPublicBaseUrl()
    {
        String base = (url != null && !url.isEmpty()) ? url : endpoint;
        if (base != null && base.endsWith("/"))
        {
            return base.substring(0, base.length() - 1);
        }
        return base;
    }
}
