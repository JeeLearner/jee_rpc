package com.jee.simple.server;

import com.jee.rpc.server.RpcService;
import com.jee.simple.client.HelloService;
import com.jee.simple.client.Person;

/**
 * @author jeeLearner
 * @date 2019/7/6
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService{

    @Override
    public String hello(String name) {
        System.out.println("已经调用服务端接口实现，业务处理结果为：");
        System.out.println("Hello! " + name);
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        System.out.println("已经调用服务端接口实现，业务处理为：");
        System.out.println("Hello! " + person.getFirstName() + " " + person.getLastName());
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}

