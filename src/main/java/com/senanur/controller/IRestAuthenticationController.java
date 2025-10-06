package com.senanur.controller;

import com.senanur.dto.AuthRequest;
import com.senanur.dto.AuthResponse;
import com.senanur.dto.DtoUser;
import com.senanur.dto.RefreshTokenRequest;

public interface IRestAuthenticationController {
    public RootEntity<DtoUser> register(AuthRequest input);
    //public RootEntity<AuthResponse> authenticate(AuthRequest input);
    //public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest input);
}
