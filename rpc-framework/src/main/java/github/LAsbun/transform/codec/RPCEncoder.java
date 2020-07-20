package github.LAsbun.transform.codec;

import github.LAsbun.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by sws
 *
 * @create 2020-07-20 7:45 AM
 */

public class RPCEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer;
    // 需要反序列话的类
    private Class<?> genericClass;

    public RPCEncoder(Serializer serializer, Class<?> genericClass) {
        this.serializer = serializer;
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            // 序列化
            byte[] serialize = serializer.serialize(o);

            int length = serialize.length;
            //数据的长度
            byteBuf.writeInt(length);

            byteBuf.writeBytes(serialize);
        }
    }
}
