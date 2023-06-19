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

            int brushKey = brushManager.addBrush(new BrushManager.Brush(0, 0, SupportClass.randomColor()));

            PixelGridView view = new PixelGridView(pixelGrid, brushManager, brushKey, 600, 600);

            view.addMouseMovedListener((x, y) -> {
                try {
                    brushManager.updateBrushPosition(brushKey, x, y);
                } catch (RemoteException e) {
                    System.err.println("MouseMovedListener exception: " + e.toString());
                    e.printStackTrace();
                }
            });

            view.addPixelGridEventListener((x, y) -> {
                try {
                    pixelGrid.set(x, y, brushManager.getBrushColor(brushKey));
                } catch (RemoteException e) {
                    System.err.println("PixelGridEventListener exception: " + e.toString());
                    e.printStackTrace();
                }
            });

            view.addColorChangedListener(color -> {
                try {
                    brushManager.updateBrushColor(brushKey, color);
                } catch (RemoteException e) {
                System.err.println("ColorChangedListener exception: " + e.toString());
                e.printStackTrace();
                }
            });

            view.display();
            new Thread(SupportClass.viewRefresh(view)).start();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
