package com.halcyon.computer.helper.repository;

import com.halcyon.computer.helper.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    boolean existsByChatIdAndIsRequest(Long chatId, Boolean isRequest);
    List<Specialist> findAllByIsRequest(Boolean isRequest);
    Optional<Specialist> findByChatId(Long chatId);
    void removeByChatId(Long chatId);
}
