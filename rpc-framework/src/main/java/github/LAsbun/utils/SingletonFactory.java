package github.LAsbun.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sws
 * <p>
 */

public class SingletonFactory {

    public static Map<String, Object> singletonMap = new ConcurrentHashMap<>();

    // 单例模式
    public static <T> T getInstance(Class<T> clazz) {
        String clazzString = clazz.toString();
        Object instance = singletonMap.get(clazzString);
        if (instance == null) {
            synchronized (clazz) {
                // 因为有可能并发，所以这里是双重校验
                if (instance == null) {
                    try {
                        instance = clazz.newInstance();
                        singletonMap.put(clazzString, instance);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        // 这里重新获取 单例，走过上面代码，要么runtimeException 要么成功创建单例
        return clazz.cast(singletonMap.get(clazzString));

    }
}
