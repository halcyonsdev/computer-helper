package com.halcyon.computer.helper.repository;

import com.halcyon.computer.helper.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findAllByClientChatId(Long clientChatId);
    List<Problem> findAllByStatus(String status);
}
