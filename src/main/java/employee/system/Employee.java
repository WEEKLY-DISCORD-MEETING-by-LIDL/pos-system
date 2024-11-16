package employee.system;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public final class Employee {
    public int id;
    public int merchantId;
    //TODO: Add string restrictions of max length 30
    public String firstName;
    public String lastName;
    public EmployeeType employeeType;
    public String username;
    public String password;
    /// Type in docs is timestamp, could be changed later
    public Date createdAt;
    public Date updatedAt;

    public Employee(int id, int merchantId, String firstName, String lastName, EmployeeType employeeType, String username, String password, Date createdAt) {
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
}
