package com.interviewdemo;

import org.junit.Test;//spring1
//import org.junit.jupiter.api.Test;//spring2
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;


import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    App.class
})
public class SpringAOPDemo {

    @Autowired
    ServiceDemo serviceDemo;


    @Test
    public void test1(){
        System.out.println("开始测试");
        serviceDemo.run();
    }


}
