package com.senanur.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoTaskIU {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String status;
    @NotNull
    private Long user_id;

    private Date dueDate;
}
