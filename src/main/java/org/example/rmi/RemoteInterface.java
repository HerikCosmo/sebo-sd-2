package org.example.rmi;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
    String SERVICE_NAME = "VendasService";

    byte[] doOperation(RemoteObjectRef o, String methodId, byte[] arguments, InetAddress clientHost, int clientPort) throws RemoteException;
}
