package eng.ubb.brigadagrea.server.repository;


import eng.ubb.brigadagrea.server.domain.*;
import eng.ubb.brigadagrea.server.domain.validators.ClientValidator;
import eng.ubb.brigadagrea.server.service.ClientService;
import org.xml.sax.SAXException;
import eng.ubb.brigadagrea.server.service.ProductService;
import eng.ubb.brigadagrea.server.service.OrderService;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlClientRepository extends InMemoryRepository<Long, Client>{

    private final String URL = "jdbc:postgresql://localhost:5432/catalog";
    private final String USER = "postgres";
    private final String PASSWORD = "password";


    public SqlClientRepository(ClientValidator clientValidator) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException {
        super(clientValidator);
        readClients();
    }

    private void readClients() throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        List<Client> clients = this.getAllClients();
        for (Client client : clients) {
            super.save(client);
        }
    }

    public void addClient(Client client) throws Exception, SQLException {
        List<Client> allClients = getAllClients();
        for (Client client1 : allClients)
        {
            if(client1.getId().equals(client.getId())){
                throw new Exception("A client with id " + client.getId().toString() + " already exists!");
            }
        }

        String sql1 = "insert into client (id, name, surname, fidelityScore) values(?,?,?,?)";
        Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection1.prepareStatement(sql1);

        preparedStatement.setLong(1, client.getId());
        preparedStatement.setString(2, client.getName());
        preparedStatement.setString(3, client.getSurname());
        preparedStatement.setInt(4, client.getFidelityScore());
        preparedStatement.executeUpdate();

        super.save(client);
    }

    private void updateClient(Client client) throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        String sql = "update client set name=?, surname=?, fidelityScore=? where id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        preparedStatement.setString(1, client.getName());
        preparedStatement.setString(2, client.getSurname());
        preparedStatement.setInt(3, client.getFidelityScore());
        preparedStatement.setLong(4, client.getId());
        preparedStatement.executeUpdate();

        super.update(client);
    }

    private void deleteClientById(Long id) throws SQLException, SAXException, TransformerException, ParserConfigurationException, IOException {
        String sql = "delete from client where id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();

        super.delete(id);
    }

    private List<Client> getAllClients() throws SQLException {
        List<Client> result = new ArrayList<>();
        String sql = "select * from client";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            int fidelityScore = resultSet.getInt("fidelityScore");
            Client client = new Client(name, surname);
            client.setId(id);
            client.setFidelityScore(fidelityScore);
            result.add(client);
        }

        return result;
    }

    private List<Client> getClientsWithFidelityScoreGreater(int newFidelityScore) throws SQLException {
        List<Client> result = new ArrayList<>();
        String sql = "select * from client where fidelityScore >" + newFidelityScore;

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            int fidelityScore = resultSet.getInt("fidelityScore");
            Client client = new Client(name, surname);
            client.setId(id);
            client.setFidelityScore(fidelityScore);
            result.add(client);
        }
        return result;
    }

    private List<Client> getBestClient() throws SQLException {
        List<Client> result = new ArrayList<>();
        String sql = "select * from client where fidelityScore= (select max(fidelityScore) from Client)";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement =
                connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            int fidelityScore = resultSet.getInt("fidelityScore");
            Client client = new Client(name, surname);
            client.setId(id);
            client.setFidelityScore(fidelityScore);
            result.add(client);
        }
        return result;
    }
}
