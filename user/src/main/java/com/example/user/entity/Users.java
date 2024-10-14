package com.example.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@ToString @Getter @Setter
public class Users {

    @Id
    private String id;
    private String fullName;
    private String email;
    private Long typeId;

}
