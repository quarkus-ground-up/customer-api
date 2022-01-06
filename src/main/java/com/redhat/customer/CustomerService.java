package com.redhat.customer;

import com.redhat.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<Customer> findAll() {
        return this.customerMapper.toDomainList(customerRepository.findAll().list());
    }

    public Optional<Customer> findById(@NonNull Integer customerId) {
        return customerRepository.findByIdOptional(customerId)
                .map(customerMapper::toDomain);
    }

    @Transactional
    public void save(@Valid Customer customer) {
        log.debug("Saving Customer: {}", customer);
        CustomerEntity entity = customerMapper.toEntity(customer);
        customerRepository.persist(entity);
        customerMapper.updateDomainFromEntity(entity, customer);
    }

    @Transactional
    public void update(@Valid Customer customer) {
        log.debug("Updating Customer: {}", customer);
        if (Objects.isNull(customer.getCustomerId())) {
            throw new ServiceException("Customer does not have a customerId");
        }
        CustomerEntity entity = customerRepository.findByIdOptional(customer.getCustomerId())
                .orElseThrow(() -> new ServiceException("No Customer found for customerId[%s]", customer.getCustomerId()));
        customerMapper.updateEntityFromDomain(customer, entity);
        customerRepository.persist(entity);
        customerMapper.updateDomainFromEntity(entity, customer);
    }

}