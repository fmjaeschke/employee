package net.fmjaeschke.demos.employee.domain;

public class Employee {


    private String employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeeAddress;

    public void setEmployeeId(String employeeId) {

        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setEmployeeEmail(String employeeEmail) {

        this.employeeEmail = employeeEmail;
    }

    public void setEmployeeAddress(String employeeAddress) {

        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }
}


