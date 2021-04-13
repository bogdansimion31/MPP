package eng.ubb.brigadagrea.server.repository;


import eng.ubb.brigadagrea.server.domain.Entity;
import eng.ubb.brigadagrea.server.domain.validators.IValidator;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * The type In memory repository.
 *
 * @param <ID>   the type parameter
 * @param <Type> the type parameter
 */
public class InMemoryRepository<ID, Type extends Entity<ID>> implements IRepository<ID, Type> {

    private Map<ID, Type> entities;
    private IValidator<Type> validator;

    /**
     * Instantiates a new In memory repository.
     */
    public InMemoryRepository() {}

    /**
     * Instantiates a new In memory repository.
     *
     * @param validator the validator
     */
    public InMemoryRepository(IValidator<Type> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<Type> findById(ID id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<Type> findAll() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        return new HashSet<>(entities.values());
    }

    @Override
    public Optional<Type> save(Type entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        if (entity == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<Type> delete(ID id) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<Type> update(Type entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }
}

