package com.example.wdmsystem.employee.system;

import com.example.wdmsystem.merchant.system.Merchant;

/// CreateEmployee is used as a DTO in both creation and updates, separated them as they have different fields
public record UpdateEmployeeDTO(Merchant merchant, String firstName, String lastName, EmployeeType employeeType, String username,
                          String password) {
}
