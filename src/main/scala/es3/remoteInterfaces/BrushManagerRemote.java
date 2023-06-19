package es3.remoteInterfaces;

import es3.example.BrushManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface BrushManagerRemote extends Remote {
    int addBrush(final BrushManager.Brush brush) throws RemoteException;
    public void updateBrush(final int key, final BrushManager.Brush brush) throws RemoteException;
    public HashMap<Integer, BrushManager.Brush> getBrushes() throws RemoteException;
    public void removeBrush(final int key) throws RemoteException;
}
