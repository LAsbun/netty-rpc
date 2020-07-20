package github.LAsbun;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by sws
 *
 * @create 2020-07-20 11:09 PM
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloWorldMessage helloWorldMessage) {
        return helloWorldMessage.getMessage() + helloWorldMessage.getDesc();
    }
}
