package eng.ubb.brigadagrea.server.domain.validators;

import eng.ubb.brigadagrea.server.domain.Product;
import eng.ubb.brigadagrea.server.domain.validators.exceptions.ValidatorException;
import eng.ubb.brigadagrea.server.domain.validators.IValidator;

/**
 * The type Product validator.
 */
public class ProductValidator implements IValidator<Product>{
    @Override
    public void validate(Product product) throws ValidatorException {
        if (product.getName().equals(""))
            throw new ValidatorException("Invalid name.");

        if (product.getCategory().equals(""))
            throw new ValidatorException("Invalid category.");

        if(product.getStock()< 0)
            throw new ValidatorException("Invalid stock value!");
    }
}
