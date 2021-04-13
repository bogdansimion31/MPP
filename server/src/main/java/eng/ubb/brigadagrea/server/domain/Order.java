package eng.ubb.brigadagrea.server.domain;

import java.util.Random;

/**
 * The type Order.
 */
public class Order extends Entity<Long>{
    private Client client;
    private Product product;
    private int quantity;

    public Order(Client client, Product product, int quantity) {
        this.client = client;
        this.product = product;
        this.quantity = quantity;

        Random random = new Random();
        this.setId(random.nextLong());
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets client.
     *
     * @param client the client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Sets product.
     *
     * @param product the product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return super.toString() + client.toString() + " " + product.toString() + " " + quantity;
    }
}
