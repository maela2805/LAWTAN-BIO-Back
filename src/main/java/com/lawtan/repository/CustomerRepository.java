package com.lawtan.repository;

import com.lawtan.entity.Customer;
import com.lawtan.model.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCustomerType(CustomerType customerType);
    List<Customer> findAllByOrderByTotalSpentFcfaDesc();
}
