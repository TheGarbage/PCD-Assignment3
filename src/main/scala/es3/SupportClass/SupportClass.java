package es3.SupportClass;

import es3.example.PixelGridView;

import java.util.Random;

public class SupportClass {
    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }

    public static Runnable viewRefresh(PixelGridView view) {
        while(true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            view.refresh();
        }
    }

}
