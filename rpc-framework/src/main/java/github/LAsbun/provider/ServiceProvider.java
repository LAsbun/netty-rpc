package github.LAsbun.provider;

/**
 * Created by sws
 * <p>
 * 服务端，提供服务管理
 *
 * @create 2020-07-20 6:47 AM
 */

public interface ServiceProvider {

    /**
     * 注册本地的服务
     *
     * @param service      服务实例(这里注册的都是单例，note:后面可以使用动态代理)
     * @param serviceClass 服务对应的class
     * @param <T>
     */
    <T> void addService(T service, Class<T> serviceClass);

    /**
     * 获取注册过的service
     * @param serviceName 注册的名称. note:里面使用注册的是class.getCanonicalName
     * @return
     */
    Object getService(String serviceName);
}
