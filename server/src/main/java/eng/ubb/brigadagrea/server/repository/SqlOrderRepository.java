package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.Order;
import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.ClientValidator;
import eng.ubb.brigadagrea.server.domain.validators.OrderValidator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlOrderRepository  extends InMemoryRepository<Long, Order>{

    private final String URL = "jdbc:postgresql://localhost:5432/catalog";
    private final String USER = "postgres";
    private final String PASSWORD = "password";

    private IRepository<Long, Client> clientRepo;
    private IRepository<Long, Product> productRepo;


    public SqlOrderRepository(OrderValidator orderValidator, IRepository<Long, Client> clientRepo, IRepository<Long, Product> productRepo) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException {
        super(orderValidator);
        this.clientRepo = clientRepo;
        this.productRepo = productRepo;

        readOrders();
    }

    private void readOrders() throws SQLException, TransformerException, ParserConfigurationException, SAXException, IOException {
        List<Order> orders = this.getAllOrders();
        for (Order order : orders) {
            super.save(order);
        }
    }

    public void addOrder(Order order) throws Exception, SQLException {
        boolean clientExists = false, productExists = false;
        List<Order> allOrders = getAllOrders();
        for (Order order1 : allOrders)
        {
            if(order1.getId().equals(order.getId())){
                throw new Exception("A order with id " + order.getId().toString() + " already exists!");
            }
        }
        List<Product> allProducts = (List<Product>) this.productRepo.findAll();
        for (Product product1 : allProducts)
        {
            if(product1.getId().equals(order.getProduct().getId())){
                productExists = true;
                break;
            }
        }
        if(!productExists){
            throw new Exception("There is no product with id: " + order.getProduct().getId().toString());
        }
        List<Client> allClients = (List<Client>) this.clientRepo.findAll();
        for (Client client1 : allClients)
        {
            if(client1.getId().equals(order.getClient().getId())){
                clientExists = true;
                break;
            }
        }
        if(!clientExists){
            throw new Exception("There is no client with id: " + order.getClient().getId().toString());
        }

        String sql1 = "insert into ordertable (order_id, client_id, product_id, quantity) values(?,?,?,?)";
        Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection1.prepareStatement(sql1);

        preparedStatement.setLong(1, order.getId());
        preparedStatement.setLong(2, order.getClient().getId());
        preparedStatement.setLong(3, order.getProduct().getId());
        preparedStatement.setDouble(4, order.getQuantity());
        preparedStatement.executeUpdate();

    }

    private void updateOrder(Order order) throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        String sql = "update ordertable set client_id=?, product_id=?, quantity=? where order_id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, order.getClient().getId());
        preparedStatement.setLong(2, order.getProduct().getId());
        preparedStatement.setInt(3, order.getQuantity());
        preparedStatement.setLong(4, order.getId());
        preparedStatement.executeUpdate();

        super.update(order);
    }

    private void deleteOrderById(Long orderId) throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        String sql = "delete from ordertable where order_id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        preparedStatement.setLong(1, orderId);
        preparedStatement.executeUpdate();

        super.delete(orderId);
    }

    private List<Order> getAllOrders() throws SQLException, ParserConfigurationException, IOException, SAXException, TransformerException {
        List<Order> result = new ArrayList<>();
        String sql = "select * from ordertable";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Long orderId = resultSet.getLong("order_id");
            Long clientId = resultSet.getLong("client_id");
            Long productId = resultSet.getLong("product_id");
            int quantity = resultSet.getInt("quantity");

            Optional<Client> client = this.clientRepo.findById(clientId);
            if (client.isEmpty()) {
                client = Optional.of(new Client("Client", "Deleted"));
            }

            Optional<Product> product = this.productRepo.findById(productId);
            if (product.isEmpty()){
                product = Optional.of(new Product("Product", "Deleted", 0));
            }

            Order order = new Order(client.get(), product.get(), quantity);
            order.setId(orderId);
            result.add(order);
        }
        return result;
    }
}
