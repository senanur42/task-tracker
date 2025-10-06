package com.senanur.service.impl;

import com.senanur.dto.AuthRequest;
import com.senanur.dto.DtoUser;

import com.senanur.model.User;
import com.senanur.repository.UserRepository;
import com.senanur.service.IAuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;


/**
 * Bu servis artık sadece kullanıcı kaydı (register) ile ilgilenir.
 * Kimlik doğrulama (authenticate) ve token yenileme (refreshToken) işlemleri
 * doğrudan Keycloak tarafından Postman/Frontend aracılığıyla yapılacaktır.
 */
@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private User createUser(AuthRequest input){
        User user = new User();
        user.setCreateTime(new Date());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return user;
    }

    @Override
    public DtoUser register(AuthRequest input) {
        DtoUser dtoUser = new DtoUser();
        User savedUser = userRepository.save(createUser(input));
        BeanUtils.copyProperties(savedUser, dtoUser);
        return dtoUser;
    }

}



/*
Yalnızca jwt varken kullandığım kod key clock gelince değişti


package com.senanur.service.impl;

import com.senanur.dto.AuthRequest;
import com.senanur.dto.AuthResponse;
import com.senanur.dto.DtoUser;
import com.senanur.dto.RefreshTokenRequest;
import com.senanur.exception.BaseException;
import com.senanur.exception.ErrorMessage;
import com.senanur.exception.MessageType;
import com.senanur.jwt.JWTService;
import com.senanur.model.RefreshToken;
import com.senanur.model.User;
import com.senanur.repository.RefreshTokenRepository;
import com.senanur.repository.UserRepository;
import com.senanur.service.IAuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private User createUser(AuthRequest input){
        User user =new User();
        user.setCreateTime(new Date());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return user;
    }

    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreateTime(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000*60*60*4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }
    @Override
    public DtoUser register(AuthRequest input) {
        DtoUser dtoUser =new DtoUser();
        User savedUser = userRepository.save(createUser(input));
        BeanUtils.copyProperties(savedUser,dtoUser);
        return dtoUser;
    }

    @Override
    public AuthResponse authenticate(AuthRequest input) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken=new
                    UsernamePasswordAuthenticationToken(input.getUsername(),input.getPassword());

            authenticationProvider.authenticate(authenticationToken);

            Optional<User> optUser = userRepository.findByUsername(input.getUsername());
            String accessToken = jwtService.generateToken(optUser.get());
            RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(optUser.get()));

            return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken());
        }
        catch (Exception e){
            throw new BaseException(new ErrorMessage(MessageType.USERNAME_OR_PASSWORD_INVALID, e.getMessage()));
        }

    }

    public boolean isValidRefreshToken(Date expiredDate){
        // refreshtoken geçerliliğini yitirmiş olabilir
        return new Date().before(expiredDate);
    }
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest input) {
        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.findByRefreshToken(input.getRefreshToken());
        if(optRefreshToken.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND,input.getRefreshToken()));

        }
        if(!isValidRefreshToken(optRefreshToken.get().getExpiredDate())) // token expire olduysa
        {
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_IS_EXPIRED,input.getRefreshToken()));
        }
        User user = optRefreshToken.get().getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(user));
        return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken());
    }


}


*/