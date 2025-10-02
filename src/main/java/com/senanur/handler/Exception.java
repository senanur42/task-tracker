package com.senanur.handler;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Exception <E> {
    private String path; // hangi addreste hata aldı

    private Date createTime;

    private String hostName;

    private E message; // e tipinde bir mesaj alacaksın

}
