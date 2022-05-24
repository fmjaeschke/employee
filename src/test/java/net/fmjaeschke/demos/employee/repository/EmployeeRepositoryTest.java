package net.fmjaeschke.demos.employee.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import net.fmjaeschke.demos.employee.domain.Employee;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({DBUnitExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@DataSet("employees.yml")
@ContextConfiguration(initializers = EmployeeRepositoryTest.DockerPostgreDataSourceInitializer.class)
@Testcontainers(disabledWithoutDocker = true)
class EmployeeRepositoryTest {

    @Container
    private final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3");

    @Autowired
    private EmployeeRepository repository;

    @Test
    @DataSet(cleanBefore = true)
    void shouldFindAllIfEmpty() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void shouldFindAll() {
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void shouldFindById() {
        Employee employee = repository.findById("1");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(employee).isNotNull();
            softly.assertThat(employee.getEmployeeId()).isEqualTo("1");
            softly.assertThat(employee.getEmployeeName()).isEqualTo("jack");
            softly.assertThat(employee.getEmployeeEmail()).isEqualTo("jack@gmail.com");
            softly.assertThat(employee.getEmployeeAddress()).isNull();
        });
    }

    @Test
    void shouldNotFindById() {
        Employee employee = repository.findById("3");
        assertThat(employee).isNull();
    }

    @Test
    void shouldDelete() {
        Employee employeeToDelete = new Employee();
        employeeToDelete.setEmployeeId("1");
        repository.deleteEmployee(employeeToDelete);

        Employee employee = repository.findById("1");
        assertThat(employee).isNull();
    }

    @Test
    void shouldInsert() {
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeName("Jon");
        newEmployee.setEmployeeEmail("jon@gmail.com");

        Employee employee = repository.insertEmployee(newEmployee);

        assertThat(employee).usingRecursiveComparison()
                .ignoringFields("employeeId")
                .isEqualTo(newEmployee);
    }

    @Test
    void shouldUpdate() {
        Employee oldEmployee = repository.findById("1");

        assertThat(oldEmployee.getEmployeeName()).isEqualTo("jack");
        assertThat(oldEmployee.getEmployeeEmail()).isEqualTo("jack@gmail.com");

        oldEmployee.setEmployeeName("jon");
        oldEmployee.setEmployeeEmail("jon@gmail.com");

        repository.updateEmployee(oldEmployee);

        Employee employee = repository.findById("1");
        assertThat(employee.getEmployeeName()).isEqualTo("jon");
        assertThat(employee.getEmployeeEmail()).isEqualTo("jon@gmail.com");
    }

    public static class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
            );
        }
    }
}

