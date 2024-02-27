package lab3;

import java.util.concurrent.TimeUnit;

public class Lab3Main {
    public static void main(String[] args) {
        Server.main(new String[]{"8080"});
        new Thread (() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Client.main(new String[]{"localhost", "8080"});
        }).start();
        //Client.main(new String[]{"localhost", "8080"});
    }
}
