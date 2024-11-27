package employee.system;

import org.springframework.stereotype.Repository;
import utils.IRepository;

import java.util.List;

//TODO: Implement database logic
@Repository
public class EmployeeRepository implements IRepository<Employee> {
    public void add(Employee employee) {
    }

    public Employee get(int id) {
        return null;
    }

    public void update(int id, Employee employee) {
    }

    public void delete(int id) {
    }

    public List<Employee> getMultiple(EmployeeType type, int limit) {
        return null;
    }
}
