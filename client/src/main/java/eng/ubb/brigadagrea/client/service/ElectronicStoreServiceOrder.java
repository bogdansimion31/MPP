package eng.ubb.brigadagrea.client.service;

import eng.ubb.brigadagrea.client.connections.TcpClient;
import eng.ubb.brigadagrea.common.ElectronicStoreService;
import eng.ubb.brigadagrea.common.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ElectronicStoreServiceOrder implements ElectronicStoreService {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public ElectronicStoreServiceOrder(ExecutorService executorService, TcpClient tcpClient) {
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

    public CompletableFuture<String> addOrder(String order){
        return makeRequest(ElectronicStoreService.ADD_ORDER, order);
    }

    public CompletableFuture<String> updateOrder(String order){
        return makeRequest(ElectronicStoreService.UPDATE_ORDER, order);
    }

    public CompletableFuture<String> removeOrder(String order){
        return makeRequest(ElectronicStoreService.REMOVE_ORDER, order);
    }

    public CompletableFuture<String> listOrders(){
        return makeRequest(ElectronicStoreService.LIST_ORDERS, "");
    }

    public CompletableFuture<String> filterOrders(String filter){
        return makeRequest(ElectronicStoreService.FILTER_ORDERS, filter);
    }
}
