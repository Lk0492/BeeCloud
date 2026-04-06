package com.ruoyi.common.enums;

/**
 * 资料审核-审核操作类型枚举
 *
 * @author ruoyi
 */
public enum BcActionType {
    SUBMIT("submit", "提交资料"),
    ACCEPT("accept", "审核通过"),
    REJECT("reject", "审核驳回"),
    RESUBMIT("resubmit", "补交资料"),
    REVIEW("review", "认领审核"),
    ASSIGN("assign", "分配审核员");

    private final String code;
    private final String desc;

    BcActionType(String code, String desc) {
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