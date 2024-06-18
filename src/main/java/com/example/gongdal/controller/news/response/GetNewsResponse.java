package com.example.gongdal.controller.news.response;

import com.example.gongdal.entity.news.News;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class GetNewsResponse {
    private Long id;
    private String title;
    private String content;
    private String createDate;

    public static GetNewsResponse toRes(News news) {
        return GetNewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .createDate(news.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }
}
