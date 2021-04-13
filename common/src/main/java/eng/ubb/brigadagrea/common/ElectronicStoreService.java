package eng.ubb.brigadagrea.common;

import java.util.concurrent.Future;

public interface ElectronicStoreService {
    int PORT = 8888;
    String HOST = "localhost";
    String SAY_HELLO = "sayHello";
    String ADD_CLIENT = "addClient";
    String UPDATE_CLIENT = "updateClient";
    String REMOVE_CLIENT = "removeClient";
    String LIST_CLIENTS = "listClients";
    String FILTER_CLIENTS = "filterClients";
    String REPORT_CLIENTS = "reportClients";

    String ADD_PRODUCT = "addProduct";
    String UPDATE_PRODUCT = "updateProduct";
    String REMOVE_PRODUCT = "removeProduct";
    String LIST_PRODUCTS = "listProducts";
    String FILTER_PRODUCTS = "filterProducts";

    String ADD_ORDER = "addOrder";
    String UPDATE_ORDER = "updateOrder";
    String REMOVE_ORDER = "removeOrder";
    String LIST_ORDERS = "listOrders";
    String FILTER_ORDERS = "filterOrders";

    Future<String> sayHello(String name);
}
