package eng.ubb.brigadagrea.server.domain.validators;

import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;

/**
 * The interface Validator.
 *
 * @param <Type> the type parameter
 */
public interface IValidator<Type> {
    /**
     * Validate.
     *
     * @param entity the entity
     * @throws ValidatorException the validator exception
     */
    void validate(Type entity) throws ValidatorException;
}
