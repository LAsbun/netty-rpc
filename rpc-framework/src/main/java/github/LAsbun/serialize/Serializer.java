package github.LAsbun.serialize;

/**
 * Created by sws
 * <p>
 * 序列化 && 反序列化
 *
 * @create 2020-07-20 8:09 AM
 */

public interface Serializer {

    /**
     * 序列化对象
     *
     * @param object 需要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     *
     * @param bytes 序列化后的数组
     * @param clazz 指定目标类
     * @param <T>   目标类型
     * @return 反序列化之后的对象实例
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
