package github.LAsbun.serialize;

import com.alibaba.fastjson.JSON;
import github.LAsbun.entity.RPCRequest;
import github.LAsbun.entity.enums.RPCMessageTypeEnum;
import junit.framework.TestCase;
import org.junit.Test;
import sun.jvm.hotspot.HelloWorld;

import java.util.UUID;

public class FastjsonSerializerTest extends TestCase {


    @Test
    public void testSerialize() {

        RPCRequest rpcRequest = RPCRequest.builder()
                .paramTypes(new Class[]{HelloWorld.class})
                .interfaceName(HelloWorld.class.getName())
                .methodName("a")
                .parameters(new Object[]{})
                .requestId(UUID.randomUUID().toString())
                .rpcMessageTypeEnum(RPCMessageTypeEnum.RPC_SERVICE)
                .build();

        byte[] bytes = JSON.toJSONBytes(rpcRequest);
        Object o = JSON.parseObject(bytes, RPCRequest.class);

//        Serializer serializer = new FastjsonSerializerImpl();

//        byte[] serialize = serializer.serialize(rpcRequest);
//        RPCRequest deserialize = serializer.deserialize(serialize, RPCRequest.class);
        System.out.println("xx");
    }

    public void testDeserialize() {
    }
}