package com.halcyon.computer.helper.repository;

import com.halcyon.computer.helper.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByChatId(Long chatId);
    void deleteByChatId(Long chatId);
    boolean existsByChatId(Long chatId);
}
