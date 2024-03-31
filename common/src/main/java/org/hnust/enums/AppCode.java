package org.hnust.enums;

import lombok.Getter;

@Getter
public enum AppCode {


    ACCOUNT_NOT_FOUND(0, "账号不存在"),
    VERIFY_CODE_ERROR(0, "验证码错误"),

    PHONE_ALREADY_EXISTS(0, "手机号已存在"),
    EMAIL_ALREADY_EXISTS(0, "邮箱已存在"),
    USERNAME_ALREADY_EXISTS(0, "用户名已存在"),
    ALREADY_EXISTS(0, "账号已存在"),

    SUGGEST_NOT_FOUND(0, "建议不存在"),
    ITEM_NOT_FOUND(0, "失物招领不存在"),
    MESSAGE_NOT_FOUND(0, "留言不存在"),

    MODIFICATION_NOT_ALLOWED(0, "不允许修改他人的建议"),
    DELETION_NOT_ALLOWED(0, "不允许删除他人的建议"),
    MESSAGE_DELETION_NOT_ALLOWED(0, "不允许删除他人的留言"),

    USER_INVALID(0, "用户信息不正确，只能操作自己的数据哦"),
    PHONE_INVALID(0, "手机号格式错误"),
    EMAIL_INVALID(0, "邮箱号格式错误"),
    PASSWORD_INVALID(0, "密码格式错误"),

    OPERATION_NOT_ALLOWED(0, "审核操作不再范围内"),
    PARAM_INVALID(0, "参数无效"),

    LOGIN_FAILED(0, "登录失败"),

    COMMON_ERROR(0, "通用错误（重构枚举类使用）");


    private final int code;
    private final String msg;

    AppCode(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

}
