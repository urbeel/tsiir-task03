package by.urbel.task03.entity;

import by.urbel.task03.entity.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Role role;
    private String phone;
    private String address;
    private Long cartId;
}
