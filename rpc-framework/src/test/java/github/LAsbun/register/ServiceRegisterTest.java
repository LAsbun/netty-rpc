package github.LAsbun.register;

import org.junit.Test;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class ServiceRegisterTest {

    @Test
    public void registerService() {

        ServiceRegister serviceRegister = new ServiceRegisterImpl();

        serviceRegister.registerService("github.LAsbun.helloService", new InetSocketAddress("127.0.0.1", 8080));

    }
}