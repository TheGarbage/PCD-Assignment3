package es3.example;

import com.hierynomus.asn1.encodingrules.ber.BERDecoder;
import es3.remoteInterfaces.BrushManagerRemote;

import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

public class BrushManager implements BrushManagerRemote { // Modificato
    public static final int BRUSH_SIZE = 10; // Modificato
    public static final int STROKE_SIZE = 2; // Modificato
    private HashMap<Integer, Brush> brushes = new HashMap(); // Modificato
    private int keyCount = 0;

    public HashMap<Integer, Brush> getBrushes(){ // Modificato
        return (HashMap<Integer, Brush>)brushes.clone();
    }

    public int addBrush(final Brush brush) { // Modificato
        brushes.put(keyCount, brush);
        return keyCount++;
    }

    public void removeBrush(final int key) { // Modificato
        brushes.remove(key);
    }

    public void updateBrushPosition(final int key, final int x, final int y){ // Modificato
        brushes.get(key).updatePosition(x, y);
    }

    public void updateBrushColor(final int key, final int color){ // Modificato
        brushes.get(key).setColor(color);
    }

    public int getBrushColor(int key){ // Modificato
        return brushes.get(key).getColor();
    }

    public static class Brush implements Serializable { // Modificato
        private int x, y;
        private int color;

        public Brush(final int x, final int y, final int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void updatePosition(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        // write after this getter and setters
        public int getX(){
            return this.x;
        }
        public int getY(){
            return this.y;
        }
        public int getColor(){
            return this.color;
        }
        public void setColor(int color){
            this.color = color;
        }
    }
}
