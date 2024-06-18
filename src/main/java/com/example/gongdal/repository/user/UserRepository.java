package com.example.gongdal.repository.user;

import com.example.gongdal.entity.user.User;
import com.example.gongdal.entity.user.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginIdAndJoinType(String loginId, UserType joinType);

    Optional<User> findByFcmToken(String fcmToken);
}
