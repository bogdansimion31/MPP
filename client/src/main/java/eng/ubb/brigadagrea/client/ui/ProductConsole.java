package eng.ubb.brigadagrea.client.ui;


import eng.ubb.brigadagrea.client.service.ElectronicStoreServiceProduct;
import eng.ubb.brigadagrea.common.exceptions.StoreException;
import eng.ubb.brigadagrea.common.exceptions.SystemException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProductConsole {
    private ElectronicStoreServiceProduct productService;

    public ProductConsole(ElectronicStoreServiceProduct productService) {
        this.productService = productService;
    }
    private void displayManageMenu(){
        System.out.println("|   PRODUCT MENU");
        System.out.println("|   Options:");
        System.out.println("|1. Add product");
        System.out.println("|2. Update product");
        System.out.println("|3. Delete product");
        System.out.println("|4. List all products");
        System.out.println("|5. Filter products");
        System.out.println("|6. Back to main menu");

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
                    String new_product = readProduct();
                    addProduct(new_product);
                    break;
                case 2:
                    String update_product = readProduct();
                    updateProduct(update_product);
                    break;
                case 3:
                    String to_remove_product = readRemoveProduct();
                    removeProduct(to_remove_product);
                    break;
                case 4:
                    listProducts();
                    break;
                case 5:
                    filterProducts();
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

    private void addProduct(String product) throws ParserConfigurationException, IOException, SAXException, TransformerException, ExecutionException, InterruptedException {
        printResponse(productService.addProduct(product));
    }

    private void updateProduct(String product) throws IOException, ParserConfigurationException, TransformerException, SAXException, ExecutionException, InterruptedException {
        printResponse(productService.updateProduct(product));
    }

    private void removeProduct(String product) throws IOException, ParserConfigurationException, TransformerException, SAXException, ExecutionException, InterruptedException {
        printResponse(productService.removeProduct(product));
    }

    private String readProduct() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("id: ");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("name:");
            String name = bufferRead.readLine();
            System.out.println("category: ");
            String category = bufferRead.readLine();
            System.out.println("stock: ");
            String stock = bufferRead.readLine();

            return id + "|" + name + "|" + category + "|" + stock;
        } catch (IOException e) {
            throw new SystemException("System error");
        }
    }

    private String readRemoveProduct(){
        System.out.println("product id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String id = "0";
        try{
            String firstLine = bufferRead.readLine();
            if (!firstLine.equals("none")){
                id =firstLine;
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        return id;
    }

    private void listProducts() throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        printResponse(productService.listProducts());
    }
    private void filterProducts() throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        System.out.println("Category name must contain:");
        String filter="";
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try{
            filter = bufferRead.readLine();
        }catch (IOException e) {e.printStackTrace();}

        printResponse(productService.filterProducts(filter));
    }
}
