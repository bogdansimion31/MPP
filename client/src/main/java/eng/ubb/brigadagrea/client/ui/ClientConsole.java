package eng.ubb.brigadagrea.client.ui;

import eng.ubb.brigadagrea.client.service.ElectronicStoreServiceClient;
import eng.ubb.brigadagrea.common.exceptions.StoreException;
import eng.ubb.brigadagrea.common.exceptions.SystemException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ClientConsole{
    private ElectronicStoreServiceClient clientService;

    public ClientConsole(ElectronicStoreServiceClient clientService) {
        this.clientService = clientService;
    }


    private void displayManageMenu(){
        System.out.println("|   CLIENT MENU");
        System.out.println("|   Options:");
        System.out.println("|1. Add client");
        System.out.println("|2. Update client");
        System.out.println("|3. Delete client");
        System.out.println("|4. List all clients");
        System.out.println("|5. Filter clients");
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
                    String new_client = readClient();
                    addClient(new_client);
                    break;
                case 2:
                    String updated_client = readClientUpdate();
                    updateClient(updated_client);
                    break;
                case 3:
                    printResponse(removeClient());
                    break;
                case 4:
                    listClients();
                    break;
                case 5:
                    filterClients();
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

    private void addClient(String new_client) throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        printResponse(clientService.addClient(new_client));
    }

    private void updateClient(String updated_client) throws IOException, SAXException, TransformerException, ParserConfigurationException, ExecutionException, InterruptedException {
        printResponse(clientService.updateClient(updated_client));
    }

    private CompletableFuture<String> removeClient(){
        System.out.println("client id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String id = "0";
        try{
            String firstLine = bufferRead.readLine();
            if (!firstLine.equals("none")){
                id = firstLine;
            }
        } catch (IOException e){
            throw new SystemException("System error.");
        }

        return clientService.removeClient(id);
    }

    private void filterClients() {
        System.out.println("filtering by score:");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String score = "0";
        try {
            String firstLine = bufferRead.readLine();
            if (!firstLine.equals("none")) {
                score = firstLine;
            }
            printResponse(clientService.filterClients(score));
        }catch(IOException | SAXException | TransformerException | ParserConfigurationException | ExecutionException | InterruptedException e){
            throw new SystemException("System error.");
        }
    }


    public void reportClients() throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        printResponse(clientService.reportClients());
    }

    private void listClients() throws SAXException, ParserConfigurationException, IOException, TransformerException, ExecutionException, InterruptedException {
        printResponse(clientService.listClients());
    }

    private String readClient(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("client id:");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("client name:");
            String name = bufferRead.readLine();
            System.out.println("client surname:");
            String surname = bufferRead.readLine();

            return id + "|" + name + "|" + surname;
        } catch (IOException e){
            throw new SystemException("System error.");
        }
    }

    private String readClientUpdate(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("client internal id:");
            long internalID = Long.parseLong(bufferRead.readLine());
            System.out.println("client new name:");
            String name = bufferRead.readLine();
            System.out.println("client new surname:");
            String surname = bufferRead.readLine();

            return internalID + "|" + name + "|" + surname;
        } catch (IOException e){
            throw new SystemException("System error.");
        }
    }
}
