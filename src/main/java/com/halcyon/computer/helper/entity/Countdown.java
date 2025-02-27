package com.halcyon.computer.helper.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countdowns")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Countdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "file_id")
    private String fileId;

    @OneToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;
}
