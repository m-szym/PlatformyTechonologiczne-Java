package lab2;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class Lab2Main2 {
    public static void main(String[] args) {
        SharedInput in = new SharedInput();
        SharedOutput out = new SharedOutput();

        for (int i = 0; i < 10; i++) {
            in.put(i);
        }


        int threadNumber = 4;
        System.out.println("Starting " + threadNumber + " threads");
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            executor.execute(new MockThread(in, out));
        }


        Scanner scanner = new Scanner(System.in);
        int scannedInt;
        while(scanner.hasNextInt()) {
            scannedInt = scanner.nextInt();
            if (scannedInt == 0)
                break;
            else
                in.put(scannedInt);
        }

        System.out.println("Shutting all threads down");
        executor.shutdownNow();
        System.out.println("Input = " + in.buffer);
        System.out.println("Output = " + out.buffer);
    }
}
