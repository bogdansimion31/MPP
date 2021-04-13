package eng.ubb.brigadagrea.server;

import eng.ubb.brigadagrea.common.ElectronicStoreService;
import eng.ubb.brigadagrea.common.Message;
import eng.ubb.brigadagrea.server.connections.TcpServer;
import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.validators.ClientValidator;
import eng.ubb.brigadagrea.server.domain.validators.OrderValidator;
import eng.ubb.brigadagrea.server.domain.validators.ProductValidator;
import eng.ubb.brigadagrea.server.repository.*;
import eng.ubb.brigadagrea.server.service.ClientService;
import eng.ubb.brigadagrea.server.service.ElectronicStoreServiceServer;
import eng.ubb.brigadagrea.server.service.OrderService;
import eng.ubb.brigadagrea.server.service.ProductService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {
    public static void addRequestHandlers(TcpServer connection, ElectronicStoreServiceServer service, String methodName){
        connection.addHandler(methodName, request -> {
            Future<String> res = service.requestIdentifier(request.getHeader(), request.getBody());
            try {
                String result = res.get();
                Message response = new Message(Message.OK, result);
                return response;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return new Message(Message.ERROR, e.getMessage());
            }
        });
    }

    public static void main(String[] args) throws SQLException, TransformerException, ParserConfigurationException, SAXException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        TcpServer tcpServer = new TcpServer(executorService, ElectronicStoreService.PORT);

        ClientValidator clientValidator = new ClientValidator();
        ProductValidator productValidator = new ProductValidator();
        OrderValidator orderValidator = new OrderValidator();

        //SqlClientRepository clientRepository = new SqlClientRepository(clientValidator);
        XmlClientRepository clientRepository = new XmlClientRepository(clientValidator, "server/src/main/java/eng/ubb/brigadagrea/server/repository/clients.xml");
        //SqlProductRepository productRepository = new SqlProductRepository(productValidator);
        XmlProductRepository productRepository = new XmlProductRepository(productValidator, "server/src/main/java/eng/ubb/brigadagrea/server/repository/products.xml");
        //SqlOrderRepository orderRepository = new SqlOrderRepository(orderValidator, clientRepository, productRepository);
        XmlOrderRepository orderRepository = new XmlOrderRepository(orderValidator, "server/src/main/java/eng/ubb/brigadagrea/server/repository/orders.xml", clientRepository, productRepository);

        ClientService clientService = new ClientService(clientRepository);
        ProductService productService = new ProductService(productRepository);
        OrderService orderService = new OrderService(clientRepository, productRepository, orderRepository);
        ElectronicStoreServiceServer electronicStoreService = new ElectronicStoreServiceServer(executorService, clientService, productService, orderService);

        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.ADD_CLIENT);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.REMOVE_CLIENT);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.UPDATE_CLIENT);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.LIST_CLIENTS);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.FILTER_CLIENTS);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.ADD_PRODUCT);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.REMOVE_PRODUCT);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.UPDATE_PRODUCT);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.LIST_PRODUCTS);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.FILTER_PRODUCTS);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.ADD_ORDER);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.REMOVE_ORDER);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.UPDATE_ORDER);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.LIST_ORDERS);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.FILTER_PRODUCTS);
        addRequestHandlers(tcpServer, electronicStoreService, ElectronicStoreService.REPORT_CLIENTS);

        tcpServer.startServer();
        System.out.println("Server closed");
    }
}
