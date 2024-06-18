package com.example.gongdal.repository.schedule;

import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.schedule.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findByGroup(Group group, Pageable pageable);
}
