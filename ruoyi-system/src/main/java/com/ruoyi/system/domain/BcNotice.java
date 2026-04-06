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
 * 系统通知表 bc_notice
 *
 * @author ruoyi
 */
public class BcNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 通知ID */
    @Excel(name = "通知ID", cellType = ColumnType.NUMERIC)
    private Long noticeId;

    /** 通知类型 */
    @Excel(name = "通知类型", readConverterExp = "review_pass=审核通过,review_reject=审核驳回,supply=补交通知,system=系统通知")
    private String noticeType;

    /** 通知标题 */
    @Excel(name = "通知标题")
    private String title;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String content;

    /** 关联申请单ID */
    @Excel(name = "申请单ID", cellType = ColumnType.NUMERIC)
    private Long formId;

    /** 接收人用户ID */
    @Excel(name = "接收人ID", cellType = ColumnType.NUMERIC)
    private Long receiveUserId;

    /** 接收人姓名 */
    @Excel(name = "接收人")
    private String receiveUserName;

    /** 是否已读 */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private String isRead;

    /** 阅读时间 */
    @Excel(name = "阅读时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeType", getNoticeType())
            .append("title", getTitle())
            .append("content", getContent())
            .append("formId", getFormId())
            .append("receiveUserId", getReceiveUserId())
            .append("receiveUserName", getReceiveUserName())
            .append("isRead", getIsRead())
            .append("readTime", getReadTime())
            .toString();
    }
}
