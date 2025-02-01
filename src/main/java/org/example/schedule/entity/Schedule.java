package org.example.schedule.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.schedule.dto.ScheduleRequestDto;
import org.example.schedule.dto.ScheduleResponseDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id; // 고유 식별자
    private String task;          // 할일
    private String authorName;    // 작성자명
    private String password;      // 비밀번호
    private LocalDateTime createdDate;  // 작성일
    private LocalDateTime updatedDate;  // 수정일

    // 생성자에서 날짜 초기화
    public Schedule(Long id, String task, String authorName, String password) {
        this.id = id;
        this.task = task;
        this.authorName = authorName;
        this.password = password;
        this.createdDate = LocalDateTime.now(); // 현재 시간으로 초기화
        this.updatedDate = this.createdDate;   // 수정일은 생성일과 동일
    }

    public void update(ScheduleRequestDto requestDto) {
        this.task = requestDto.getTask();
        this.authorName = requestDto.getAuthorName();
        this.password = requestDto.getPassword();

    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {

        this.updatedDate = updatedDate;
    }

    public void setId(Long scheduleId) {

    }
}


