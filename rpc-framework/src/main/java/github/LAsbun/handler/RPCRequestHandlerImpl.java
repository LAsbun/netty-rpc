package github.LAsbun.handler;

import github.LAsbun.entity.RPCRequest;
import github.LAsbun.exception.RPCException;
import github.LAsbun.provider.ServiceProvider;
import github.LAsbun.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;

/**
 * Created by sws
 */
@Slf4j
public class RPCRequestHandlerImpl implements RPCRequestHandler {

    private ServiceProvider serviceProvider;

    public RPCRequestHandlerImpl() {
        this.serviceProvider = new ServiceProviderImpl();
    }


    /**
     * 处理请求，并返回
     *
     * @param rpcRequest 请求参数
     * @return 处理结果
     */
    @Override
    public Object handle(RPCRequest rpcRequest) {

        log.info("[{}] handler RPC start", rpcRequest.getRequestId());

        try {
            Object service = serviceProvider.getService(rpcRequest.getInterfaceName());
            if (null != service) {
                log.info("[{}] not found service", rpcRequest.getRequestId());
                throw new NoClassDefFoundError("没有找到对应的service");
            }
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());

            Object result = method.invoke(service, rpcRequest.getParameters());
            log.info("[{}] handler RPC end", rpcRequest.getRequestId());
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoClassDefFoundError e) {
            log.info("[{}] handler error:{}", rpcRequest.getRequestId(), e.getMessage());
            throw new RPCException(e.getMessage(), e);
        }
    }
}
