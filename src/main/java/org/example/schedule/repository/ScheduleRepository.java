package org.example.schedule.repository;

import org.example.schedule.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScheduleRepository {
    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    // 새로운 일정 저장
    public Schedule save(Schedule schedule) {
        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;
        schedule.setId(scheduleId);
        scheduleList.put(scheduleId, schedule);
        return schedule;
    }

    // ID로 일정 조회
    public Optional<Schedule> findById(Long id) {
        return Optional.ofNullable(scheduleList.get(id));
    }

    // 일정 삭제
    public boolean deleteById(Long id) {
        return scheduleList.remove(id) != null;
    }
}

