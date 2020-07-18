package github.LAsbun.register;

import org.junit.Test;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

public class ServiceDiscoverTest {

    @Test
    public void lookup() {

        ServiceDiscover serviceDiscover = new ServiceDiscoverImpl();
        InetSocketAddress lookup = serviceDiscover.lookup("github.LAsbun.helloService");
        System.out.println(lookup.toString());
    }
}