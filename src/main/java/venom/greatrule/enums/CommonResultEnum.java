package venom.greatrule.enums;

import lombok.Getter;

@Getter
public enum CommonResultEnum {

    SUCCESS(100000, "操作成功"),

    UNKNOWN_ERROR(900000, "未知异常！"),

    IMPORT_FILE_SCHEMA_ERROR(200001, "导入文件格式错误！"),

    IMPORT_URL_ERROR(200002, "导入url错误！"),

    DELETE_RULE_PARAM_ERROR(200003, "删除规则参数错误！"),
    ;

    private final int code;

    private final String msg;

    CommonResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
