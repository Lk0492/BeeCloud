package com.ruoyi.system.domain.vo;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资料审核统计数据VO
 *
 * @author ruoyi
 */
public class BcStatisticsVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 总申请单数 */
    private Long totalForms;

    /** 未提交数 */
    private Long draftCount;

    /** 已提交数 */
    private Long submittedCount;

    /** 审核中数 */
    private Long reviewingCount;

    /** 已通过数 */
    private Long passedCount;

    /** 已驳回数 */
    private Long rejectedCount;

    /** 补交中数 */
    private Long supplyCount;

    /** 再审核数 */
    private Long reReviewCount;

    /** 通过率 */
    private Double passRate;

    /** 驳回率 */
    private Double rejectRate;

    /** 统计数据列表（按日期） */
    private List<DailyStat> dailyStats;

    public static class DailyStat {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date date;
        private Long submitted;
        private Long passed;
        private Long rejected;

        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
        public Long getSubmitted() { return submitted; }
        public void setSubmitted(Long submitted) { this.submitted = submitted; }
        public Long getPassed() { return passed; }
        public void setPassed(Long passed) { this.passed = passed; }
        public Long getRejected() { return rejected; }
        public void setRejected(Long rejected) { this.rejected = rejected; }
    }

    public Long getTotalForms() { return totalForms; }
    public void setTotalForms(Long totalForms) { this.totalForms = totalForms; }
    public Long getDraftCount() { return draftCount; }
    public void setDraftCount(Long draftCount) { this.draftCount = draftCount; }
    public Long getSubmittedCount() { return submittedCount; }
    public void setSubmittedCount(Long submittedCount) { this.submittedCount = submittedCount; }
    public Long getReviewingCount() { return reviewingCount; }
    public void setReviewingCount(Long reviewingCount) { this.reviewingCount = reviewingCount; }
    public Long getPassedCount() { return passedCount; }
    public void setPassedCount(Long passedCount) { this.passedCount = passedCount; }
    public Long getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(Long rejectedCount) { this.rejectedCount = rejectedCount; }
    public Long getSupplyCount() { return supplyCount; }
    public void setSupplyCount(Long supplyCount) { this.supplyCount = supplyCount; }
    public Long getReReviewCount() { return reReviewCount; }
    public void setReReviewCount(Long reReviewCount) { this.reReviewCount = reReviewCount; }
    public Double getPassRate() { return passRate; }
    public void setPassRate(Double passRate) { this.passRate = passRate; }
    public Double getRejectRate() { return rejectRate; }
    public void setRejectRate(Double rejectRate) { this.rejectRate = rejectRate; }
    public List<DailyStat> getDailyStats() { return dailyStats; }
    public void setDailyStats(List<DailyStat> dailyStats) { this.dailyStats = dailyStats; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("totalForms", getTotalForms())
            .append("draftCount", getDraftCount())
            .append("submittedCount", getSubmittedCount())
            .append("reviewingCount", getReviewingCount())
            .append("passedCount", getPassedCount())
            .append("rejectedCount", getRejectedCount())
            .append("passRate", getPassRate())
            .append("rejectRate", getRejectRate())
            .toString();
    }
}
