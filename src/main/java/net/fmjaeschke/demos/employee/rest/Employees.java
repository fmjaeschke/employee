package net.fmjaeschke.demos.employee.rest;

import net.fmjaeschke.demos.employee.domain.Employee;
import net.fmjaeschke.demos.employee.domain.EmployeeNotFoundException;
import net.fmjaeschke.demos.employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Service
@Path("/employees")
@Produces("application/json")
public class Employees {

    @Resource
    private EmployeeService employeeService;

    @GET
    public List<Employee> getEmployees() {
        return employeeService.findAll();
    }

    @GET
    @Path("/{employeeId}")
    public Employee getEmployee(@PathParam("employeeId") String employeeId) throws EmployeeNotFoundException {
        return employeeService.findEmployee(employeeId);
    }

    @POST
    public Response createEmployee(Employee employee, @Context UriInfo uriInfo) throws EmployeeNotFoundException {
        Employee newEmployee = employeeService.insertEmployee(employee);
        URI uri = uriInfo.getAbsolutePathBuilder().path(newEmployee.getEmployeeId()).build();
        return Response.created(uri).entity(employee).build();
    }

    @PUT
    public void updateEmployee(Employee employee) {
        employeeService.updateEmployee(employee);
    }

    @DELETE
    public void deleteEmployee(Employee employee) {
        employeeService.deleteEmployee(employee);
    }
}
