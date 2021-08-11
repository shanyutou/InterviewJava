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
 * jstack tid | grep tid(16) -A60 ******没有成功，计划换成oracle JDK试试******
 */

public class TuningJVMGC {

    public static void main(String[] args) {
        while (true) {
            System.out.println(new java.util.Random().nextInt(77778888));
        }
    }

}
