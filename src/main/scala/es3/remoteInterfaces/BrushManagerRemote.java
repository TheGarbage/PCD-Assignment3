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
    public void updateBrushPosition(final int key, final int x, final int y) throws RemoteException;
    public void updateBrushColor(final int key, final int color) throws RemoteException;
    public int getBrushColor(final int key) throws RemoteException;
}
