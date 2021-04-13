package eng.ubb.brigadagrea.server.service;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.Order;
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
 * The type Order service.
 */
public class OrderService {
    private IRepository<Long, Client> clientRepository;
    private IRepository<Long, Product> productRepository;
    private IRepository<Long, Order> orderRepository;

    /**
     * Instantiates a new Order service.
     *
     * @param clientRepository  the client repository
     * @param productRepository the product repository
     * @param orderRepository   the order repository
     */
    public OrderService(IRepository<Long, Client> clientRepository, IRepository<Long, Product> productRepository, IRepository<Long, Order> orderRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Add order.
     *
     * @param clientId  the client id
     * @param productId the product id
     * @param quantity  the quantity
     * @throws StoreException the store exception
     */
    public void addOrder(Long orderId, Long clientId, Long productId, int quantity) throws StoreException, ParserConfigurationException, TransformerException, SAXException, IOException {
        if(!clientRepository.findById(clientId).isPresent()) {
            throw new StoreException("Invalid client id.");
        }
        if(!productRepository.findById(productId).isPresent()) {
            throw new StoreException("Invalid product id.");
        }
        Client client = clientRepository.findById(clientId).get();
        Product product = productRepository.findById(productId).get();
        if (product.getStock() < quantity)
            throw new StoreException("Not enough products on stock.");
        Order order = new Order(client, product, quantity);
        order.setId(orderId);
        orderRepository.save(order);
        order.getProduct().setStock(order.getProduct().getStock() - order.getQuantity());
        order.getClient().setFidelityScore(order.getClient().getFidelityScore() + order.getQuantity());
    }

    /**
     * Gets all orders.
     *
     * @return the all orders
     */
    public Set<Order> getAllOrders() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        Iterable<Order> orders = orderRepository.findAll();
        return StreamSupport.stream(orders.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Filter orders by category set.
     *
     * @param s the s
     * @return the set
     */
    public Set<Order> filterOrdersByClientName(String s) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        Iterable<Order> orders = orderRepository.findAll();

        Set<Order> filteredOrders= new HashSet<>();
        orders.forEach(filteredOrders::add);
        filteredOrders.removeIf(order -> !order.getClient().getName().contains(s));

        return filteredOrders;
    }

    /**
     * Remove order.
     *
     * @param id the id
     * @throws IllegalArgumentException the illegal argument exception
     * @throws StoreException           the store exception
     */
    public void removeOrder(Long id) throws IllegalArgumentException, StoreException, IOException, SAXException, TransformerException, ParserConfigurationException {
        Optional<Order> searchedOrder = orderRepository.findById(id);
        if (searchedOrder.isPresent()) {
            searchedOrder.get().getProduct().setStock(searchedOrder.get().getProduct().getStock() + searchedOrder.get().getQuantity());
            searchedOrder.get().getClient().setFidelityScore(searchedOrder.get().getClient().getFidelityScore() - searchedOrder.get().getQuantity());
            orderRepository.delete(id);
        }
        else
            throw new StoreException("Order does not exist.");
    }

    /**
     * Update order.
     *
     * @param id           the id
     * @param newQuantity  the new quantity
     * @throws ValidatorException the validator exception
     * @throws StoreException     the store exception
     */
    public void updateOrder(Long id, int newQuantity) throws ValidatorException, StoreException, IOException, SAXException, TransformerException, ParserConfigurationException {
        Optional<Order> searchedOrder = orderRepository.findById(id);
        if (searchedOrder.isPresent()) {
            Order newOrder = new Order(searchedOrder.get().getClient(), searchedOrder.get().getProduct(), newQuantity);
            newOrder.setId(searchedOrder.get().getId());
            if (newQuantity <= searchedOrder.get().getProduct().getStock() + searchedOrder.get().getQuantity()) {
                searchedOrder.get().getProduct().setStock(searchedOrder.get().getProduct().getStock() + searchedOrder.get().getQuantity());
                searchedOrder.get().getClient().setFidelityScore(searchedOrder.get().getClient().getFidelityScore() - searchedOrder.get().getQuantity());
                orderRepository.update(newOrder);
                newOrder.getProduct().setStock(newOrder.getProduct().getStock() - newOrder.getQuantity());
                newOrder.getClient().setFidelityScore(newOrder.getClient().getFidelityScore() + newOrder.getQuantity());
            } else
                throw new StoreException("Not enough stock for new quantity.");
        }
        else
            throw new StoreException("Order does not exist.");
    }
}
