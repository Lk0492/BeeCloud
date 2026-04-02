package com.ruoyi.system.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 附件表 sys_attachment
 *
 * @author ruoyi
 */
public class SysAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 附件ID */
    @Excel(name = "附件序号", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    private Long fileId;

    /** 文件名（上传时的原始文件名） */
    @Excel(name = "原始文件名")
    private String originalFilename;

    /** 文件名（服务器端保存的文件名） */
    @Excel(name = "服务器文件名")
    private String filename;

    /** 文件后缀 */
    @Excel(name = "文件后缀")
    private String fileSuffix;

    /** 文件大小（字节） */
    @Excel(name = "文件大小（字节）", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    private Long fileSize;

    /** 文件类型（image/video/audio/document/archive/other） */
    @Excel(name = "文件类型", readConverterExp = "image=图片,video=视频,audio=音频,document=文档,archive=压缩包,other=其他")
    private String fileType;

    /** 文件 MIME 类型 */
    private String contentType;

    /** 文件存储路径/URL */
    @Excel(name = "访问地址")
    private String accessUrl;

    /** 存储方式（minio/local） */
    @Excel(name = "存储方式", readConverterExp = "minio=MinIO对象存储,local=本地磁盘")
    private String storageType;

    /** 存储桶名称（MinIO） */
    private String bucketName;

    /** 存储路径/对象名 */
    private String objectName;

    /** 文件唯一标识（UUID） */
    private String uuid;

    /** 业务类型（如：avatar/notice/article） */
    @Excel(name = "业务类型")
    private String businessType;

    /** 关联业务ID */
    private Long businessId;

    /** 上传者部门ID */
    private Long deptId;

    /** 上传者部门名称 */
    @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT)
    private String deptName;

    /** 上传者用户名 */
    @Excel(name = "上传人")
    private String uploaderUsername;

    /** 下载次数 */
    @Excel(name = "下载次数", type = Type.EXPORT, cellType = ColumnType.NUMERIC)
    private Long downloadCount;

    /** 文件状态（0=正常 1=禁用） */
    @Excel(name = "文件状态", readConverterExp = "0=正常,1=禁用")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public SysAttachment()
    {

    }

    public SysAttachment(Long fileId)
    {
        this.fileId = fileId;
    }

    public Long getFileId()
    {
        return fileId;
    }

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public String getOriginalFilename()
    {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename)
    {
        this.originalFilename = originalFilename;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getFileSuffix()
    {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix)
    {
        this.fileSuffix = fileSuffix;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getAccessUrl()
    {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl)
    {
        this.accessUrl = accessUrl;
    }

    public String getStorageType()
    {
        return storageType;
    }

    public void setStorageType(String storageType)
    {
        this.storageType = storageType;
    }

    public String getBucketName()
    {
        return bucketName;
    }

    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(String businessType)
    {
        this.businessType = businessType;
    }

    public Long getBusinessId()
    {
        return businessId;
    }

    public void setBusinessId(Long businessId)
    {
        this.businessId = businessId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getUploaderUsername()
    {
        return uploaderUsername;
    }

    public void setUploaderUsername(String uploaderUsername)
    {
        this.uploaderUsername = uploaderUsername;
    }

    public Long getDownloadCount()
    {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount)
    {
        this.downloadCount = downloadCount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fileId", getFileId())
                .append("originalFilename", getOriginalFilename())
                .append("filename", getFilename())
                .append("fileSuffix", getFileSuffix())
                .append("fileSize", getFileSize())
                .append("fileType", getFileType())
                .append("contentType", getContentType())
                .append("accessUrl", getAccessUrl())
                .append("storageType", getStorageType())
                .append("bucketName", getBucketName())
                .append("objectName", getObjectName())
                .append("uuid", getUuid())
                .append("businessType", getBusinessType())
                .append("businessId", getBusinessId())
                .append("deptId", getDeptId())
                .append("uploaderUsername", getUploaderUsername())
                .append("downloadCount", getDownloadCount())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
