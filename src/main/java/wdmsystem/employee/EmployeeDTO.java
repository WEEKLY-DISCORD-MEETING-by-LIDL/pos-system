package wdmsystem.employee;

// CreateEmployee is used as a DTO in both creation and updates, combined to one
public record EmployeeDTO(Integer id, Integer merchantId, String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
