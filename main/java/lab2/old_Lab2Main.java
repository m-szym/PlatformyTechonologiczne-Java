package lab2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class old_Lab2Main {
    public static Random rng = new Random();

    public void l2_main(String[] args) {
        try {
            System.out.println("Hello world!");

            SharedInput in = new SharedInput(10);
            for (int i = 20; i < 32; i++) {
                in.buffer.add(i);
            }
            SharedOutput out = new SharedOutput();
            System.out.println("in.buffer: " + in.buffer.size());

            ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(4);
            for (int i = 0; i < 4; i++) {
                executor.execute(new MockThread(in, out));
            }
//        for (int i = 0; i < 5; i++) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            in.put(37);
//        }
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextInt()) {
                int a = scanner.nextInt();
                if (a == 0) {
                    break;
                }
                in.put(a);
            }


            executor.shutdownNow();
            System.out.println("out.buffer: " + out.buffer.size());
            System.out.println("in.buffer: " + out.primes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class SharedInput {
        private ArrayList<Integer> buffer;
        public SharedInput(int cap) {
            this.buffer = new ArrayList<Integer>(cap);
        }
        public synchronized int take() throws InterruptedException{
            while (buffer.isEmpty()) {
                wait();
                System.out.println("Thred: " + Thread.currentThread().getName() + " is waiting");
            }
            notify();
            return buffer.remove(0);
        }
        public synchronized void put(int num) {
            buffer.add(num);
            notify();
        }
    }

    private class SharedOutput {
        private ArrayList<Integer> buffer;
        private ArrayList<Integer> primes;
        public SharedOutput() {
            this.buffer = new ArrayList<Integer>();
            this.primes = new ArrayList<Integer>();
        }
        public synchronized void put(int num) {
            buffer.add(num);
        }
        public synchronized void addPrime(int num) {
            primes.add(num);
        }

    }

    private class MockThread
        implements Runnable {

        private final SharedInput in;
        private final SharedOutput out;

        public MockThread(SharedInput in, SharedOutput out) {
            this.in = in;
            this.out = out;
        }

        private boolean isPrime(int num) {
            if (num <= 1) {
                return false;
            }
            for (int i = 2; i < num; i++) {
                if (num % i == 0) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void run() {
            System.out.println("Thred: " + Thread.currentThread().getName() + " is running");
            int num;
            for (int i = 0; i < 4; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Thred: " + Thread.currentThread().getName() + " is interrupted");
                    break;
                }
//                synchronized (in) {
//                    if (in.buffer.isEmpty()) {
//                        try {
//                            //in.wait();
//                            System.out.println("Thred: " + Thread.currentThread().getName() + " is waiting");
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
                try {
                    num = in.take();
                    System.out.println("Thred: " + Thread.currentThread().getName() + " is on num: " + num);

                    if (isPrime(num))
                        out.addPrime(num);
                    else
                        out.put(num);
                }
                catch (InterruptedException e) {
                    System.out.println("Thred: " + Thread.currentThread().getName() + " is interrupted");
                    break;
                }
            }
            System.out.println("Thred: " + Thread.currentThread().getName() + " is done");
        }
    }
}
