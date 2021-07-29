package net.fmjaeschke.demos.employee.repository;

import net.fmjaeschke.demos.employee.domain.Employee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class EmployeeRepository {

    private final NamedParameterJdbcTemplate template;

    @Inject
    public EmployeeRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Employee> findAll() {
        return template.query("select employee_id, employee_name, employee_address, employee_email from employee", new EmployeeRowMapper());
    }

    public Employee insertEmployee(Employee employee) {
        final String sql = "insert into employee(employee_id, employee_name, employee_address, employee_email) values(:employee_id, :employee_name, :employee_address, :employee_email)";

        KeyHolder holder = new GeneratedKeyHolder();

        String employeeId = UUID.randomUUID().toString();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("employee_id", employeeId)
                .addValue("employee_name", employee.getEmployeeName())
                .addValue("employee_email", employee.getEmployeeEmail())
                .addValue("employee_address", employee.getEmployeeAddress());
        template.update(sql,param, holder);

        return findById(employeeId);
    }

    public void updateEmployee(Employee employee) {
        final String sql = "update employee set employee_name=:employee_name, employee_address=:employee_address, employee_email=:employee_email where employee_id=:employee_id";

        Map<String,Object> map= new HashMap<>();
        map.put("employee_id", employee.getEmployeeId());
        map.put("employee_name", employee.getEmployeeName());
        map.put("employee_email", employee.getEmployeeEmail());
        map.put("employee_address", employee.getEmployeeAddress());

        template.execute(sql,map, (PreparedStatementCallback<Object>) PreparedStatement::executeUpdate);
    }

    public void deleteEmployee(Employee employee) {
        final String sql = "delete from employee where employee_id=:employee_id";

        Map<String,Object> map= new HashMap<>();
        map.put("employee_id", employee.getEmployeeId());

        template.execute(sql,map, (PreparedStatementCallback<Object>) PreparedStatement::executeUpdate);
    }

    public Employee findById(String employeeId) {
        final String sql = "select employee_id, employee_name, employee_address, employee_email from employee where employee_id=:employee_id";

        Employee employee;
        try {
            employee = template.queryForObject(
                    sql,
                    new MapSqlParameterSource("employee_id", employeeId),
                    new EmployeeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            employee = null;
        }
        return employee;
    }
}
