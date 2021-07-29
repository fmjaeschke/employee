package net.fmjaeschke.demos.employee.service;

import net.fmjaeschke.demos.employee.domain.Employee;
import net.fmjaeschke.demos.employee.domain.EmployeeNotFoundException;
import net.fmjaeschke.demos.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Inject
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee insertEmployee(Employee employee) throws EmployeeNotFoundException {
        Employee newEmployee = employeeRepository.insertEmployee(employee);
        if (newEmployee == null) {
            throw new EmployeeNotFoundException();
        }
        return newEmployee;
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.deleteEmployee(employee);
    }

    public Employee findEmployee(String employeeId) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return employee;
    }
}
