package wdmsystem.employee;

public record EmployeeDTO(String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
