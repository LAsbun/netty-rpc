package github.LAsbun.entity.enums;

/**
 * Created by sws
 *
 * @create 2020-07-20 7:03 AM
 */

public enum RPCResponseCodeEnum {
    SUCCESS(200, "调用方法成功"),
    FAIL(500, "服务调用失败"),
    NOT_FOUND_METHOD(502, "没有找到指定方法"),
    NOT_FOUND_CLASS(501, "没有找到执行类");

    private Integer code;

    private String message;

    RPCResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
