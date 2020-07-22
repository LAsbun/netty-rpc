package github.LAsbun;

import github.LAsbun.transform.client.ClientProxy;
import github.LAsbun.transform.client.impl.ClientProxyImpl;

/**
 * Created by sws
 * 客户端
 */

public class NettyClientApplication {

    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxyImpl();
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new HelloWorldMessage("11", "22"));
        System.out.println(hello);
    }
}
