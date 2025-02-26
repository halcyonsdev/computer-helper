package com.halcyon.computer.helper.service;

import com.halcyon.computer.helper.entity.Client;
import com.halcyon.computer.helper.entity.Problem;
import com.halcyon.computer.helper.exception.ResourceNotFoundException;
import com.halcyon.computer.helper.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ClientService clientService;

    public Problem save(Problem problem, Long clientChatId) {
        Client client = clientService.findByChatId(clientChatId);
        problem.setClient(client);
        return problemRepository.save(problem);
    }

    public List<Problem> getClientProblems(Long clientChatId) {
        return problemRepository.findAllByClientChatId(clientChatId);
    }

    public Problem findById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem with id " + problemId + " not found"));
    }
}
