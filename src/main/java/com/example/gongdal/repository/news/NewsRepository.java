package com.example.gongdal.repository.news;

import com.example.gongdal.entity.news.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
