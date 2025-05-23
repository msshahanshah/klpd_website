package com.klpdapp.klpd.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, length = 100)
    private String name;

    private LocalDate dob;

    private LocalDate SpouseDob;

    private LocalDate anniversary;

    @Column(length = 20)
    private String childName;

    private LocalDate childDob;

    @Column(length = 20)
    private String gender;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    private long mobile;

    @Column(name = "alternate_mobile")
    private Long alternateMobile;

    @Column(length = 50)
    private String status;

    @Column(nullable = false)
    private boolean enabled = true;
}