package lab2;

import java.util.Random;

public class MockThread implements Runnable {
    private SharedInput in;
    private SharedOutput out;
    private String threadName;
    private Random rand = new Random();

    public MockThread(SharedInput _in, SharedOutput _out) {
        this.in = _in;
        this.out = _out;
    }

    @Override
    public void run() {
        this.threadName = Thread.currentThread().getName();
        System.out.println(this.threadName + " started");
        int input;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                input = in.take();
                //System.out.println(this.threadName + " took {" + input + "} form SharedInput");
                //System.out.println(this.threadName + " started working on {" + input + "}");
                workPrime(input);
                //System.out.println(this.threadName + " finished working on {" + input + "}");
            }
            catch (InterruptedException ie) {
                //System.out.println(this.threadName + " hit with InterruptException");
                System.out.println(this.threadName + " interrupted");
                break;
            }
        }

        System.out.println(this.threadName  + " finishing");
    }

    private boolean checkPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private void workPrime(int n) {
        System.out.println(this.threadName + " working on {" + n + "}");
        if (checkPrime(n)) {
            out.put(n);
        }
    }

    private void randSleep() {
        try {
            Thread.sleep(rand.nextInt(5000 - 500) + 500);
        }
        catch (InterruptedException ie) {
            System.out.println(this.threadName + " hit with InterruptException while sleeping");
        }
    }

}
