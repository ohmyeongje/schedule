package org.example.schedule.controller;


import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;
import org.example.schedule.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule") // 모든 경로에 /schedule prefix가 붙습니다.
public class ScheduleController {

    // In-memory DB 역할을 하는 Map 자료구조. Key: 식별자(Long), Value: 일정 데이터(Schedule)
    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    /**
     * 일정 생성 메서드
     *
     * @param requestDto 클라이언트가 보낸 요청 데이터(Task, AuthorName, Password 포함)
     * @return 생성된 일정의 응답 데이터
     */
    @PostMapping
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        // 고유 ID 생성 (Map의 최대 Key 값에 1을 더함)
        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;

        // 현재 시간을 생성 시간 및 수정 시간으로 설정
        LocalDateTime now = LocalDateTime.now();

        // 요청 데이터를 기반으로 Schedule 객체 생성
        Schedule schedule = new Schedule(
                scheduleId,
                requestDto.getTask(),
                requestDto.getAuthorName(),
                requestDto.getPassword(),
                now,
                now
        );

        // Map에 새로운 Schedule 추가
        scheduleList.put(scheduleId, schedule);

        // 생성된 Schedule을 응답 객체로 변환해 반환
        return new ScheduleResponseDto(schedule);
    }

    /**
     * ID로 특정 일정 조회
     *
     * @param id 요청받은 일정의 고유 식별자
     * @return 조회된 일정 데이터
     */
    @GetMapping("/{id}")
    public ScheduleResponseDto findScheduleById(@PathVariable Long id) {
        // Map에서 일정 조회
        Schedule schedule = scheduleList.get(id);

        // 일정이 존재하지 않을 경우 404 응답
        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found for ID: " + id);
        }

        // Schedule -> ScheduleResponseDto 변환 후 반환
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 조건에 따라 일정 목록 조회
     *
     * @param updatedDate 수정일 (YYYY-MM-DD 형식, 선택적)
     * @param authorName  작성자 이름 (선택적)
     * @return 조건에 맞는 일정 목록
     */
    @GetMapping
    public List<ScheduleResponseDto> findSchedulesByConditions(
            @RequestParam(required = false) String updatedDate,
            @RequestParam(required = false) String authorName
    ) {
        // 조건에 맞는 일정만 필터링
        return scheduleList.values().stream()
                .filter(schedule -> {
                    boolean matchesUpdatedDate = true;
                    boolean matchesAuthorName = true;

                    // 수정일 조건 확인
                    if (updatedDate != null) {
                        matchesUpdatedDate = schedule.getUpdatedDate()
                                .toLocalDate() // LocalDateTime -> LocalDate 변환
                                .toString()
                                .equals(updatedDate);
                    }

                    // 작성자 이름 조건 확인
                    if (authorName != null) {
                        matchesAuthorName = schedule.getAuthorName().equalsIgnoreCase(authorName);
                    }

                    return matchesUpdatedDate && matchesAuthorName;
                })
                .map(ScheduleResponseDto::new) // Schedule 객체를 ScheduleResponseDto로 변환
                .toList(); // 결과를 리스트로 반환
    }

    /**
     * ID로 특정 일정 수정
     *
     * @param id         수정할 일정의 고유 식별자
     * @param requestDto 수정 요청 데이터 (할일, 작성자명, 비밀번호 포함)
     * @return 수정된 일정 데이터
     */
    @PutMapping("/{id}")
    public ScheduleResponseDto updateScheduleById(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto requestDto
    ) {
        // Map에서 일정 조회
        Schedule schedule = scheduleList.get(id);

        // 일정이 존재하지 않을 경우 404 응답
        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found for ID: " + id);
        }

        // 비밀번호 검증
        if (!schedule.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password for schedule ID: " + id);
        }

        // 수정 가능한 필드(Task, AuthorName)만 업데이트
        if (requestDto.getTask() != null) {
            schedule.setTask(requestDto.getTask());
        }
        if (requestDto.getAuthorName() != null) {
            schedule.setAuthorName(requestDto.getAuthorName());
        }

        // 수정 시간 업데이트
        schedule.setUpdatedDate(LocalDateTime.now());

        // 수정된 Schedule을 응답 객체로 변환해 반환
        return new ScheduleResponseDto(schedule);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScheduleById(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody
    ) {
        // 일정 조회
        Schedule schedule = scheduleList.get(id);

        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found");
        }

        // 요청에서 비밀번호 추출
        String password = requestBody.get("password");
        if (password == null || !schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        // 일정 삭제
        scheduleList.remove(id);

        return ResponseEntity.ok("Schedule deleted successfully");
    }
}