package com.senanur.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="task")
@Data
public class Task extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="status", nullable = false)
    private String status;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name="due_date")
    private Date dueDate;



    // Foreign key ile kullanıcıya bağlanıyoruz
    @ManyToOne(fetch = FetchType.LAZY) // lazy fetch önerilir, ihtiyaç olunca yüklenir
    @JoinColumn(name = "user_id", nullable = false) // veritabanında foreign key sütunu
    private User user;

}
