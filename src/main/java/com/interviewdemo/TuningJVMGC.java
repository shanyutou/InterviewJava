package com.interviewdemo;

/**
 * -Xms  -Xmx ....
 */

/**
 * Undertow
 */

/**
 * 分析系统性能：
 * 整机：top(cpu mem load average)
 * CPU： vmstat       pidstat -u |-p pid 进程取样
 * 内存： free -m
 * 硬盘：df
 * 磁盘IO： iostat -xdk 2 3
 * 网络IO： ifstat
 *
 * jps
 * ps -mp 进程 -o THREAD,tid,time
 * jstack tid | grep tid(16) -A60 ******没有成功，计划换成oracle JDK试试******    错误原因：jstack 后面只能跟进程ID，而不是写线程ID
 */

public class TuningJVMGC {

    public static void main(String[] args) {
        while (true) {
            System.out.println(new java.util.Random().nextInt(77778888));
        }
    }

}
