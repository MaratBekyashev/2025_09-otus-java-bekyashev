package ru.otus;

public class PingPongClassic {

    private final Object lock = new Object();

    private boolean isThread1Turn = true; // всегда начинает Thread-1

    private int number = 1;
    private boolean ascOrder = true;

    public void print(boolean isThread1) {
        while (true) {
            synchronized (lock) {
                try {
                    // ждём своей очереди
                    while (isThread1 != isThread1Turn) {
                        lock.wait();
                    }

                    // печатаем текущее число
                    System.out.println(Thread.currentThread().getName() + ": " + number);
                    Thread.sleep(500);
                    // число меняет только второй поток
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

                    // передаём ход
                    isThread1Turn = !isThread1Turn;

                    // будим другой поток
                    lock.notify();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
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
