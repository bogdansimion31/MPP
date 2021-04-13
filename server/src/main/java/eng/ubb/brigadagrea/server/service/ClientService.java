package eng.ubb.brigadagrea.server.service;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.StoreException;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import eng.ubb.brigadagrea.server.repository.IRepository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Client service.
 */
public class ClientService {
    private IRepository<Long, Client> repository;

    /**
     * Instantiates a new Client service.
     *
     * @param repository the repository
     */
    public ClientService(IRepository<Long, Client> repository) {
        this.repository = repository;
    }

    public Set<Client> filterClientsByFidelityScore(int score) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        Iterable<Client> clients = repository.findAll();

        Set<Client> filteredClients= new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(client -> !(client.getFidelityScore() >= score));

        return filteredClients;
    }

    /**
     * Add client.
     *
     * @param client the client
     * @throws ValidatorException the validator exception
     */
    public void addClient(Client client) throws ValidatorException, SAXException, TransformerException, ParserConfigurationException, IOException {
        if(!this.repository.findById(client.getId()).isEmpty())
            throw new StoreException("Id already used.");
        repository.save(client);
    }

    /**
     * Gets all clients.
     *
     * @return the all clients
     */
    public Set<Client> getAllClients() throws SAXException, TransformerException, ParserConfigurationException, IOException {
        Iterable<Client> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Remove client.
     *
     * @param id the id
     * @throws IllegalArgumentException the illegal argument exception
     * @throws StoreException           the store exception
     */
    public void removeClient(Long id) throws IllegalArgumentException, StoreException, IOException, ParserConfigurationException, TransformerException, SAXException {
        Optional<Client> searchedClient = repository.findById(id);
        if (searchedClient.isPresent()) {
            repository.delete(id);
        }
        else
            throw new StoreException("Client does not exist.");
    }

    /**
     * Update client.
     *
     * @param id         the id
     * @param newClient the new client
     * @throws ValidatorException the validator exception
     * @throws StoreException     the store exception
     */
    public void updateClient(Long id, Client newClient) throws ValidatorException, StoreException, IOException, SAXException, TransformerException, ParserConfigurationException {
        Optional<Client> searchedClient = repository.findById(id);
        if (searchedClient.isPresent()) {
            repository.update(newClient);
        }
        else
            throw new StoreException("Client does not exist.");
    }
    public Optional<Client> findClientById(Long id) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Optional<Client> searchedClient = repository.findById(id);
        if (searchedClient.isPresent()) {
            return searchedClient;
        }
        else
            throw new StoreException("Client does not exist.");
    }
}
