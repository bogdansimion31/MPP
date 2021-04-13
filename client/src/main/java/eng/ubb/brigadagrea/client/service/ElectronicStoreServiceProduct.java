package eng.ubb.brigadagrea.client.service;

import eng.ubb.brigadagrea.client.connections.TcpClient;
import eng.ubb.brigadagrea.common.ElectronicStoreService;
import eng.ubb.brigadagrea.common.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ElectronicStoreServiceProduct implements ElectronicStoreService {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public ElectronicStoreServiceProduct(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
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

    public CompletableFuture<String> addProduct(String product){
        return makeRequest(ElectronicStoreService.ADD_PRODUCT, product);
    }

    public CompletableFuture<String> updateProduct(String product){
        return makeRequest(ElectronicStoreService.UPDATE_PRODUCT, product);
    }

    public CompletableFuture<String> removeProduct(String product){
        return makeRequest(ElectronicStoreService.REMOVE_PRODUCT, product);
    }

    public CompletableFuture<String> listProducts(){
        return makeRequest(ElectronicStoreService.LIST_PRODUCTS, "");
    }

    public CompletableFuture<String> filterProducts(String filter){
        return makeRequest(ElectronicStoreService.FILTER_PRODUCTS, filter);
    }
}
