package org.example.rmi;

import org.example.rmi.protocol.JsonMarshaller;
import org.example.rmi.protocol.ReplyMessage;
import org.example.rmi.protocol.RequestMessage;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.atomic.AtomicInteger;

public class RMIClient {
    private RemoteInterface remoteService;
    private AtomicInteger requestIdCounter = new AtomicInteger(0);
    private InetAddress clientHost;
    private int clientPort;

    public RMIClient() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1110);

            remoteService = (RemoteInterface) registry.lookup(RemoteInterface.SERVICE_NAME);

            this.clientHost = InetAddress.getLocalHost();
            this.clientPort = 0;

            System.out.println("Cliente RMI. Conectado ao serviço '" + RemoteInterface.SERVICE_NAME + "' on " + "localhost" + ":" + 1100);
        } catch (Exception e) {
            System.out.println("Erro no Cliente RMI: " + e.toString());
            e.printStackTrace();
        }
    }

    public ReplyMessage doOperation(String serviceName, String methodName, Object[] methodArguments) {
        if (remoteService == null) {
            System.out.println("Serviço remoto não disponível. Cliente não foi inicializado corretamente");
            return new ReplyMessage(0, "FAILURE", "Cliente não inicializado");
        }

        try {
            int currentRequestId = requestIdCounter.incrementAndGet();

            byte[] marshalledMethodArgumentsJson = null;
            if(methodArguments != null && methodArguments.length > 0) {
                marshalledMethodArgumentsJson = JsonMarshaller.marshal(methodArguments);
            } else {
                marshalledMethodArgumentsJson = JsonMarshaller.marshal(new Object[]{});
            }

            RequestMessage request = new RequestMessage(
                    currentRequestId,
                    serviceName,
                    methodName,
                    marshalledMethodArgumentsJson
            );

            byte[] requestMessageBytes = JsonMarshaller.marshal(request);

            RemoteObjectRef remoteObjectRef = new RemoteObjectRef(InetAddress.getByName("localhost"), 1110, serviceName);

            byte[] replyMessageBytes = remoteService.doOperation(
                    remoteObjectRef,
                    methodName,
                    requestMessageBytes,
                    clientHost,
                    clientPort
            );

            ReplyMessage reply = JsonMarshaller.unmarshal(replyMessageBytes, ReplyMessage.class);

            System.out.println("Cliente recebeu resposta para o requestID " + reply.getRequestId() + ": " + reply.getStatus());

            return reply;
        } catch(Exception e) {
            System.err.println("Error ao realizar operação remota: " + e.getMessage());
            e.printStackTrace();
            return new ReplyMessage(requestIdCounter.get(), "FAILURE", "Erro no Cliente: " + e.getMessage());
        }
    }
}
