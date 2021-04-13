package eng.ubb.brigadagrea.client.ui;


import eng.ubb.brigadagrea.client.service.ElectronicStoreServiceOrder;
import eng.ubb.brigadagrea.common.exceptions.StoreException;
import eng.ubb.brigadagrea.common.exceptions.SystemException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class OrderConsole{
    private ElectronicStoreServiceOrder orderService;

    public OrderConsole(ElectronicStoreServiceOrder orderService) {
        this.orderService = orderService;
    }

    public void runConsole() throws SAXException, TransformerException, ParserConfigurationException, IOException, ExecutionException, InterruptedException {
        label: while(true) {
            displayManageMenu();
            int new_choice = 0;
            Scanner scanner = new Scanner(System.in);
            try {
                new_choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please input a number between 1 and 6!");
            }

            switch (new_choice) {
                case 1:
                    String new_order = readOrder();
                    addOrder(new_order);
                    break;
                case 2:
                    String updated_order = readOrderUpdate();
                    updateOrder(updated_order);
                    break;
                case 3:
                    String to_remove_order = readRemoveOrder();
                    removeOrder(to_remove_order);
                    break;
                case 4:
                    listOrders();
                    break;
                case 5:
                    String filter_parameter = filterOrdersParameter();
                    filterOrders(filter_parameter);
                    break;
                case 6:
                    //exit
                    break label;
                default:
                    System.out.println("Please input a number between 1 and 6!");
            }

        }
    }

    private void printResponse(CompletableFuture<String> response) throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        System.out.println("\n");
        String uglyResponse = response.get();
        String[] prettyResponses = uglyResponse.split("\\|");
        for (String prettyResponse : prettyResponses) {
            System.out.println(prettyResponse);
        }
        System.out.println("\n");
    }

    private void addOrder(String order) throws SAXException, TransformerException, ParserConfigurationException, IOException, ExecutionException, InterruptedException {
        printResponse(orderService.addOrder(order));
    }

    private void updateOrder(String order) throws SAXException, TransformerException, ParserConfigurationException, IOException, ExecutionException, InterruptedException {
        printResponse(orderService.updateOrder(order));
    }

    private void removeOrder(String order) throws SAXException, TransformerException, ParserConfigurationException, IOException, ExecutionException, InterruptedException {
        printResponse(orderService.removeOrder(order));
    }

    private void filterOrders(String parameter) throws SAXException, TransformerException, ParserConfigurationException, IOException, ExecutionException, InterruptedException {
        printResponse(orderService.filterOrders(parameter));
    }

    private String readOrder(){
        System.out.println("order id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String firstLine = bufferRead.readLine();
            String order_id = "0";
            if (!firstLine.equals("none")){
                order_id = firstLine;
            }
            System.out.println("client id:");
            String client_id = bufferRead.readLine();
            System.out.println("product id:");
            String product_id = bufferRead.readLine();
            System.out.println("quantity: ");
            String quantity = bufferRead.readLine();

            return order_id + "|" + client_id + "|" + product_id  + "|" + quantity;
        } catch (IOException e) {
            throw new SystemException("System error");
        }
    }

    private String readOrderUpdate(){
        System.out.println("order id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String firstLine = bufferRead.readLine();
            String order_id = "0";
            if (!firstLine.equals("none"))  order_id = firstLine;
            System.out.println("new quantity");
            String quantity = bufferRead.readLine();

            return order_id + "|" + quantity;

        } catch (IOException e) {
            throw new SystemException("System error.");
        }
    }

    private String readRemoveOrder(){
        System.out.println("order id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String firstLine = bufferRead.readLine();
            String order_id = "0";
            if (!firstLine.equals("none"))  order_id = firstLine;

            return order_id;

        } catch (IOException e) {
            throw new SystemException("System error");
        }
    }

    private void listOrders() throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        printResponse(orderService.listOrders());
    }

    private String filterOrdersParameter() throws SAXException, ParserConfigurationException, IOException, TransformerException {
        System.out.println("client name: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String filter="";
        try{
            filter = bufferRead.readLine();
        }catch (IOException e) {throw new SystemException("System error");}

        return filter;
    }

    private void displayManageMenu(){
        System.out.println("|   ORDER MENU");
        System.out.println("|   Options:");
        System.out.println("|1. Add order");
        System.out.println("|2. Update order");
        System.out.println("|3. Delete order");
        System.out.println("|4. List all orders");
        System.out.println("|5. Filter orders");
        System.out.println("|6. Back to main menu");

    }

}
