package eng.ubb.brigadagrea.server.service;

import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.StoreException;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import eng.ubb.brigadagrea.server.repository.IRepository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Product service.
 */
public class ProductService {
    private IRepository<Long, Product> repository;

    /**
     * Instantiates a new Product service.
     *
     * @param repository the repository
     */
    public ProductService(IRepository<Long, Product> repository) {
        this.repository = repository;
    }

    /**
     * Add product.
     *
     * @param product the product
     * @throws ValidatorException the validator exception
     */
    public void addProduct(Product product) throws ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException {
        Optional<Product> searchedProduct = repository.findById(product.getId());
        if (searchedProduct.isPresent()) {
            if(searchedProduct.get().getName().equals(product.getName()) && searchedProduct.get().getCategory().equals(product.getCategory())){
                searchedProduct.get().setStock(searchedProduct.get().getStock() + product.getStock());
            }
            else {
                throw new ValidatorException("Id already in use.");
            }
        }
        else
            repository.save(product);
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public Set<Product> getAllProducts() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        Iterable<Product> products = repository.findAll();
        return StreamSupport.stream(products.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Filter products by category set.
     *
     * @param s the s
     * @return the set
     */
    public Set<Product> filterProductsByCategory(String s) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        Iterable<Product> products = repository.findAll();

        Set<Product> filteredProducts= new HashSet<>();
        products.forEach(filteredProducts::add);
        filteredProducts.removeIf(product -> !product.getCategory().contains(s));

        return filteredProducts;
    }

    /**
     * Remove product.
     *
     * @param id the id
     * @throws IllegalArgumentException the illegal argument exception
     * @throws StoreException           the store exception
     */
    public void removeProduct(Long id) throws IllegalArgumentException, StoreException, IOException, ParserConfigurationException, TransformerException, SAXException {
        Optional<Product> searchedProduct = repository.findById(id);
        if (searchedProduct.isPresent()) {
            repository.delete(id);
        }
        else
            throw new StoreException("Product does not exist.");
    }

    /**
     * Update product.
     *
     * @param id         the id
     * @param newProduct the new product
     * @throws ValidatorException the validator exception
     * @throws StoreException     the store exception
     */
    public void updateProduct(Long id, Product newProduct) throws ValidatorException, StoreException, IOException, ParserConfigurationException, TransformerException, SAXException {
        Optional<Product> searchedProduct = repository.findById(id);
        if (searchedProduct.isPresent()) {
            repository.update(newProduct);
        }
        else
            throw new StoreException("Product does not exist.");
    }
    public Optional<Product> findProductById(Long id) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Optional<Product> searchedProduct = repository.findById(id);
        if (searchedProduct.isPresent()) {
            return searchedProduct;
        }
        else
            throw new StoreException("Product does not exist.");
    }
}
