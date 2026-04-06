package com.ruoyi.common.enums;

/**
 * 资料审核-通知类型枚举
 *
 * @author ruoyi
 */
public enum BcNoticeType {
    REVIEW_PASS("review_pass", "审核通过"),
    REVIEW_REJECT("review_reject", "审核驳回"),
    SUPPLY("supply", "需要补交"),
    SYSTEM("system", "系统通知");

    private final String code;
    private final String desc;

    BcNoticeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}