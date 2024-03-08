package lab2;

import java.util.ArrayList;

public class SharedOutput {
    public ArrayList<Integer> buffer;

    public SharedOutput() {
        this.buffer = new ArrayList<>();
    }

    public synchronized void put(int number) {
        buffer.add(number);
    }
}
