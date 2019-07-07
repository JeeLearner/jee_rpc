package com.jee.simple.app;

import com.jee.rpc.client.RpcProxy;
import com.jee.simple.client.HelloService;
import com.jee.simple.client.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author jeeLearner
 * @date 2019/7/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class HelloServiceTest {

    @Autowired
    private RpcProxy rpcProxy;

    @Test
    public void helloTest1() {
        // 调用代理的create方法，代理HelloService接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        // 调用代理的方法，执行invoke
        String result = helloService.hello("World");
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }

    @Test
    public void helloTest2() {
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello(new Person("Yong", "Huang"));
        System.out.println("服务端返回结果：");
        System.out.println(result);
    }
}

