package github.LAsbun.entity;

import github.LAsbun.entity.enums.RPCMessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sws
 * 请求参数
 */
@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RPCRequest {

    // 请求唯一id
    private String requestId;

    // 接口名称
    private String interfaceName;

    private String methodName;

    // 参数
    private Object[] parameters;

    private Class<?>[] paramTypes;

    // 请求的类型
    private RPCMessageTypeEnum rpcMessageTypeEnum;
}
