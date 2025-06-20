package org.example.rmi;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.model.Livro;
import org.example.model.Produto;
import org.example.rmi.protocol.JsonMarshaller;
import org.example.rmi.protocol.ReplyMessage;

import java.io.IOException;
import java.util.List;

public class TestRMI {
    public static void main(String[] args) {
        RMIClient client = new RMIClient();

        System.out.println("\n--- Teste: Buscando um produto");
        ReplyMessage getProductReply = client.doOperation(RemoteInterface.SERVICE_NAME, "getProductDetails", new Object[]{"1"});
        if (getProductReply != null && "SUCCESS".equals(getProductReply.getStatus())) {
            try {
                Produto retrievedProduct = JsonMarshaller.unmarshal(getProductReply.getResult(), Produto.class);
                System.out.println("Produto encontrado: " + retrievedProduct);
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Falha ao buscar produto: " + (getProductReply != null ? getProductReply.getErrorMessage() : "Unknown error"));
        }

        System.out.println("\n--- Teste: Vendendo um produto");
        ReplyMessage sellProductReply = client.doOperation(RemoteInterface.SERVICE_NAME, "sellProduct", new Object[]{"3"});
        if (sellProductReply != null && "SUCCESS".equals(sellProductReply.getStatus())) {
            try {
                String message = JsonMarshaller.unmarshal(sellProductReply.getResult(), String.class);
                System.out.println("Produto vendido: " + message);
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Falha ao vender produto: " + (sellProductReply != null ? sellProductReply.getErrorMessage() : "Unknown error"));
        }

        System.out.println("\n--- Teste: procurando por um produto pelo título");
        ReplyMessage searchReply = client.doOperation(RemoteInterface.SERVICE_NAME, "searchProductsByTitle", new Object[]{"guia"});
        if (searchReply != null && "SUCCESS".equals(searchReply.getStatus()) && searchReply.getResult() != null) {
            try {

                List<Produto> searchResults = JsonMarshaller.unmarshal(searchReply.getResult(), new TypeReference<List<Produto>>() {});
                if (searchResults != null) {
                    searchResults.forEach(System.out::println);
                }
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Erro ao buscar produtos: " + (searchReply != null ? searchReply.getErrorMessage() : "Unknown error"));
        }

        System.out.println("\n--- Teste: buscando todos os livros");
        ReplyMessage getAllBooksReply = client.doOperation(RemoteInterface.SERVICE_NAME, "getAllBooks", new Object[]{});
        if (getAllBooksReply != null && "SUCCESS".equals(getAllBooksReply.getStatus()) && getAllBooksReply.getResult() != null) {
            try {
                List<Livro> allBooks = JsonMarshaller.unmarshal(getAllBooksReply.getResult(), new TypeReference<List<Livro>>() {});
                System.out.println("Livros:");
                if (allBooks != null) {
                    allBooks.forEach(System.out::println);
                }
            } catch (IOException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Falha ao buscar livros: " + (getAllBooksReply != null ? getAllBooksReply.getErrorMessage() : "Unknown error"));
        }

        System.out.println("\n--- Teste: buscando produto que não existe");
        ReplyMessage nonExistentReply = client.doOperation(RemoteInterface.SERVICE_NAME, "getProductDetails", new Object[]{"B999"});
        if (nonExistentReply != null && "FAILURE".equals(nonExistentReply.getStatus())) {
            System.out.println("Produto não existe. Falha esperada: " + nonExistentReply.getErrorMessage());
        } else {
            System.out.println("Erro: " + (nonExistentReply != null ? nonExistentReply.getStatus() : "null reply"));
        }

        System.out.println("\n--- Teste: método que não existe");
        client.doOperation(RemoteInterface.SERVICE_NAME, "testandoErro", new Object[]{});
    }
}