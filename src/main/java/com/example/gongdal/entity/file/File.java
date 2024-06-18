package com.example.gongdal.entity.file;

import com.example.gongdal.entity.BaseEntity;
import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.schedule.Schedule;
import com.example.gongdal.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.net.URL;

@Entity
@Getter
@Table(name = "tb_file_bas")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "url")
    private String url;

    @OneToOne(mappedBy = "profile")
    private User user;

    @OneToOne(mappedBy = "cover")
    private Group group;

    @OneToOne(mappedBy = "file")
    private Schedule schedule;

    @Column(name = "file_key")
    private String key;

    public static File createFile(String originalFilename, String url, String key) {
        return File.builder()
                .originalName(originalFilename)
                .url(url)
                .key(key)
                .build();
    }
}
