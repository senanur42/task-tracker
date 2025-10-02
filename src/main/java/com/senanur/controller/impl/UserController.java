package com.senanur.controller.impl;

import com.senanur.controller.IUserController;
import com.senanur.dto.DtoUser;
import com.senanur.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController implements IUserController {

}
