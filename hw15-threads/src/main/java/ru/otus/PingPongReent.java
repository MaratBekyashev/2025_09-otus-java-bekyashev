package ru.otus;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongReent {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private boolean isThread1Turn = true; // всегда начинает Thread-1

    private int number = 1;
    private boolean ascOrder = true;

    public void print(boolean isThread1) {
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                while (isThread1 != isThread1Turn) {
                    condition.await();
                }

                System.out.println(Thread.currentThread().getName() + ": " + number);
                Thread.sleep(600);

                if (!isThread1) {
                    if (ascOrder) {
                        number++;
                        if (number == 10) {
                            ascOrder = false;
                        }
                    } else {
                        number--;
                        if (number == 1) {
                            ascOrder = true;
                        }
                    }
                }

                isThread1Turn = !isThread1Turn;
                condition.signal();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PingPongReent pp = new PingPongReent();

        Thread t1 = new Thread(() -> pp.print(true), "Thread-1");
        Thread t2 = new Thread(() -> pp.print(false), "Thread-2");

        t1.start();
        t2.start();
    }
}
