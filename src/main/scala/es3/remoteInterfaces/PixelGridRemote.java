package es3.remoteInterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PixelGridRemote extends Remote{
    public void set(final int x, final int y, final int color) throws RemoteException;
    public int get(int x, int y) throws RemoteException;
    public int getNumRows() throws RemoteException;
    public int getNumColumns() throws RemoteException;
}
