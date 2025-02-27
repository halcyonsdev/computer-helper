package com.halcyon.computer.helper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stars_count", nullable = false)
    private Integer startsCount;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "specialist_chat_id", referencedColumnName = "id")
    private Specialist specialist;

    @ManyToOne
    @JoinColumn(name = "client_chat_id", referencedColumnName = "id")
    private Client client;
}
