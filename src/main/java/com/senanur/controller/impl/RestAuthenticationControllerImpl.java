
package com.senanur.controller.impl;

import com.senanur.controller.IRestAuthenticationController;
import com.senanur.controller.RestBaseController;
import com.senanur.controller.RootEntity;
import com.senanur.dto.AuthRequest;
import com.senanur.dto.DtoUser;
import com.senanur.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bu Controller artık sadece yeni kullanıcı kaydını (register) yönetir.
 * Kimlik doğrulama (authenticate) ve token yenileme (refreshToken)
 * işlemleri tamamen Keycloak'a devredilmiştir.
 */
@RestController
public class RestAuthenticationControllerImpl extends RestBaseController implements IRestAuthenticationController {

    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/register")
    @Override
    public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest input) {
        // Bu metot, kullanıcıyı yerel DB'ye kaydetmeye devam eder.
        return ok(authenticationService.register(input));
    }

    // Eski /authenticate ve /refreshToken metotları KALDIRILDI.
    // Bu metotlar artık kullanılmamalıdır.
}







/*  jwt -> keyclockpackage com.senanur.controller.impl;

import com.senanur.controller.IRestAuthenticationController;
import com.senanur.controller.RestBaseController;
import com.senanur.controller.RootEntity;
import com.senanur.dto.AuthRequest;
import com.senanur.dto.AuthResponse;
import com.senanur.dto.DtoUser;
import com.senanur.dto.RefreshTokenRequest;
import com.senanur.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestAuthenticationControllerImpl extends RestBaseController implements IRestAuthenticationController {
    @Autowired
    private IAuthenticationService authenticationService;
    @PostMapping("/register")
    @Override
    public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest input) {
        return ok(authenticationService.register(input));
    }

    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest input) {

        return ok(authenticationService.authenticate(input));
    }

    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest input) {
        return ok(authenticationService.refreshToken(input));
    }
}
*/