package com.example.gongdal.controller.schedule.request;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class GetScheduleUserRequest {
    private LocalDate start;
    private LocalDate end;
}
