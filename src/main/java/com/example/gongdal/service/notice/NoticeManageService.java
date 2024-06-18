package com.example.gongdal.service.notice;

import com.example.gongdal.controller.notice.response.NoticeGetResponse;
import com.example.gongdal.dto.notice.NoticeGetCommand;
import com.example.gongdal.dto.notice.NoticeUpdateCommand;
import com.example.gongdal.entity.notice.Notice;
import com.example.gongdal.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeManageService {
    private final NoticeRepository noticeRepository;

    public Page<NoticeGetResponse> getNotice(NoticeGetCommand command) {
        List<Notice> notices = noticeRepository.findByUserIdOrderByActiveAscIdDesc(command.getUser().getId());

        List<NoticeGetResponse> allNotice = notices.stream()
                .map(NoticeGetResponse::toRes).toList();

        int pageSize = command.getPageable().getPageSize();
        int currentPage = command.getPageable().getPageNumber();
        int startItem = currentPage * pageSize;

        List<NoticeGetResponse> pageNotice;

        if (allNotice.size() < startItem) {
            pageNotice = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, allNotice.size());
            pageNotice = allNotice.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageNotice, command.getPageable(), allNotice.size());
    }

    @Transactional
    public void updateNotice(NoticeUpdateCommand command) {
        List<Notice> notices = noticeRepository.findByUserIdAndActive(command.getUser().getId(), false);

        if (notices.isEmpty()) {
            return;
        }
        notices.forEach(Notice::updateActive);
    }
}
