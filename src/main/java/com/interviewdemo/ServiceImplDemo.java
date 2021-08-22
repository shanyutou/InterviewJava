package com.interviewdemo;


@Service
public class ServiceImplDemo implements ServiceDemo {
    @Override
    public void run() {
        System.out.println("run....");
    }
}
