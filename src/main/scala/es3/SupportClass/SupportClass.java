package es3.SupportClass;

import es3.example.BrushManager;
import es3.example.PixelGridView;

import java.util.Random;

public class SupportClass {
    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }
}
