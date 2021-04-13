package eng.ubb.brigadagrea.server.service;

import eng.ubb.brigadagrea.common.ElectronicStoreService;
import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.Order;
import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ElectronicStoreServiceServer implements ElectronicStoreService {
    private ExecutorService executorService;
    private ClientService clientService;
    private ProductService productService;
    private OrderService orderService;

    public ElectronicStoreServiceServer(ExecutorService executorService,
                                        ClientService clientService,
                                        ProductService productService,
                                        OrderService orderService) {
        this.executorService = executorService;
        this.clientService = clientService;
        this.productService = productService;
        this.orderService = orderService;
    }

    public Future<String> requestIdentifier(String clientHeader, String clientBody) {
        switch (clientHeader) {
            case "reportClients" -> {
                return reportClients();
            }
            case "addClient" -> {
                return addClient(clientBody);
            }
            case "removeClient" -> {
                return removeClient(clientBody);
            }
            case "updateClient" -> {
                return updateClient(clientBody);
            }
            case "listClients" -> {
                return listClients();
            }
            case "filterClients" -> {
                return filterClients(clientBody);
            }
            case "addProduct" -> {
                return addProduct(clientBody);
            }
            case "removeProduct" -> {
                return removeProduct(clientBody);
            }
            case "updateProduct" -> {
                return updateProduct(clientBody);
            }
            case "listProducts" -> {
                return listProducts();
            }
            case "filterProduct" -> {
                return filterProducts(clientBody);
            }
            case "addOrder" -> {
                return addOrder(clientBody);
            }
            case "removeOrder" -> {
                return removeOrder(clientBody);
            }
            case "updateOrder" -> {
                return updateOrder(clientBody);
            }
            case "listOrders" -> {
                return listOrders();
            }
            case "filterOrders" -> {
                return filterOrders(clientBody);
            }
            default -> {
                return executorService.submit(() -> "Method not found");
            }
        }
    }

    @Override
    public Future<String> sayHello(String name) {
        return executorService.submit(() -> {
            return "Hello " + name;
        });
    }

    public Future<String> addClient(String methodParameters) {
        List<String> requestParameters= Arrays.asList(methodParameters.split("\\|"));
        Client client = new Client(requestParameters.get(1), requestParameters.get(2));
        client.setId(Long.parseLong(requestParameters.get(0)));
        try{
            this.clientService.addClient(client);
            return executorService.submit(() -> "Client added.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
                return executorService.submit(e::getMessage);
        }
    }

    public Future<String> removeClient(String methodParameters) {
        try{
            this.clientService.removeClient(Long.parseLong(methodParameters));
            return executorService.submit(() -> "Client removed.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> updateClient(String methodParameters) {
        List<String> requestParameters= Arrays.asList(methodParameters.split("\\|"));
        Client client = new Client(requestParameters.get(1), requestParameters.get(2));
        client.setId(Long.parseLong(requestParameters.get(0)));
        try{
            this.clientService.updateClient(Long.parseLong(requestParameters.get(0)), client);
            return executorService.submit(() -> "Client updated.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> listClients() {
        try{
            Set<Client> clients = this.clientService.getAllClients();
            String result = "";
            for(Client client:clients) {
                result = result.concat(client.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> filterClients(String methodParameters) {
        try{
            Set<Client> clients = this.clientService.filterClientsByFidelityScore(Integer.parseInt(methodParameters));
            String result = "";
            for(Client client:clients) {
                result = result.concat(client.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> addProduct(String methodParameters) {
        List<String> requestParameters= Arrays.asList(methodParameters.split("\\|"));
        Product product = new Product(requestParameters.get(1), requestParameters.get(2), Integer.parseInt(requestParameters.get(3)));
        product.setId(Long.parseLong(requestParameters.get(0)));
        try{
            this.productService.addProduct(product);
            return executorService.submit(() -> "Product added.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> removeProduct(String methodParameters) {
        try{
            this.productService.removeProduct(Long.parseLong(methodParameters));
            return executorService.submit(() -> "Product removed.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> updateProduct(String methodParameters) {
        List<String> requestParameters= Arrays.asList(methodParameters.split("\\|"));
        Product product = new Product(requestParameters.get(1), requestParameters.get(2), Integer.parseInt(requestParameters.get(3)));
        product.setId(Long.parseLong(requestParameters.get(0)));
        try{
            this.productService.addProduct(product);
            return executorService.submit(() -> "Product updated.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> listProducts() {
        try{
            Set<Product> products = this.productService.getAllProducts();
            String result = "";
            for(Product product:products) {
                result = result.concat(product.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> filterProducts(String methodParameters) {
        try{
            Set<Product> products = this.productService.filterProductsByCategory(methodParameters);
            String result = "";
            for(Product product:products) {
                result = result.concat(product.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> addOrder(String methodParameters) {
        List<String> requestParameters= Arrays.asList(methodParameters.split("\\|"));
        try{
            this.orderService.addOrder(Long.parseLong(requestParameters.get(0)), Long.parseLong(requestParameters.get(1)), Long.parseLong(requestParameters.get(2)), Integer.parseInt(requestParameters.get(3)));
            return executorService.submit(() -> "Order added.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> removeOrder(String methodParameters) {
        try{
            this.orderService.removeOrder(Long.parseLong(methodParameters));
            return executorService.submit(() -> "Order removed.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> updateOrder(String methodParameters) {
        List<String> requestParameters= Arrays.asList(methodParameters.split("\\|"));
        try{
            this.orderService.updateOrder(Long.parseLong(requestParameters.get(0)), Integer.parseInt(requestParameters.get(2)));
            return executorService.submit(() -> "Order updated.");
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> listOrders() {
        try{
            Set<Order> orders = this.orderService.getAllOrders();
            String result = "";
            for(Order order:orders) {
                result = result.concat(order.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> filterOrders(String methodParameters) {
        try{
            Set<Order> orders = this.orderService.filterOrdersByClientName(methodParameters);
            String result = "";
            for(Order order:orders) {
                result = result.concat(order.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }

    public Future<String> reportClients() {
        try{
            Set<Client> clients = this.clientService.getAllClients();
            List<Client> sortedList = clients.stream()
                    .sorted(Comparator.comparingInt(Client::getFidelityScore)
                            .reversed())
                    .collect(Collectors.toList());
            String result = "";
            for(Client client:sortedList) {
                result = result.concat(client.toString() + "|");
            }
            String finalResult = result;
            return executorService.submit(() -> finalResult);
        }catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e){
            return executorService.submit(e::getMessage);
        }
    }
}
