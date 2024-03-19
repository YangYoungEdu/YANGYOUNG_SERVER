package com.yangyoung.server.day.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, Long> {

    Day findByDayName(String dayName);
}
