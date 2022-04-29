package com.hyuk.coffeeserver.service;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.EXIST_NAME_EXP_MSG;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.exception.ServiceException;
import com.hyuk.coffeeserver.repository.CoffeeRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DefaultCoffeeService implements CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public DefaultCoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @Override
    @Transactional
    public Coffee createCoffee(String name, Category category, long price) {
        validateDuplicateName(name);

        var coffee = new Coffee(UUID.randomUUID(), name, category, price);
        return coffeeRepository.insertCoffee(coffee);
    }

    private void validateDuplicateName(String name) {
        coffeeRepository.findByName(name)
            .ifPresent(coffee -> {
                throw new ServiceException(EXIST_NAME_EXP_MSG);
            });
    }
}
