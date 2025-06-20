package org.example.rmi;

import java.io.Serializable;
import java.net.InetAddress;

public class RemoteObjectRef implements Serializable {
    private static final long serialVersionUID = 1L;

    private InetAddress host;
    private int port;
    private String serviceName;

    public RemoteObjectRef() {}

    public RemoteObjectRef(InetAddress host, int port, String serviceName) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "RemoteObjectRef{" +
                "host=" + host +
                ", port=" + port +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
