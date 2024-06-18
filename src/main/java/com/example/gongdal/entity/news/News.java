package com.example.gongdal.entity.news;

import com.example.gongdal.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_news_bas")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class News extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
}
