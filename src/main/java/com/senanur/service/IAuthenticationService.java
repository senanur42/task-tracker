package com.senanur.service;

import com.senanur.dto.AuthRequest;
import com.senanur.dto.AuthResponse;
import com.senanur.dto.DtoUser;
import com.senanur.dto.RefreshTokenRequest;

public interface IAuthenticationService {

    public DtoUser register(AuthRequest input);
    public AuthResponse authenticate(AuthRequest input);

    public AuthResponse refreshToken(RefreshTokenRequest input);



}
