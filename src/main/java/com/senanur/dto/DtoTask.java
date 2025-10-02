package com.senanur.dto;

import com.senanur.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoTask extends DtoBase {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Date createTime;
    private Date dueDate;
    //private DtoUser user_id;


}
