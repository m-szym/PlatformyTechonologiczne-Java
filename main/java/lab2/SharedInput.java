package lab2;

import java.util.ArrayList;

public class SharedInput {
    public ArrayList<Integer> buffer;

    public SharedInput() {
        this.buffer = new ArrayList<>();
    }

    public synchronized int take() throws InterruptedException{
        while (buffer.isEmpty()) {
            wait();
            System.out.println("Thread: " + Thread.currentThread().getName() + " is waiting");
        }
        notify();
        return buffer.remove(0);
    }
    public synchronized void put(int num) {
        buffer.add(num);
        notify();
    }
}
