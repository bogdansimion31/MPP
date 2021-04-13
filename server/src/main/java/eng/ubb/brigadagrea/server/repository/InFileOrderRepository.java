package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.Order;
import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.OrderValidator;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.SystemException;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InFileOrderRepository extends InMemoryRepository<Long, Order>{
    private String fileName;
    private IRepository<Long, Client> clientRepo;
    private IRepository<Long, Product> productRepo;

    public InFileOrderRepository(OrderValidator validator, String fileName, IRepository<Long, Client> clientRepo, IRepository<Long, Product> productRepo) {
        super(validator);
        this.fileName = fileName;
        this.clientRepo = clientRepo;
        this.productRepo = productRepo;
        readFile();
    }
    private void readFile() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                Long clientId = Long.valueOf(items.get(1));
                Long productId = Long.valueOf(items.get(2));
                int quantity = Integer.parseInt(items.get(3));
                Optional<Client> OptionalClient;
                Client client = null;
                try {
                    OptionalClient = clientRepo.findById(clientId);
                    if (OptionalClient.isPresent()){
                        client = OptionalClient.get();
                    }
                } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                    throw new SystemException("System error.");
                }
                Optional<Product> OptionalProduct;
                Product product = null;
                try {
                    OptionalProduct = productRepo.findById(productId);
                    if(OptionalProduct.isPresent()){
                        product = OptionalProduct.get();
                    }
                } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
                    throw new SystemException("System error.");
                }
                Order order = new Order(client, product, quantity);
                order.setId(id);

                try {
                    super.save(order);
                } catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e) {
                    throw new SystemException("System error.");
                }
            });
        } catch (IOException ex) {
            throw new SystemException("System error.");
        }
    }

    private void writeFile(Order entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getClient().getId() + "," + entity.getProduct().getId() + "," + entity.getQuantity());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new SystemException("System error.");
        }
    }

    private void deleteFileContent() throws FileNotFoundException {
        Path path = Paths.get(fileName);

        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        // other operations
        writer.close();
    }

    @Override
    public Optional<Order> findById(Long id) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        readFile();
        return super.findById(id);
    }

    @Override
    public Iterable<Order> findAll() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        return super.findAll();
    }

    @Override
    public Optional<Order> save(Order entity) throws ValidatorException, SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        Optional<Order> returnValue = super.save(entity);
        writeFile(entity);
        return returnValue;
    }

    @Override
    public Optional<Order> delete(Long id) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        readFile();
        Optional<Order> returnValue = super.delete(id);
        this.deleteFileContent();
        for (Order order: super.findAll()) {
            writeFile(order);
        }
        return returnValue;
    }

    @Override
    public Optional<Order> update(Order entity) throws ValidatorException, IOException, SAXException, TransformerException, ParserConfigurationException {
        readFile();
        Optional<Order> returnValue = super.update(entity);
        this.deleteFileContent();
        for (Order order: super.findAll()) {
            writeFile(order);
        }
        return returnValue;
    }
}
