package com.example.gongdal.repository.group;

import com.example.gongdal.entity.group.Group;
import com.example.gongdal.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByKey(String key);

    Optional<Group> findByKey(String key);

    @Query("SELECT g.users FROM Group g WHERE g.id = :groupId")
    Page<User> findUsersByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}
