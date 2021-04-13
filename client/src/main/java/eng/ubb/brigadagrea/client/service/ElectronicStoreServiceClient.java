package eng.ubb.brigadagrea.client.service;

import eng.ubb.brigadagrea.client.connections.TcpClient;
import eng.ubb.brigadagrea.common.ElectronicStoreService;
import eng.ubb.brigadagrea.common.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ElectronicStoreServiceClient implements ElectronicStoreService {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public ElectronicStoreServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<String> sayHello(String name) {
        //build a request of type Message
        //send the request and receive a response of type Message
        //extract result from response

        return executorService.submit(() -> {
            Message request = new Message(ElectronicStoreService.SAY_HELLO, name);
            Message response = tcpClient.sendAndReceive(request);
            String result = response.getBody();
            //todo: handle exceptions e.g. status error

            return result;
        });
    }

    public CompletableFuture<String> makeRequest(String header, String body){
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message(header, body);
            Message response = tcpClient.sendAndReceive(request);
            String result = response.getBody();
            //todo: handle exceptions e.g. status error

            return result;
        });
    }

    public CompletableFuture<String> addClient(String client){
        return makeRequest(ElectronicStoreService.ADD_CLIENT, client);
    }

    public CompletableFuture<String> updateClient(String client){
        return makeRequest(ElectronicStoreService.UPDATE_CLIENT, client);
    }

    public CompletableFuture<String> removeClient(String client){
        return makeRequest(ElectronicStoreService.REMOVE_CLIENT, client);
    }

    public CompletableFuture<String> listClients(){
        return makeRequest(ElectronicStoreService.LIST_CLIENTS, "");
    }

    public CompletableFuture<String> filterClients(String score){
        return makeRequest(ElectronicStoreService.FILTER_CLIENTS, score);
    }

    public CompletableFuture<String> reportClients(){
        return makeRequest(ElectronicStoreService.REPORT_CLIENTS, "");
    }
}
