package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.ProductValidator;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.SystemException;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class InFileProductRepository extends InMemoryRepository<Long, Product>{
    private String fileName;

    public InFileProductRepository(ProductValidator validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        readFile();
    }
    private void readFile() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String name = items.get(1);
                String category = items.get((2));
                int stock = Integer.parseInt(items.get(3));
                Product product = new Product(name, category, stock);
                product.setId(id);

                try {
                    super.save(product);
                } catch (ValidatorException e) {
                    throw new ValidatorException(e.getMessage());
                }
                catch (IOException | SAXException | ParserConfigurationException | TransformerException e) {
                    throw new SystemException(e.getMessage());
                    }
            });
        } catch (IOException ex) {
            throw new SystemException("System error.");
        }
    }

    private void writeFile(Product entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName() + "," + entity.getCategory() + "," + entity.getStock());
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
    public Optional<Product> findById(Long id) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        return super.findById(id);
    }

    @Override
    public Iterable<Product> findAll() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        return super.findAll();
    }

    @Override
    public Optional<Product> save(Product entity) throws ValidatorException, SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        Optional<Product> returnValue = super.save(entity);
        writeFile(entity);
        return returnValue;
    }

    @Override
    public Optional<Product> delete(Long id) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        readFile();
        Optional<Product> returnValue = super.delete(id);
        this.deleteFileContent();
        for (Product product: super.findAll()) {
            writeFile(product);
        }
        return returnValue;
    }

    @Override
    public Optional<Product> update(Product entity) throws ValidatorException, IOException, SAXException, TransformerException, ParserConfigurationException {
        readFile();
        Optional<Product> returnValue = super.update(entity);
        this.deleteFileContent();
        for (Product product: super.findAll()) {
            writeFile(product);
        }
        return returnValue;
    }
}
