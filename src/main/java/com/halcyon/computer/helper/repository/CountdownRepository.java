package com.halcyon.computer.helper.repository;

import com.halcyon.computer.helper.entity.Countdown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountdownRepository extends JpaRepository<Countdown, Long> {
}
