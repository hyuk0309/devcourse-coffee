package com.hyuk.coffeeserver.service;

import static com.hyuk.coffeeserver.exception.ExceptionMessage.EXIST_NAME_EXP_MSG;
import static com.hyuk.coffeeserver.exception.ExceptionMessage.INVALID_COFFEE_ID_EXP_MSG;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;
import com.hyuk.coffeeserver.exception.ServiceException;
import com.hyuk.coffeeserver.repository.CoffeeRepository;
import java.util.List;
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

    @Override
    @Transactional
    public void removeCoffee(UUID id) {
        validateValidId(id);

        coffeeRepository.deleteById(id);
    }

    @Override
    public List<Coffee> findAllCoffees() {
        return coffeeRepository.findAll();
    }

    @Override
    public Coffee findCoffee(UUID id) {
        return coffeeRepository.findById(id)
            .orElseThrow(() -> {
                throw new ServiceException(INVALID_COFFEE_ID_EXP_MSG.toString());
            });
    }

    @Override
    @Transactional
    public Coffee updateName(UUID id, String name) {
        var coffee = coffeeRepository.findById(id)
            .orElseThrow(() -> {
                throw new ServiceException(INVALID_COFFEE_ID_EXP_MSG.toString());
            });

        validateDuplicateName(name);

        coffee.changeName(name);
        return coffeeRepository.updateCoffee(coffee);
    }

    private void validateValidId(UUID id) {
        coffeeRepository.findById(id)
            .orElseThrow(() -> {
                throw new ServiceException(INVALID_COFFEE_ID_EXP_MSG.toString());
            });
    }

    private void validateDuplicateName(String name) {
        coffeeRepository.findByName(name)
            .ifPresent(coffee -> {
                throw new ServiceException(EXIST_NAME_EXP_MSG.toString());
            });
    }
}
