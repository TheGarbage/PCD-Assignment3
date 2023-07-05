package es2.example;


import es2.actors.Receiver;

import java.awt.*;
import java.util.HashMap;

public class BrushManager {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    private static final int REMOTE_BRUSH_TRASPARENCY = 128; //Modifica
    private HashMap<Integer,Brush> brushes = new java.util.HashMap<>(); //Modifica

    void draw(final Graphics2D g) {
        brushes.forEach((key, brush) -> { //Modifica
            var color = new Color(brush.color); //Modifica
            if(brush.isRemote) //Modifica
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), REMOTE_BRUSH_TRASPARENCY); //Modifica
            g.setColor(color); //Modifica
            var circle = new java.awt.geom.Ellipse2D.Double(brush.x - BRUSH_SIZE / 2.0, brush.y - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
            // draw the polygon
            g.fill(circle);
            g.setStroke(new BasicStroke(STROKE_SIZE));
            g.setColor(brush.isRemote ? new Color(0, 0, 0, REMOTE_BRUSH_TRASPARENCY) : Color.BLACK);
            g.draw(circle);
        });
    }

    public void addBrush(final int key, final Brush brush) {
        brushes.put(key, brush);
    } //Modifica

    public Brush getBrush(final int key) {
        return brushes.get(key);
    } //Modifica

    public void removeBrush(final int key) {
        brushes.remove(key);
    } //Modifica

    public static class Brush {
        private int x, y;
        private int color;
        public final boolean isRemote; //Modifica

        public Brush(final int x, final int y, final int color, final boolean isRemote) { //Modifica
            this.x = x;
            this.y = y;
            this.color = color;
            this.isRemote = isRemote; //Modifica
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
