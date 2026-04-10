package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帮助文章/FAQ表 bc_help_article
 *
 * @author ruoyi
 */
public class BcHelpArticle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    @Excel(name = "文章ID", cellType = ColumnType.NUMERIC)
    private Long articleId;

    /** 所属分类ID */
    @Excel(name = "所属分类", targetAttr = "categoryName")
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 问题标题 */
    @Excel(name = "问题")
    private String question;

    /** 答案内容 */
    @Excel(name = "答案")
    private String answer;

    /** 关键词 */
    @Excel(name = "关键词")
    private String keywords;

    /** 是否热门 */
    @Excel(name = "是否热门", readConverterExp = "0=否,1=是")
    private String isHot;

    /** 是否推荐 */
    @Excel(name = "是否推荐", readConverterExp = "0=否,1=是")
    private String isRecommend;

    /** 浏览次数 */
    @Excel(name = "浏览次数", cellType = ColumnType.NUMERIC)
    private Integer viewCount;

    /** 排序号 */
    @Excel(name = "排序号", cellType = ColumnType.NUMERIC)
    private Integer sortNum;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("articleId", getArticleId())
            .append("categoryId", getCategoryId())
            .append("question", getQuestion())
            .append("answer", getAnswer())
            .append("keywords", getKeywords())
            .append("isHot", getIsHot())
            .append("isRecommend", getIsRecommend())
            .append("viewCount", getViewCount())
            .append("sortNum", getSortNum())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
