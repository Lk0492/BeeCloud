package com.ruoyi.common.enums;

/**
 * 资料审核-申请单状态枚举
 *
 * @author ruoyi
 */
public enum BcFormStatus {
    DRAFT("0", "未提交", "info"),
    SUBMITTED("1", "已提交", "primary"),
    REVIEWING("2", "审核中", "warning"),
    PASSED("3", "已通过", "success"),
    REJECTED("4", "已驳回", "danger"),
    SUPPLY("5", "补交中", "warning"),
    RE_REVIEW("6", "再审核", "warning");

    private final String code;
    private final String desc;
    private final String cssClass;

    BcFormStatus(String code, String desc, String cssClass) {
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

    public static BcFormStatus fromCode(String code) {
        for (BcFormStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return DRAFT;
    }

    public boolean canSubmit() {
        return this == DRAFT || this == REJECTED || this == SUPPLY;
    }

    public boolean canReview() {
        return this == SUBMITTED || this == REVIEWING || this == RE_REVIEW;
    }
}