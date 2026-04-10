package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * FAQ浏览记录表 bc_help_view_log
 *
 * @author ruoyi
 */
public class BcHelpViewLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long logId;

    /** 文章ID */
    private Long articleId;

    /** 浏览用户ID */
    private Long userId;

    /** 浏览用户姓名 */
    private String userName;

    /** IP地址 */
    private String ipAddr;

    /** 浏览时间 */
    private java.util.Date createTime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
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

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
}
