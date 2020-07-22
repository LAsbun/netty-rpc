package github.LAsbun.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import github.LAsbun.entity.RPCRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sws
 *
 * @create 2020-07-20 8:21 AM
 */
@Slf4j
public class FastjsonSerializerImpl implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        return JSONObject.toJSONBytes(object);
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Object o = JSONObject.parseObject(bytes, clazz);
        if (o instanceof RPCRequest) {
            RPCRequest rpcRequest = (RPCRequest) o;
            rpcRequest.setParameters(transformParamMeters(rpcRequest));
            return clazz.cast(rpcRequest);
        } else {
            return clazz.cast(o);
        }
    }

    // 转换参数为实际的类型对象
    private Object[] transformParamMeters(RPCRequest rpcRequest) {
        if (null == rpcRequest.getParamTypes() || null == rpcRequest.getParameters()) {
            return rpcRequest.getParameters();
        }

        List<Object> objectList = new ArrayList<>();

        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            objectList.add(JSON.parseObject(
                    JSON.toJSONString(rpcRequest.getParameters()[i]),
                    rpcRequest.getParamTypes()[i]));
        }

        return objectList.toArray(new Object[0]);
    }


}
