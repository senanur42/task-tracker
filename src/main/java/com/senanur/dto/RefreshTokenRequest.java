// jwt yerine ke clock geldiği için bu class artık kullanılmıyor

package com.senanur.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotEmpty
    private String refreshToken;
}