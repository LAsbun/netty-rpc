package github.LAsbun.utils;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.RPCResponse;
import github.LAsbun.entity.enums.RPCResponseCodeEnum;
import github.LAsbun.exception.RPCException;

/**
 * Created by sws
 * rpc 校验工具类
 */

public class RPCCheckUtils {

    public static void checkRequestAndResponse(RPCRequest rpcRequest, RPCResponse rpcResponse) throws RPCException {

        if (null == rpcRequest || null == rpcResponse) {
            throw new RPCException(RPCResponseCodeEnum.FAIL);
        }
    }
}
