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
 * 资料审核申请单附件表 bc_review_attachment
 *
 * @author ruoyi
 */
public class BcReviewAttachment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    @Excel(name = "明细ID", cellType = ColumnType.NUMERIC)
    private Long detailId;

    /** 申请单ID */
    @Excel(name = "申请单ID", cellType = ColumnType.NUMERIC)
    private Long formId;

    /** 附件ID */
    private Long fileId;

    /** 附件资料类型 */
    @Excel(name = "资料类型", readConverterExp = "admission_notice=录取通知书,id_card=身份证,household=户口本,photo=照片,ganscore=高考成绩单,health_cert=体检表,enroll_record=学籍表,party_relation=党团关系,poor_proof=贫困证明,other=其他材料")
    private String fileCategory;

    /** 文件原始名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件访问路径 */
    private String filePath;

    /** 文件大小（字节） */
    @Excel(name = "文件大小", cellType = ColumnType.NUMERIC)
    private Long fileSize;

    /** 文件后缀 */
    private String fileSuffix;

    /** 附件审核状态（0未提交 1已提交 2审核中 3已通过 4已驳回） */
    @Excel(name = "审核状态", readConverterExp = "0=未提交,1=已提交,2=审核中,3=已通过,4=已驳回")
    private String attachStatus;

    /** 附件审核备注 */
    private String attachRemark;

    /** 审核时间 */
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 审核员ID */
    private Long reviewerId;

    /** 审核员姓名 */
    @Excel(name = "审核员")
    private String reviewerName;

    /** 版本号 */
    private Integer versionNum;

    /** 排序号 */
    private Integer sortNum;

    /** 删除标志 */
    private String delFlag;

    /** 资料类型名称（用于前端显示） */
    private String fileCategoryName;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(String fileCategory) {
        this.fileCategory = fileCategory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getAttachStatus() {
        return attachStatus;
    }

    public void setAttachStatus(String attachStatus) {
        this.attachStatus = attachStatus;
    }

    public String getAttachRemark() {
        return attachRemark;
    }

    public void setAttachRemark(String attachRemark) {
        this.attachRemark = attachRemark;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getFileCategoryName() {
        return fileCategoryName;
    }

    public void setFileCategoryName(String fileCategoryName) {
        this.fileCategoryName = fileCategoryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("detailId", getDetailId())
            .append("formId", getFormId())
            .append("fileId", getFileId())
            .append("fileCategory", getFileCategory())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileSize", getFileSize())
            .append("fileSuffix", getFileSuffix())
            .append("attachStatus", getAttachStatus())
            .append("attachRemark", getAttachRemark())
            .append("reviewTime", getReviewTime())
            .append("reviewerId", getReviewerId())
            .append("reviewerName", getReviewerName())
            .append("versionNum", getVersionNum())
            .append("sortNum", getSortNum())
            .append("delFlag", getDelFlag())
            .toString();
    }
}