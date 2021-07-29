package net.fmjaeschke.demos.employee.repository;

import net.fmjaeschke.demos.employee.domain.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getString("employee_id"));
        employee.setEmployeeName(rs.getString("employee_name"));
        employee.setEmployeeEmail(rs.getString("employee_email"));
        employee.setEmployeeAddress(rs.getString("employee_address"));

        return employee;
    }
}
