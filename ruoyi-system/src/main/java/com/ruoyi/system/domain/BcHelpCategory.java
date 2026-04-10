package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 帮助问题分类表 bc_help_category
 *
 * @author ruoyi
 */
public class BcHelpCategory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @Excel(name = "分类ID", cellType = ColumnType.NUMERIC)
    private Long categoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 分类编码 */
    @Excel(name = "分类编码")
    private String categoryCode;

    /** 父分类ID */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 显示顺序 */
    @Excel(name = "排序号", cellType = ColumnType.NUMERIC)
    private Integer sortNum;

    /** 分类图标 */
    @Excel(name = "分类图标")
    private String icon;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 子分类列表 */
    private java.util.List<BcHelpCategory> children;

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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.util.List<BcHelpCategory> getChildren() {
        return children;
    }

    public void setChildren(java.util.List<BcHelpCategory> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("categoryCode", getCategoryCode())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("sortNum", getSortNum())
            .append("icon", getIcon())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
