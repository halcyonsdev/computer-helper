package com.halcyon.computer.helper.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specialists")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specialist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_request", nullable = false)
    private Boolean isRequest;
}
