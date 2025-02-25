package com.halcyon.computer.helper.service;

import com.halcyon.computer.helper.entity.Specialist;
import com.halcyon.computer.helper.exception.ResourceNotFoundException;
import com.halcyon.computer.helper.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialistService {
    private final SpecialistRepository specialistRepository;

    public void save(Specialist specialist) {
        specialistRepository.save(specialist);
    }

    public Boolean isRequest(Long chatId) {
        return specialistRepository.existsByChatIdAndIsRequest(chatId, true);
    }

    public List<Specialist> getActiveSpecialistsRequests() {
        return specialistRepository.findAllByIsRequest(true);
    }

    public Specialist findByChatId(Long chatId) {
        return specialistRepository.findByChatId(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist with chat_id " + chatId + " not found"));
    }

    public void acceptRequest(Long chatId) {
        Specialist specialist = findByChatId(chatId);
        specialist.setIsRequest(false);
        save(specialist);
    }

    @Transactional
    public void declineRequest(Long chatId) {
        specialistRepository.removeByChatId(chatId);
    }
}
