package eng.ubb.brigadagrea.server.domain;

import java.util.Random;

/**
 * The type Product.
 */
public class Product extends Entity<Long>{
    private String name;
    private String category;
    private int stock;

    /**
     * Instantiates a new Product.
     */
    public Product(){}

    public Product(String name, String category, int stock){
        this.name = name;
        this.category = category;
        this.stock = stock;
    }

    /**
     * Gets serial number.
     *
     * @return the serial number
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets stock.
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets stock.
     *
     * @param stock the stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        if (!this.getId().equals(product.getId())) return false;

        return this.name.equals(product.name);
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + " " + category + " " + stock;
    }
}
