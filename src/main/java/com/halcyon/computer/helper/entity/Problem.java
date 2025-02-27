package com.halcyon.computer.helper.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "problems")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "subcategory", nullable = false)
    private String subcategory;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "file_id")
    private String fileId;

    @ManyToOne
    @JoinColumn(name = "client_chat_id", referencedColumnName = "chat_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "specialist_chat_id", referencedColumnName = "chat_id")
    private Specialist specialist;

    @OneToOne(mappedBy = "problem")
    private Countdown countdown;
}
