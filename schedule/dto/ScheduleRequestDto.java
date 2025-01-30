package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Schedule;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {
    private String task;          // 할일
    private String authorName;    // 작성자명
    private String password;      // 비밀번호

}






