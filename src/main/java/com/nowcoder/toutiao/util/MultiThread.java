package com.nowcoder.toutiao.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by hsw on 2017/6/10.
 */

class Mythread extends Thread {
    private int tid;

    public Mythread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {

            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("T%d: %d", tid, i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> q;

    public Producer(BlockingQueue q) {
        this.q = q;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(100);
                q.put(String.valueOf(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

class  Consumer implements  Runnable{
    private BlockingQueue<String> q;

    public Consumer(BlockingQueue q) {
        this.q = q;
    }
    @Override
    public void run() {

        try {
            while(true){
                System.out.println(Thread.currentThread().getName()+ ":" +q.take());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}


public class MultiThread {

    public static void testThread() {
        for (int i = 0; i < 10; i++) {
            new Mythread(i).start();
        }
    }

    public static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"consumer1").start();
        new Thread(new Consumer(q),"consumer2").start();
    }

    public static void sleep(int mills){
        try {
            Thread.sleep(mills);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private  static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private  static int  userId;

    public static void testThreadLocal(){

       /* for(int i =0; i<10; ++i){
            final  int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal: " + threadLocalUserIds.get());
                }
            }).start();
        }*/

        for(int i =0; i<10; ++i){
            final  int finalI = i;
            userId = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    System.out.println("NoThreadLocal: " + userId);
                }
            }).start();
        }

    }

    public static void main(String[] args) {
        //testThread();
        //testBlockingQueue();
        testThreadLocal();
    }
}
