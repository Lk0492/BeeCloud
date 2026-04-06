package com.ruoyi.common.enums;

/**
 * 资料审核-附件状态枚举
 *
 * @author ruoyi
 */
public enum BcAttachStatus {
    UNSUBMITTED("0", "未提交", "info"),
    SUBMITTED("1", "已提交", "primary"),
    REVIEWING("2", "审核中", "warning"),
    PASSED("3", "已通过", "success"),
    REJECTED("4", "已驳回", "danger");

    private final String code;
    private final String desc;
    private final String cssClass;

    BcAttachStatus(String code, String desc, String cssClass) {
        this.code = code;
        this.desc = desc;
        this.cssClass = cssClass;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getCssClass() {
        return cssClass;
    }

    public static BcAttachStatus fromCode(String code) {
        for (BcAttachStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return UNSUBMITTED;
    }
}