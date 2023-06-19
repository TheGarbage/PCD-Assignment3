package es3.example;

import es3.remoteInterfaces.BrushManagerRemote;
import es3.remoteInterfaces.PixelGridRemote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class VisualiserPanel extends JPanel{
    private static final int STROKE_SIZE = 1;
    private final BrushManagerRemote brushManager; // Modificato
    private final PixelGridRemote grid; // Modificato
    private final int w,h;

    public VisualiserPanel(PixelGridRemote grid, BrushManagerRemote brushManager, int w, int h){ // Modificato
        setSize(w,h);
        this.grid = grid;
        this.w = w;
        this.h = h;
        this.brushManager = brushManager;
        this.setPreferredSize(new Dimension(w, h));
    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());

        try {
            int dx = w / grid.getNumColumns();
            int dy = h / grid.getNumRows();
            g2.setStroke(new BasicStroke(STROKE_SIZE));
            for (int i = 0; i < grid.getNumRows(); i++) {
                int y = i * dy;
                g2.drawLine(0, y, w, y);
            }

            for (int i = 0; i < grid.getNumColumns(); i++) {
                int x = i * dx;
                g2.drawLine(x, 0, x, h);
            }

            for (int row = 0; row < grid.getNumRows(); row++) {
                int y = row * dy;
                for (int column = 0; column < grid.getNumColumns(); column++) {
                    int x = column * dx;
                    int color = grid.get(column, row);
                    if (color != 0) {
                        g2.setColor(new Color(color));
                        g2.fillRect(x + STROKE_SIZE, y + STROKE_SIZE, dx - STROKE_SIZE, dy - STROKE_SIZE);
                    }
                }
            }

            brushManager.getBrushes().forEach((key, brush) -> { // Modificato
                g2.setColor(new Color(brush.getColor()));
                var circle = new java.awt.geom.Ellipse2D.Double(brush.getX() - BrushManager.BRUSH_SIZE / 2.0, brush.getY() - BrushManager.BRUSH_SIZE / 2.0, BrushManager.BRUSH_SIZE, BrushManager.BRUSH_SIZE);
                // draw the polygon
                g2.fill(circle);
                g2.setStroke(new BasicStroke(STROKE_SIZE));
                g2.setColor(Color.BLACK);
                g2.draw(circle);
            });

        } catch (RemoteException e) {
            System.err.println("VisualiserPanel exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
