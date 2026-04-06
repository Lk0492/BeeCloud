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
 * 资料审核记录表 bc_review_record
 *
 * @author ruoyi
 */
public class BcReviewRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 审核记录ID */
    @Excel(name = "记录ID", cellType = ColumnType.NUMERIC)
    private Long recordId;

    /** 申请单ID */
    @Excel(name = "申请单ID", cellType = ColumnType.NUMERIC)
    private Long formId;

    /** 附件明细ID */
    private Long detailId;

    /** 学生ID */
    private Long studentId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 审核员ID */
    @Excel(name = "审核员ID", cellType = ColumnType.NUMERIC)
    private Long reviewerId;

    /** 审核员姓名 */
    @Excel(name = "审核员")
    private String reviewerName;

    /** 操作类型 */
    @Excel(name = "操作类型", readConverterExp = "submit=提交,accept=通过,reject=驳回,resubmit=补交,review=认领")
    private String actionType;

    /** 操作备注 */
    @Excel(name = "备注")
    private String actionRemark;

    /** 操作前申请单状态 */
    private String formStatusBefore;

    /** 操作后申请单状态 */
    private String formStatusAfter;

    /** 版本号 */
    private Integer versionNum;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionRemark() {
        return actionRemark;
    }

    public void setActionRemark(String actionRemark) {
        this.actionRemark = actionRemark;
    }

    public String getFormStatusBefore() {
        return formStatusBefore;
    }

    public void setFormStatusBefore(String formStatusBefore) {
        this.formStatusBefore = formStatusBefore;
    }

    public String getFormStatusAfter() {
        return formStatusAfter;
    }

    public void setFormStatusAfter(String formStatusAfter) {
        this.formStatusAfter = formStatusAfter;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("formId", getFormId())
            .append("detailId", getDetailId())
            .append("studentId", getStudentId())
            .append("studentName", getStudentName())
            .append("reviewerId", getReviewerId())
            .append("reviewerName", getReviewerName())
            .append("actionType", getActionType())
            .append("actionRemark", getActionRemark())
            .append("formStatusBefore", getFormStatusBefore())
            .append("formStatusAfter", getFormStatusAfter())
            .append("versionNum", getVersionNum())
            .toString();
    }
}
