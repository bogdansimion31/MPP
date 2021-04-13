package eng.ubb.brigadagrea.server.domain.validators;

import eng.ubb.brigadagrea.server.domain.Client;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import eng.ubb.brigadagrea.server.domain.validators.IValidator;

/**
 * The type Client validator.
 */
public class ClientValidator implements IValidator<Client> {
    @Override
    public void validate(Client client) throws ValidatorException {
        if (client.getFidelityScore() < 0 || client.getFidelityScore() > 100)
            throw new ValidatorException("Invalid fidelity score!");

        if (client.getName().equals(""))
            throw new ValidatorException("Invalid name.");

        if (client.getSurname().equals(""))
            throw new ValidatorException("Invalid surname.");
    }
}
