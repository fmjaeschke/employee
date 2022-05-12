package net.fmjaeschke.demos.employee.repository;

import net.fmjaeschke.demos.employee.domain.Employee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeRepository {

    private static final String EMPLOYEE_ID = "employee_id";
    private static final String EMPLOYEE_NAME = "employee_name";
    private static final String EMPLOYEE_EMAIL = "employee_email";
    private static final String EMPLOYEE_ADDRESS = "employee_address";
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

        String employeeId = UUID.randomUUID().toString();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue(EMPLOYEE_ID, employeeId)
                .addValue(EMPLOYEE_NAME, employee.getEmployeeName())
                .addValue(EMPLOYEE_EMAIL, employee.getEmployeeEmail())
                .addValue(EMPLOYEE_ADDRESS, employee.getEmployeeAddress());
        template.update(sql,param);

        return findById(employeeId);
    }

    public void updateEmployee(Employee employee) {
        final String sql = "update employee set employee_name=:employee_name, employee_address=:employee_address, employee_email=:employee_email where employee_id=:employee_id";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue(EMPLOYEE_ID, employee.getEmployeeId())
            .addValue(EMPLOYEE_NAME, employee.getEmployeeName())
            .addValue(EMPLOYEE_EMAIL, employee.getEmployeeEmail())
            .addValue(EMPLOYEE_ADDRESS, employee.getEmployeeAddress());
        template.execute(sql, param, (PreparedStatementCallback<Object>) PreparedStatement::executeUpdate);
    }

    public void deleteEmployee(Employee employee) {
        final String sql = "delete from employee where employee_id=:employee_id";

        SqlParameterSource param = new MapSqlParameterSource()
            .addValue(EMPLOYEE_ID, employee.getEmployeeId());

        template.execute(sql, param, (PreparedStatementCallback<Object>) PreparedStatement::executeUpdate);
    }

    public Employee findById(String employeeId) {
        final String sql = "select employee_id, employee_name, employee_address, employee_email from employee where employee_id=:employee_id";

        Employee employee;
        try {
            employee = template.queryForObject(
                    sql,
                    new MapSqlParameterSource(EMPLOYEE_ID, employeeId),
                    new EmployeeRowMapper());
        } catch (EmptyResultDataAccessException e) {
            employee = null;
        }
        return employee;
    }
}
