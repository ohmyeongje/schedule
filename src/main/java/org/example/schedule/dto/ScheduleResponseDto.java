package org.example.schedule.dto;

import lombok.Getter;
import org.example.schedule.entity.Schedule;

import java.time.LocalDateTime;
@Getter
public class ScheduleResponseDto {
    private Long id; // 고유 식별자
    private String task;          // 할일
    private String authorName;    // 작성자명
    private String password;      // 비밀번호
    private LocalDateTime createdDate;  // 작성일
    private LocalDateTime updatedDate;  // 수정일

    // Memo class를 인자로 가지는 생성자
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.task = schedule.getTask();
        this.authorName = schedule.getAuthorName();
        this.password = schedule.getPassword();
        this.createdDate = schedule.getCreatedDate();
        this.updatedDate = schedule.getUpdatedDate();
    }
}
