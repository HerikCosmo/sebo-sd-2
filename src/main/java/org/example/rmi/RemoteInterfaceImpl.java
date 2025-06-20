package org.example.rmi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Livro;
import org.example.model.Produto;
import org.example.rmi.protocol.JsonMarshaller;
import org.example.rmi.protocol.ReplyMessage;
import org.example.rmi.protocol.RequestMessage;
import org.example.service.ProdutoService;
import org.example.service.VendasService;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RemoteInterfaceImpl extends UnicastRemoteObject implements RemoteInterface {
    private static final long serialVersionUID = 1L;
    private ProdutoService productService;
    private VendasService salesService;
    private final ObjectMapper objectMapper;

    public RemoteInterfaceImpl(ProdutoService productService, VendasService salesService) throws RemoteException {
        super();
        this.productService = productService;
        this.salesService = salesService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public byte[] doOperation(RemoteObjectRef o, String methodId, byte[] arguments, InetAddress clientHost, int clientPort) throws RemoteException {
        RequestMessage request = null;
        ReplyMessage reply = null;
        int currentRequestId = 0;

        try {
            request = JsonMarshaller.unmarshal(arguments, RequestMessage.class);
            currentRequestId = request.getRequestId();
            System.out.println("Servidor (RMI) recebendo requisição de " + clientHost.getHostAddress() + ":" + clientPort);

            String serviceName = request.getObjectReference();
            String methodName = request.getMethodId();
            byte[] methodArgsJson = request.getArguments();

            List<Object> actualMethodArgsList;
            if (methodArgsJson != null && methodArgsJson.length > 0) {
                actualMethodArgsList = objectMapper.readValue(methodArgsJson, new TypeReference<List<Object>>() {});
            } else {
                actualMethodArgsList = List.of();
            }

            switch (methodName) {
                case "getProductDetails":
                    String productId;
                    if (!actualMethodArgsList.isEmpty()) {
                        productId = objectMapper.convertValue(actualMethodArgsList.get(0), String.class);

                    } else {
                        throw new IllegalArgumentException("getProductDetails precisa de argumentos");
                    }
                    Produto productFound = productService.getProductDetails(productId);

                    if (productFound != null) {
                        reply = new ReplyMessage(currentRequestId, "SUCCESS", JsonMarshaller.marshal(productFound));
                    } else {
                        reply = new ReplyMessage(currentRequestId, "FAILURE", "Produto não encontrado");
                    }
                    break;

                case "sellProduct":
                    String sellProductId;
                    if (!actualMethodArgsList.isEmpty()) {
                        sellProductId = objectMapper.convertValue(actualMethodArgsList.get(0), String.class);
                    } else {
                        throw new IllegalArgumentException("sellProduct precisa de argumentos");
                    }
                    boolean sold = salesService.sellProduct(sellProductId);
                    if (sold) {
                        reply = new ReplyMessage(currentRequestId, "SUCCESS", JsonMarshaller.marshal("Produto vendido."));
                    } else {
                        reply = new ReplyMessage(currentRequestId, "FAILURE", "Falha ao vender produto");
                    }
                    break;

                case "searchProductsByTitle":
                    String keyword;
                    if (!actualMethodArgsList.isEmpty()) {
                        keyword = objectMapper.convertValue(actualMethodArgsList.get(0), String.class);
                    } else {
                        throw new IllegalArgumentException("searchProductsByTitle precisa de argumentos");
                    }
                    List<Produto> searchResults = productService.searchProductsByTitle(keyword);
                    reply = new ReplyMessage(currentRequestId, "SUCCESS", JsonMarshaller.marshal(searchResults));
                    break;

                case "getAllBooks":
                    List<Livro> allBooks = productService.getAllBooks();
                    reply = new ReplyMessage(currentRequestId, "SUCCESS", JsonMarshaller.marshal(allBooks));
                    break;
                default:
                    reply = new ReplyMessage(currentRequestId, "FAILURE", "Método deconhecido: " + methodName);
                    break;
            }

        } catch (IOException e) {
            System.err.println("Error ao processar JSON: " + e.getMessage());
            reply = new ReplyMessage(currentRequestId, "FAILURE", "Erro ao processar (JSON): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            reply = new ReplyMessage(currentRequestId, "FAILURE", "Erro interno do servidor: " + e.getMessage());
        }

        try {
            System.out.println("Servidor (RMI) mandando Resposta: " + reply.getStatus() + " para o requestID: " + reply.getRequestId());
            return JsonMarshaller.marshal(reply);
        } catch (IOException e) {
            System.err.println("Error ao serializar resposta JSON: " + e.getMessage());
            throw new RemoteException("Error ao serializar resposta JSON", e);
        }
    }
}
