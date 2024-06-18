package com.example.gongdal.service.news;

import com.example.gongdal.entity.news.News;
import com.example.gongdal.repository.news.NewsRepository;
import com.example.gongdal.service.news.adapter.NewsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsAdapter newsAdapter;

    public Page<News> getList(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public News getDetail(long newsId) {
        return newsAdapter.getNews(newsId);
    }
}
