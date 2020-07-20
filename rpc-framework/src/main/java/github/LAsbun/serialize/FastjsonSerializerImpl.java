package github.LAsbun.serialize;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

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
        return JSONObject.parseObject(bytes, clazz);
    }
}
