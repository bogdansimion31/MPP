package eng.ubb.brigadagrea.server.repository;

import eng.ubb.brigadagrea.server.domain.Entity;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

/**
 * The interface Repository.
 *
 * @param <ID>   the type parameter
 * @param <Type> the type parameter
 */
public interface IRepository<ID, Type extends Entity<ID>> {
    /**
     * Find one optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<Type> findById(ID id) throws ParserConfigurationException, SAXException, IOException, TransformerException;

    /**
     * Find all iterable.
     *
     * @return the iterable
     */
    Iterable<Type> findAll() throws IOException, SAXException, ParserConfigurationException, TransformerException;

    /**
     * Save optional.
     *
     * @param entity the entity
     * @return the optional
     * @throws ValidatorException the validator exception
     */
    Optional<Type> save(Type entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException;

    /**
     * Delete optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<Type> delete(ID id) throws IOException, SAXException, ParserConfigurationException, TransformerException;

    /**
     * Update optional.
     *
     * @param entity the entity
     * @return the optional
     * @throws ValidatorException the validator exception
     */
    Optional<Type> update(Type entity) throws ValidatorException, IOException, SAXException, ParserConfigurationException, TransformerException;
}
