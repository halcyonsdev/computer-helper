package com.halcyon.computer.helper.service;

import com.halcyon.computer.helper.entity.Client;
import com.halcyon.computer.helper.exception.ResourceNotFoundException;
import com.halcyon.computer.helper.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client findByChatId(Long chatId) {
        return clientRepository.findByChatId(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Client with chat_id " + chatId + " not found"));
    }

    @Transactional
    public void deleteByChatId(Long chatId) {
        clientRepository.deleteByChatId(chatId);
    }

    public boolean existsByChatId(Long chatId) {
        return clientRepository.existsByChatId(chatId);
    }
}
