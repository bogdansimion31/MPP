package eng.ubb.brigadagrea.client;

import eng.ubb.brigadagrea.client.connections.TcpClient;
import eng.ubb.brigadagrea.client.service.ElectronicStoreServiceClient;
import eng.ubb.brigadagrea.client.service.ElectronicStoreServiceOrder;
import eng.ubb.brigadagrea.client.service.ElectronicStoreServiceProduct;
import eng.ubb.brigadagrea.client.ui.*;
import eng.ubb.brigadagrea.common.ElectronicStoreService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        TcpClient tcpClient = new TcpClient(executorService);

        ElectronicStoreServiceClient electronicStoreServiceClient = new ElectronicStoreServiceClient(executorService, tcpClient);
        ElectronicStoreServiceProduct electronicStoreServiceProduct = new ElectronicStoreServiceProduct(executorService, tcpClient);
        ElectronicStoreServiceOrder electronicStoreServiceOrder = new ElectronicStoreServiceOrder(executorService, tcpClient);

        SomeConsole someConsole = new SomeConsole(electronicStoreServiceClient);
        // Test Connection:
        // someConsole.runConsole();
        ClientConsole clientConsole = new ClientConsole(electronicStoreServiceClient);
        ProductConsole productConsole = new ProductConsole(electronicStoreServiceProduct);
        OrderConsole orderConsole = new OrderConsole(electronicStoreServiceOrder);

        Console console = new Console(clientConsole, orderConsole, productConsole);
        console.runConsole();

        executorService.shutdown();
        System.out.println("Disconnected");
    }
}
