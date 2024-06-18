package com.example.gongdal.controller.user.adapter;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.entity.user.UserType;
import com.example.gongdal.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter {
    private final UserRepository userRepository;

    public void checkJoin(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new CustomRuntimeException(ErrorResponseCode.DUPLICATE_USERID);
        }
    }

    public User checkSocial(String loginId, UserType type) {
       return  userRepository.findByLoginIdAndJoinType(loginId, type).orElse(null);
    }

    public User checkLogin(String loginId) {
        return userRepository.findByLoginIdAndJoinType(loginId, UserType.N).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_USER));
    }

    public User validUserId(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_USER));
    }

    public User getUserByToken(String fcmToken) {
        return userRepository.findByFcmToken(fcmToken).orElseThrow(
                () -> new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_USER));
    }
}
