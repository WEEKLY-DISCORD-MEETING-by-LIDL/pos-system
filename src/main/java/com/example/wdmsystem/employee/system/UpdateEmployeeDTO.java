package com.example.wdmsystem.employee.system;

/// CreateEmployee is used as a DTO in both creation and updates, separated them as they have different fields
public record UpdateEmployeeDTO(int merchantId, String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
