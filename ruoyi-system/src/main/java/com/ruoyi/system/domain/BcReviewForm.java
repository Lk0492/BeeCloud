package com.ruoyi.system.domain;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资料审核申请单主表 bc_review_form
 *
 * @author ruoyi
 */
public class BcReviewForm extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 申请单ID */
    @Excel(name = "申请单ID", cellType = ColumnType.NUMERIC)
    private Long formId;

    /** 申请单编号 */
    @Excel(name = "申请单编号")
    private String formNo;

    /** 学生ID */
    @Excel(name = "学生ID", cellType = ColumnType.NUMERIC)
    private Long studentId;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNo;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 所属组织ID */
    @Excel(name = "组织ID", cellType = ColumnType.NUMERIC)
    private Long deptId;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String deptName;

    /** 入学年份 */
    @Excel(name = "入学年份")
    private String admissionYear;

    /** 申请单状态（0未提交 1已提交 2审核中 3已通过 4已驳回 5补交中 6再审核） */
    @Excel(name = "申请单状态", readConverterExp = "0=未提交,1=已提交,2=审核中,3=已通过,4=已驳回,5=补交中,6=再审核")
    private String formStatus;

    /** 提交时间 */
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 当前审核员ID */
    private Long reviewerId;

    /** 当前审核员姓名 */
    @Excel(name = "审核员")
    private String reviewerName;

    /** 审核时间 */
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 版本号 */
    private Integer versionNum;

    /** 是否为当前版本 */
    private String isCurrent;

    /** 驳回原因 */
    private String rejectReason;

    /** 附件总数 */
    @Excel(name = "附件总数", cellType = ColumnType.NUMERIC)
    private Integer totalAttachments;

    /** 已审核附件数 */
    @Excel(name = "已审核附件数", cellType = ColumnType.NUMERIC)
    private Integer reviewedAttachments;

    /** 删除标志 */
    private String delFlag;

    /** 批量操作ID列表（前端批量选择） */
    private Long[] ids;

    /** 附件列表 */
    private List<BcReviewAttachment> attachmentList;

    /** 审核记录列表 */
    private List<BcReviewRecord> recordList;

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
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

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(String admissionYear) {
        this.admissionYear = admissionYear;
    }

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
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

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Integer getTotalAttachments() {
        return totalAttachments;
    }

    public void setTotalAttachments(Integer totalAttachments) {
        this.totalAttachments = totalAttachments;
    }

    public Integer getReviewedAttachments() {
        return reviewedAttachments;
    }

    public void setReviewedAttachments(Integer reviewedAttachments) {
        this.reviewedAttachments = reviewedAttachments;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public List<BcReviewAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<BcReviewAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<BcReviewRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<BcReviewRecord> recordList) {
        this.recordList = recordList;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("formId", getFormId())
            .append("formNo", getFormNo())
            .append("studentId", getStudentId())
            .append("studentName", getStudentName())
            .append("studentNo", getStudentNo())
            .append("idCard", getIdCard())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("admissionYear", getAdmissionYear())
            .append("formStatus", getFormStatus())
            .append("submitTime", getSubmitTime())
            .append("reviewerId", getReviewerId())
            .append("reviewerName", getReviewerName())
            .append("reviewTime", getReviewTime())
            .append("versionNum", getVersionNum())
            .append("isCurrent", getIsCurrent())
            .append("rejectReason", getRejectReason())
            .append("totalAttachments", getTotalAttachments())
            .append("reviewedAttachments", getReviewedAttachments())
            .append("delFlag", getDelFlag())
            .toString();
    }
}