package com.example.gongdal.service.news.adapter;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.news.News;
import com.example.gongdal.repository.news.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsAdapter {
    private final NewsRepository newsRepository;

    public News getNews(Long id) {
        return newsRepository.findById(id).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_NEWS));
    }
}
