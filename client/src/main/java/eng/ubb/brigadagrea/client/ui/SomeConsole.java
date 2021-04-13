package eng.ubb.brigadagrea.client.ui;

import eng.ubb.brigadagrea.common.ElectronicStoreService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SomeConsole {
    private ElectronicStoreService electronicStoreService;

    public SomeConsole(ElectronicStoreService electronicStoreService) {
        this.electronicStoreService = electronicStoreService;
    }

    public void runConsole() {
        //todo: implement a menu or cmd based ui

        String name = "Ana";
        Future<String> resultFuture = electronicStoreService.sayHello(name); //non-blocking

        /*
        .....
         */

        try {
            String result = resultFuture.get(); //blocking :(((
            System.out.println("***************");
            System.out.println(result);
            System.out.println("***************");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
