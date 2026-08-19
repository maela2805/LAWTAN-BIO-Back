package com.lawtan.service.impl;

import com.lawtan.dto.CustomerDTO;
import com.lawtan.entity.Customer;
import com.lawtan.model.CustomerType;
import com.lawtan.repository.CustomerRepository;
import com.lawtan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAllByOrderByTotalSpentFcfaDesc()
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> getCustomersByType(CustomerType customerType) {
        return customerRepository.findByCustomerType(customerType)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'ID: " + id));
        return convertToDTO(customer);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setCompanyName(dto.getCompanyName());
        customer.setCustomerType(dto.getCustomerType() != null ? dto.getCustomerType() : CustomerType.INDIVIDUAL);
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity() != null ? dto.getCity() : "Dakar");
        customer.setNineaNumber(dto.getNineaNumber());
        customer.setNotes(dto.getNotes());
        customer.setTotalOrdersCount(0);
        customer.setTotalSpentFcfa(0.0);
        customer.setBalanceDueFcfa(0.0);
        customer.setCreatedAt(LocalDateTime.now());

        Customer saved = customerRepository.save(customer);
        return convertToDTO(saved);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable avec l'ID: " + id));

        customer.setName(dto.getName());
        customer.setCompanyName(dto.getCompanyName());
        if (dto.getCustomerType() != null) customer.setCustomerType(dto.getCustomerType());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setNineaNumber(dto.getNineaNumber());
        customer.setNotes(dto.getNotes());

        Customer saved = customerRepository.save(customer);
        return convertToDTO(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCustomerType(entity.getCustomerType());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setNineaNumber(entity.getNineaNumber());
        dto.setTotalOrdersCount(entity.getTotalOrdersCount());
        dto.setTotalSpentFcfa(entity.getTotalSpentFcfa());
        dto.setBalanceDueFcfa(entity.getBalanceDueFcfa());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
