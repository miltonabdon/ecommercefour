package com.milton.ecommercefour.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.UUID;

@Entity
@Table(name = "users")
public record User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) UUID id,
        @Column(nullable = false) String firstName,
        @Column(nullable = false) String lastName,
        @Column(nullable = false, unique = true) String email
) {
}
