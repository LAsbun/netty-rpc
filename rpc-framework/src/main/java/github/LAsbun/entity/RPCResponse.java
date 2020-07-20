package github.LAsbun.entity;

import github.LAsbun.entity.enums.RPCResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by sws
 *
 * @create 2020-07-20 7:00 AM
 */
@Data
public class RPCResponse<T> implements Serializable {

    private String requestId;

    private Integer code;

    private String message;

    private T data;

    public static <T> RPCResponse<T> success(T data, String requestId) {

        RPCResponse<T> response = new RPCResponse<>();

        response.setCode(RPCResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RPCResponseCodeEnum.SUCCESS.getMessage());
        response.setData(data);
        response.setRequestId(requestId);
        return response;
    }

    public static <T> RPCResponse<T> fail(RPCResponseCodeEnum codeEnum, String requestId) {

        RPCResponse<T> response = new RPCResponse<>();

        response.setCode(codeEnum.getCode());
        response.setMessage(codeEnum.getMessage());
        response.setRequestId(requestId);
        return response;
    }
}
