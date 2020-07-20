package github.LAsbun.exception;

import github.LAsbun.entity.enums.RPCResponseCodeEnum;

/**
 * Created by sws
 */

public class RPCException extends RuntimeException {

    public RPCException(RPCResponseCodeEnum codeEnum, String message) {
        super(codeEnum.getMessage() + ":" + message);
    }

    public RPCException(String message, Throwable e) {
        super(message, e);
    }

    public RPCException(RPCResponseCodeEnum codeEnum) {
        super(codeEnum.getMessage());
    }
}
