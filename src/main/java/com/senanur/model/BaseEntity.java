package com.senanur.model;
// bütün tablolarımda id ve create time alanları olacağı için burada özel bir tanımla hallediyoruz.


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass  // bu sınıfı extend eden bütün tablolara kolon olarak yansıyacak demek istiyoruz
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="create_time")
    @CreationTimestamp
    private Date createTime;
}
