package es3.rmi;

import es3.SupportClass.SupportClass;
import es3.example.BrushManager;
import es3.example.PixelGridView;
import es3.remoteInterfaces.BrushManagerRemote;
import es3.remoteInterfaces.PixelGridRemote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmiClient {
    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            BrushManagerRemote brushManager = (BrushManagerRemote) registry.lookup("BrushManager");
            PixelGridRemote pixelGrid = (PixelGridRemote) registry.lookup("PixelGrid");

            BrushManager.Brush localBrush = new BrushManager.Brush(0, 0, SupportClass.randomColor());
            int brushKey = brushManager.addBrush(localBrush);

            PixelGridView view = new PixelGridView(pixelGrid, brushManager, brushKey, 600, 600);

            view.addMouseMovedListener(localBrush::updatePosition);

            view.addPixelGridEventListener((x, y) -> {
                try {
                    pixelGrid.set(x, y, localBrush.getColor());
                } catch (RemoteException e) {
                    System.err.println("PixelGridEventListener exception: " + e.toString());
                    e.printStackTrace();
                }
            });

            view.addColorChangedListener(localBrush::setColor);

            view.display();
            new Thread(() -> {
                while(true) {
                    try {
                        Thread.sleep(10);
                        brushManager.updateBrush(brushKey, localBrush);
                    } catch (InterruptedException | RemoteException e) {
                        System.err.println("RefreshThread exception: " + e.toString());
                        e.printStackTrace();
                    }
                    view.refresh();
                }
            }).start();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
