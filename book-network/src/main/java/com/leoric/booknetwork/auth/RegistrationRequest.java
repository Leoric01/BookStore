package com.leoric.booknetwork.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "First name is mandatory")
    @NotBlank(message = "First name is mandatory")
    private String firstname;
    @NotEmpty(message = "Last name is mandatory")
    @NotBlank(message = "Last name is mandatory")
    private String lastname;
    @NotEmpty(message = "Email name is mandatory")
    @NotBlank(message = "Email name is mandatory")
    @Email(message = "Email is not in valid format")
    private String email;
    @NotEmpty(message = "Password name is mandatory")
    @NotBlank(message = "Passwrd name is mandatory")
    @Size(min = 4, message = "Password should be at least 4 characters long")
    private String password;

}
