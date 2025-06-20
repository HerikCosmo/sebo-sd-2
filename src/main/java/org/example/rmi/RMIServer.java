package org.example.rmi;

import org.example.model.Livro;
import org.example.model.aggregation.Loja;
import org.example.service.ProdutoService;
import org.example.service.VendasService;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            Loja loja = new Loja("Meu Sebo", "Rua 123");

            loja.addProduct(new Livro("1", "O Guia dos Mochileiros das Galáxias", 15.99, "Comédia e Sci-fi", "Usado", "Douglas Adams", "978-0345391803", 1979, "Ficção Científica"));
            loja.addProduct(new Livro("2", "1984", 12.50, "Novela Distópica", "Usado", "George Orwell", "978-0451524935", 1949, "Dystopian"));
            loja.addProduct(new Livro("3", "Orgulho e Preconceito", 10.00, "Romance clássico.", "Novo", "Jane Austen", "978-0486284738", 1813, "Romance"));
            loja.addProduct(new Livro("4", "O Guia dos Mochileiros das Galáxias 2", 16.99, "Comédia e Sci-fi", "Usado", "Douglas Adams", "978-0345391803", 1980, "Ficção Científica"));

            ProdutoService produtoService = new ProdutoService(loja);
            VendasService vendasService = new VendasService(loja);

            RemoteInterface obj = new RemoteInterfaceImpl(produtoService, vendasService);

            Registry registry = LocateRegistry.createRegistry(1110);
            registry.rebind(RemoteInterface.SERVICE_NAME, obj);

            System.out.println("Servidor RMI. Serviço '" + RemoteInterface.SERVICE_NAME + "' vinculado ao registro da porta " + 1100);
            System.out.println("IP do servidor: " + InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            System.err.println("Erro do servidor: " + e.toString());
            e.printStackTrace();
        }


    }
}
