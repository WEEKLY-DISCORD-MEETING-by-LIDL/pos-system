package com.example.wdmsystem.employee.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public final class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    //TODO: Add string restrictions of max length 30
    public String firstName;
    public String lastName;
    @Enumerated(EnumType.STRING)
    public EmployeeType employeeType;
    public String username;
    public String password;
    /// Type in docs is timestamp, could be changed later
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Employee(Integer id, int merchantId, String firstName, String lastName, EmployeeType employeeType, String username, String password, LocalDateTime createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeType = employeeType;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Employee() {

    }
}
