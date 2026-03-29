package com.ruoyi.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ruoyi.common.config.MinioProperties;
import io.minio.MinioClient;

/**
 * MinIO 客户端（仅在 minio.enabled=true 时加载）
 *
 * @author ruoyi
 */
@Configuration
@ConditionalOnProperty(prefix = "minio", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig
{
    @Bean
    public MinioClient minioClient(MinioProperties minioProperties)
    {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
