package lab2;

import java.util.Random;

public class MockThread implements Runnable {
    private SharedInput in;
    private SharedOutput out;
    private String threadName;

    public MockThread(SharedInput _in, SharedOutput _out) {
        this.in = _in;
        this.out = _out;
    }

    @Override
    public void run() {
        this.threadName = Thread.currentThread().getName();
        System.out.println(this.threadName + " started");
        Random rng = new Random();
        int input;


        while (!Thread.currentThread().isInterrupted()) {
            try {
                input = in.take();
                //System.out.println(this.threadName + " took {" + input + "} form SharedInput");
                System.out.println(this.threadName + " started working on {" + input + "}");
                Thread.sleep(rng.nextInt(5000 - 500) + 500);
                System.out.println(this.threadName + " finished working on {" + input + "}");
                out.put(input);
            }
            catch (InterruptedException ie) {
                System.out.println(this.threadName + " hit with InterruptException");
                break;
            }
        }
        System.out.println(this.threadName + " interrupted");


        System.out.println(this.threadName  + " finishing");
    }

}
