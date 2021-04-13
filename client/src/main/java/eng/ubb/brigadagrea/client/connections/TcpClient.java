package eng.ubb.brigadagrea.client.connections;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import eng.ubb.brigadagrea.common.ElectronicStoreService;
import eng.ubb.brigadagrea.common.ElectronicStoreServiceException;
import eng.ubb.brigadagrea.common.Message;

public class TcpClient {
    private ExecutorService executorService;

    public TcpClient(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Message sendAndReceive(Message request) {
        try (var socket = new Socket(ElectronicStoreService.HOST, ElectronicStoreService.PORT);
             var is = socket.getInputStream();
             var os = socket.getOutputStream()) {

            System.out.println("---Sending request: " + request + "---");
            request.writeTo(os);
            System.out.println("---Request sent---");

            Message response = new Message();
            response.readFrom(is);
            System.out.println("---Received response: " + response + "---");

            return response;

        } catch (IOException e) {
            e.printStackTrace();
            throw new ElectronicStoreServiceException("Exception occurred during send and receive process", e);
        }
    }
}
