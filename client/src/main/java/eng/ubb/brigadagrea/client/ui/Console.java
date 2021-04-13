package eng.ubb.brigadagrea.client.ui;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Scanner;

/**
 * The type Console.
 */
public class Console {

    private ProductConsole productConsole;
    private ClientConsole clientConsole;
    private OrderConsole orderConsole;


    public Console(ClientConsole clientConsole, OrderConsole orderConsole, ProductConsole productConsole) {
        this.clientConsole = clientConsole;
        this.productConsole = productConsole;
        this.orderConsole = orderConsole;
    }

    /**
     * Run console.
     */
    public void runConsole() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        main_loop:
        while (true) {
            try {
                displayInitialMenu();

                int choice = 0;
                Scanner scanner = new Scanner(System.in);
                try {
                    choice = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                switch (choice) {
                    case 1:
                        productConsole.runConsole();
                        break;
                    case 2:
                        clientConsole.runConsole();
                        break;
                    case 3:
                        orderConsole.runConsole();
                        break;
                    case 4:
                        clientConsole.reportClients();
                        break;
                    case 5:
                        break main_loop;
                    default:
                        System.out.println("Please input a number between 1 and 5!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                this.runConsole();
            }
        }
    }

    private void displayInitialMenu() {
        System.out.println("|   MAIN MENU              |");
        System.out.println("|   Options:               |");
        System.out.println("|   1. Manage products     |");
        System.out.println("|   2. Manage clients      |");
        System.out.println("|   3. Manage orders       |");
        System.out.println("|   4. Report clients      |");
        System.out.println("|   5. Exit                |");
    }
}