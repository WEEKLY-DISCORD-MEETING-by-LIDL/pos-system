package customer.system.DTOS;

///CreateCustomer is used as a DTO in both creation and updates, so renamed to a more general term
public record CustomerDTO(String firstName, String lastName, String phone) { }
