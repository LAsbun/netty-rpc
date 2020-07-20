package github.LAsbun.transform.codec;

import github.LAsbun.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by sws
 *
 * @create 2020-07-20 7:44 AM
 */
@Slf4j
public class RPCDecoder extends ByteToMessageDecoder {

    private Serializer serializer;
    // 需要反序列话的类
    private Class<?> genericClass;

    public RPCDecoder(Serializer serializer, Class<?> genericClass) {
        this.serializer = serializer;
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 标记当前读取位置
        byteBuf.markReaderIndex();

        // 判断是否能够读取length 长度
        if (byteBuf.readableBytes() <= 4) {
            return;
        }

        int dataLength = byteBuf.readInt();
        // 异常情况
        if (dataLength < 0) {
            throw new CorruptedFrameException("negative length: " + dataLength);
        }

        // 数据长度没有完全传递过来，
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] content = new byte[dataLength];
        byteBuf.readBytes(content);

        Object deserialize = serializer.deserialize(content, genericClass);
        list.add(deserialize);
        log.info("success decode bytebuf to Object");
    }
}
