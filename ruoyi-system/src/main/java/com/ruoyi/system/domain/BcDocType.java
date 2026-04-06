package com.ruoyi.system.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资料类型字典表 bc_doc_type
 *
 * @author ruoyi
 */
public class BcDocType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 资料类型ID */
    @Excel(name = "类型ID", cellType = ColumnType.NUMERIC)
    private Long typeId;

    /** 类型编码 */
    @Excel(name = "类型编码")
    private String typeCode;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    /** 材料说明 */
    @Excel(name = "材料说明")
    private String typeDesc;

    /** 是否必填 */
    @Excel(name = "是否必填", readConverterExp = "0=否,1=是")
    private String isRequired;

    /** 是否允许多文件 */
    @Excel(name = "允许多文件", readConverterExp = "0=否,1=是")
    private String allowMultiple;

    /** 最大文件数量 */
    @Excel(name = "最大文件数", cellType = ColumnType.NUMERIC)
    private Integer maxFileCount;

    /** 允许的文件后缀 */
    @Excel(name = "允许后缀")
    private String allowedSuffix;

    /** 单文件最大大小（字节） */
    @Excel(name = "最大文件大小(字节)", cellType = ColumnType.NUMERIC)
    private Long maxFileSize;

    /** 排序号 */
    @Excel(name = "排序号", cellType = ColumnType.NUMERIC)
    private Integer sortNum;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @NotBlank(message = "类型编码不能为空")
    @Size(min = 0, max = 50, message = "类型编码长度不能超过50个字符")
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @NotBlank(message = "类型名称不能为空")
    @Size(min = 0, max = 100, message = "类型名称长度不能超过100个字符")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(String allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public Integer getMaxFileCount() {
        return maxFileCount;
    }

    public void setMaxFileCount(Integer maxFileCount) {
        this.maxFileCount = maxFileCount;
    }

    public String getAllowedSuffix() {
        return allowedSuffix;
    }

    public void setAllowedSuffix(String allowedSuffix) {
        this.allowedSuffix = allowedSuffix;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
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
            .append("typeId", getTypeId())
            .append("typeCode", getTypeCode())
            .append("typeName", getTypeName())
            .append("typeDesc", getTypeDesc())
            .append("isRequired", getIsRequired())
            .append("allowMultiple", getAllowMultiple())
            .append("maxFileCount", getMaxFileCount())
            .append("allowedSuffix", getAllowedSuffix())
            .append("maxFileSize", getMaxFileSize())
            .append("sortNum", getSortNum())
            .append("status", getStatus())
            .append("remark", getRemark())
            .toString();
    }
}
