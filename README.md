# Trabalho 2 de Sistemas Distribuídos

Trabalho 2 da disciplina de Sistemas Distribuídos. 

## Tema da Aplicação

Sistema de Venda de Livros (sebo) 

## Equipe

- Antonio Herik Cosmo Martins - 516098
- Luigy Gabriel de Oliveira Ferreira - 542161

## Estrutura do Projeto

- A estrutura de pastas:
  - /model - Entidades, Interfaces e Agregadores da aplicação
  - /rmi - Tem as classes de implementação do RMI (Interface, Implementação, Servidor, Cliente e Teste)
    - /rmi/protocol - Classes de estrutura das mensagens de requisição/resposta e utilitário para serialização do JSON
  - /service - Serviços do sistema

## Relatório

 Teste [Link para o relatório](relatorioSD2.pdf)

## Para testar

- Executar "rmiregistry &" no terminal para registrar o RMI
- Executar RMIServer
- Executar TestRMI
