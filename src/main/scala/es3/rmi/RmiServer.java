package es3.rmi;

import es3.example.BrushManager;
import es3.example.PixelGrid;
import es3.example.PixelGridView;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RmiServer {

    public static int randomColor() {
        Random rand = new Random();
        return rand.nextInt(256 * 256 * 256);
    }

    public static void main(String args[]) {

        try {
            PixelGrid grid = new PixelGrid(40, 40);
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                grid.set(rand.nextInt(40), rand.nextInt(40), randomColor());
            }

            PixelGrid gridStub = (PixelGrid) UnicastRemoteObject.exportObject(grid, 0);
            BrushManager brushManagerStub = (BrushManager) UnicastRemoteObject.exportObject(new BrushManager(), 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("PixelGrid", gridStub);
            registry.rebind("BrushManager", brushManagerStub);

            System.out.println("Objects registered.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
