package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.validators.ClientValidator;
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

public class InFileClientRepository extends InMemoryRepository<Long, Client>{
    private String fileName;

    public InFileClientRepository(ClientValidator validator, String fileName) {
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
                String surname = items.get(2);
                int fidelityScore = Integer.parseInt(items.get(3));
                Client client = new Client(name, surname);
                client.setId(id);
                client.setFidelityScore(fidelityScore);

                try {
                    super.save(client);
                } catch (ValidatorException | IOException | SAXException | ParserConfigurationException | TransformerException e) {
                    throw new SystemException("System error.");
                }
            });
        } catch (IOException ex) {
            throw new SystemException("System error.");
        }
    }

    private void writeFile(Client entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName() + "," + entity.getSurname() + "," + entity.getFidelityScore());
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
    public Optional<Client> findById(Long id) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        readFile();
        return super.findById(id);
    }

    @Override
    public Iterable<Client> findAll() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        return super.findAll();
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException, SAXException, TransformerException, ParserConfigurationException, IOException {
        readFile();
        Optional<Client> returnValue = super.save(entity);
        writeFile(entity);
        return returnValue;
    }

    @Override
    public Optional<Client> delete(Long id) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        readFile();
        Optional<Client> returnValue = super.delete(id);
        this.deleteFileContent();
        for (Client client: super.findAll()) {
            writeFile(client);
        }
        return returnValue;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException, IOException, SAXException, TransformerException, ParserConfigurationException {
        readFile();
        Optional<Client> returnValue = super.update(entity);
        this.deleteFileContent();
        for (Client client: super.findAll()) {
            writeFile(client);
        }
        return returnValue;
    }
}
