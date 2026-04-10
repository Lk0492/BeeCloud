package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 问题反馈表 bc_help_feedback
 *
 * @author ruoyi
 */
public class BcHelpFeedback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 反馈ID */
    @Excel(name = "反馈ID", cellType = ColumnType.NUMERIC)
    private Long feedbackId;

    /** 提交用户ID */
    @Excel(name = "用户ID", cellType = ColumnType.NUMERIC)
    private Long userId;

    /** 提交用户姓名 */
    @Excel(name = "用户姓名")
    private String userName;

    /** 用户提交的问题 */
    @Excel(name = "问题内容")
    private String question;

    /** 管理员回复的答案 */
    @Excel(name = "回复内容")
    private String answer;

    /** 回复人 */
    @Excel(name = "回复人")
    private String replyBy;

    /** 回复时间 */
    @Excel(name = "回复时间")
    private java.util.Date replyTime;

    /** 回复状态 */
    @Excel(name = "回复状态", readConverterExp = "0=待回复,1=已回复,2=已关闭")
    private String replyStatus;

    /** 搜索关键词 */
    private String searchKeywords;

    /** 提交时间 */
    @Excel(name = "提交时间")
    private java.util.Date createTime;

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    public java.util.Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(java.util.Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("feedbackId", getFeedbackId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("question", getQuestion())
            .append("answer", getAnswer())
            .append("replyBy", getReplyBy())
            .append("replyTime", getReplyTime())
            .append("replyStatus", getReplyStatus())
            .append("searchKeywords", getSearchKeywords())
            .toString();
    }
}
