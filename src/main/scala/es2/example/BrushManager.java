package es2.example;


import es2.actors.Receiver;

import java.awt.*;
import java.util.HashMap;

public class BrushManager {
    private static final int BRUSH_SIZE = 10;
    private static final int STROKE_SIZE = 2;
    private HashMap<Integer,Brush> brushes = new java.util.HashMap<>(); //Modifica

    void draw(final Graphics2D g) {
        brushes.forEach((key, brush) -> { //Modifica
            var color = new Color(brush.color); //Modifica
            if(brush.transparency != Receiver.LOCAL_BRUSH_TRASPARENCY()) //Modifica
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(), brush.transparency); //Modifica
            g.setColor(color); //Modifica
            var circle = new java.awt.geom.Ellipse2D.Double(brush.x - BRUSH_SIZE / 2.0, brush.y - BRUSH_SIZE / 2.0, BRUSH_SIZE, BRUSH_SIZE);
            // draw the polygon
            g.fill(circle);
            g.setStroke(new BasicStroke(STROKE_SIZE));
            g.setColor(new Color(0, 0, 0, brush.transparency)); //Modifica
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
        public final int transparency; //Modifica

        public Brush(final int x, final int y, final int color, final int transparency) { //Modifica
            this.x = x;
            this.y = y;
            this.color = color;
            this.transparency = transparency; //Modifica
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
