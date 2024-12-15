package wdmsystem.employee;

/// CreateEmployee is used as a DTO in both creation and updates, separated them as they have different fields
public record UpdateEmployeeDTO(Integer merchantId, String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
