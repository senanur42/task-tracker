// jwt yerine ke clock geldiği için bu class artık kullanılmıyor

package com.senanur.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthRequest {
    @NotEmpty
    public String username;
    @NotEmpty
    public String password;

}
