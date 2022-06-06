package hu.webuni.hr.akostomschweger.service;

import hu.webuni.hr.akostomschweger.dto.EmployeeDto;
import hu.webuni.hr.akostomschweger.model.Employee;
import hu.webuni.hr.akostomschweger.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

// itt ne legyen annotáció (teacher's hint) -- @Service
public abstract class EmployeeSuperClass implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;


    /*

        private Map<Long, Employee> employees = new HashMap<>();

        {
            employees.put(1L, new Employee(
                    1L,
                    "Akos",
                    "junior java developer",
                    100000,
                    LocalDateTime.of(2011, 1, 11, 11, 11))

            );
            employees.put(2L, new Employee(
                    2L,
                    "Bkos",
                    "senior java developer",
                    200000,
                    LocalDateTime.of(2012, 2, 22, 22, 22))

            );
        }


         */

    @Transactional
    public Employee save(Employee employee) {
        // employees.put(employee.getId(), employee);
        return employeeRepository.save(employee);// employee;
    }

    public List<Employee> findAll() {
        //return new ArrayList<>(employees.values());
    return employeeRepository.findAll();
    }


    public Optional<Employee> findById(long id) {
        //return employees.get(id);
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee update(long id, Employee employee) {
        // employees.put(id, employee);
        // return employee;
    if(employeeRepository.existsById(employee.getId()))
        return employeeRepository.save(employee);
    else throw new NoSuchElementException();
    }

    @Transactional
    public void delete(long id) {
        // employees.remove(id);
    employeeRepository.deleteById(id);
    }




}
