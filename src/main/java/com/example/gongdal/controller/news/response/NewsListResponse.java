package com.example.gongdal.controller.news.response;

import com.example.gongdal.entity.news.News;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class NewsListResponse {
    private Long id;
    private String title;
    private String createDate;
    private String content;

    public static NewsListResponse toRes(News news) {
        return NewsListResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .createDate(news.getCreateDate().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT))
                .build();
    }
}
