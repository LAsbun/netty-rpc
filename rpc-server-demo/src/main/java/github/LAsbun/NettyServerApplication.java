package github.LAsbun;

import github.LAsbun.transform.server.NettyServer;

/**
 * Created by sws
 *
 * @create 2020-07-20 11:10 PM
 */

public class NettyServerApplication {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1", 9001);
        nettyServer.publishService(helloService, HelloService.class);
        nettyServer.start();
    }
}
