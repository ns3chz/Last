package com.hzc.last.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ThreadPoolTest {

    public static void main(String[] a) {
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Semaphore semaphore = new Semaphore(5, false);
        for (int i = 0; i < 100; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(semaphore.availablePermits());
                }
            });
            semaphore.release();
        }
        executorService.shutdown();
    }

    static class Test {
        private int r1, r2, A, B;
        private volatile boolean end1 = false;
        private volatile boolean end2 = false;
        private int num = 10000;

        public void start() {
            while (num > 0) {
                r1 = 0;
                r2 = 0;
                A = 0;
                B = 0;
                end1 = false;
                end2 = false;
                num--;
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        r1 = B;
                        A = 1;
                        end1 = true;
                    }
                });
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        r2 = A;
                        B = 2;
                        end2 = true;
                    }
                });
                thread1.start();
                thread2.start();

                while (!end1 || !end2) {
                }
                if (r1 == 2 && r2 == 1) {
                    System.out.println("occurred");
                    break;
                }

//                else {
//                    System.out.println(r1 + " " + r2);
//                }
            }

        }
    }
}
