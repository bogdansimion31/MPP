package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.ProductValidator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlProductRepository  extends InMemoryRepository<Long, Product> {

    private final String URL = "jdbc:postgresql://localhost:5432/catalog";
    private final String USER = "postgres";
    private final String PASSWORD = "password";


    public SqlProductRepository(ProductValidator productValidator) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException {
        super(productValidator);
        readProducts();
    }

    private void readProducts() throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        List<Product> products = this.getAllProducts();
        for (Product product : products) {
            super.save(product);
        }
    }

    public void addProduct(Product product) throws Exception, SQLException {
        List<Product> allProducts = getAllProducts();
        for (Product product1 : allProducts)
        {
            if(product1.getId().equals(product.getId())){
                throw new Exception("A product with id " + product.getId().toString() + " already exists!");
            }
        }

        String sql1 = "insert into product (id, name, category, stock) values(?,?,?,?)";
        Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection1.prepareStatement(sql1);

        preparedStatement.setLong(1, product.getId());
        preparedStatement.setString(2, product.getName());
        preparedStatement.setString(3, product.getCategory());
        preparedStatement.setInt(4,product.getStock());
        preparedStatement.executeUpdate();

        super.save(product);
    }

    private void updateProduct(Product product) throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        String sql = "update product set name=?, category=?, stock=? where id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        preparedStatement.setString(1, product.getName());
        preparedStatement.setString(2, product.getCategory());
        preparedStatement.setInt(3, product.getStock());
        preparedStatement.setLong(4, product.getId());
        preparedStatement.executeUpdate();

        super.update(product);
    }

    private void deleteProductById(Long id) throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        String sql = "delete from product where id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();

        super.delete(id);
    }

    private List<Product> getAllProducts() throws SQLException {
        List<Product> result = new ArrayList<>();
        String sql = "select * from product";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String category = resultSet.getString("category");
            int stock = resultSet.getInt("stock");
            Product product = new Product(name,category,stock);
            product.setId(id);
            result.add(product);
        }

        return result;
    }
}
