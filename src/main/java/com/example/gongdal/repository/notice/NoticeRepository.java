package com.example.gongdal.repository.notice;

import com.example.gongdal.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByUserIdOrderByActiveAscIdDesc(Long userId);

    List<Notice> findByUserIdAndActive(Long id, boolean b);

}
