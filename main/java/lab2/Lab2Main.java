package lab2;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Lab2Main {
    public static void main(String[] args) throws InterruptedException {
        SharedInput in = new SharedInput();
        SharedOutput out = new SharedOutput();

        for (int i = 0; i < 100; i++) {
            in.put(i);
        }


        int threadNumber;
        try {
            threadNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid input, using default thread pool of 4");
            threadNumber = 4;
        }

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
