package eng.ubb.brigadagrea.server.domain.validators;

import eng.ubb.brigadagrea.server.domain.Order;
import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import eng.ubb.brigadagrea.server.domain.validators.IValidator;

public class OrderValidator implements IValidator<Order> {
    @Override
    public void validate(Order order) throws ValidatorException {
        if (order.getClient() == null)
            throw new ValidatorException("Client can't be null!");

        if (order.getProduct() == null)
            throw new ValidatorException("Product can't be null!");
    }
}