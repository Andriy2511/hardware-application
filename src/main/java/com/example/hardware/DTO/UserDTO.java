package com.example.hardware.DTO;

import com.example.hardware.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "Name cannot be void")
    private String name;

    @NotBlank(message = "Surname cannot be void")
    private String surname;

    @NotBlank(message = "Login cannot be void")
    private String login;
    @Size(min = 5, message = "Password has to contain at least 5 symbols")
    private String password;

    public UserDTO(User user){
        this.name = user.getName();
        this.surname = user.getSurname();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }
}
