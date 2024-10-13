package com.example.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@ToString @Getter @Setter
public class User {

    @Id
    private String id;
    private String fullName;
    private String email;
    private Long typeId;

}
