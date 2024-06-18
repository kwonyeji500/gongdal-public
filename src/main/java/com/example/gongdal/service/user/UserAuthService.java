package com.example.gongdal.service.user;

import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.error.CustomRuntimeException;
import com.example.gongdal.config.jwt.JwtProvider;
import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.dto.token.TokenCommand;
import com.example.gongdal.dto.token.TokenRenewCommand;
import com.example.gongdal.dto.user.command.NormalUserJoinCommand;
import com.example.gongdal.dto.user.command.NormalUserLoginCommand;
import com.example.gongdal.dto.user.command.SocialCheckCommand;
import com.example.gongdal.entity.user.User;
import com.example.gongdal.entity.user.UserType;
import com.example.gongdal.repository.user.UserRepository;
import com.example.gongdal.service.fcm.FcmSubscriptionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

import static com.example.gongdal.util.RandomNicknameGenerator.generateRandomNickname;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {
    private final UserAdapter userAdapter;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final FcmSubscriptionManager fcmSubscriptionManager;
    @Value("${kakao.app.key}")
    private String kakaoKey;
    @Value("${google.app.key}")
    private String googleKey;
    @Value("${apple.app.key}")
    private String appleKey;

    @Transactional
    public User loginUser(NormalUserLoginCommand command) {
        User user = authenticateUser(command);
        String newFcmToken = command.getFcmToken();

        handleFcmTokenUpdate(user, newFcmToken);

        return user;
    }

    private User authenticateUser(NormalUserLoginCommand command) {
        User user = userAdapter.checkLogin(command.getLoginId());

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new CustomRuntimeException(ErrorResponseCode.USER_LOGIN_ERROR);
        }

        String accessToken = jwtProvider.createAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(user.getId()));

        user.login(accessToken, refreshToken);

        return user;
    }

    private void handleFcmTokenUpdate(User user, String newFcmToken) {
        String currentFcmToken = user.getFcmToken();

        if (currentFcmToken == null) {
            user.updateFcmToken(newFcmToken);
            fcmSubscriptionManager.subscribeToTopics(newFcmToken, List.of("all"));
        } else if (!currentFcmToken.equals(newFcmToken)) {
            user.updateFcmToken(newFcmToken);
            updateFcmSubscriptions(user, currentFcmToken, newFcmToken);
        }
    }

    private void updateFcmSubscriptions(User user, String oldToken, String newToken) {
        List<String> topicList = fcmSubscriptionManager.getSubscribedTopics(user.getGroups());
        fcmSubscriptionManager.subscribeToTopics(newToken, topicList);
        fcmSubscriptionManager.unsubscribeFromTopics(oldToken, topicList);
    }

    public void logoutUser(User user) {
        user.logout();

        userRepository.save(user);
    }

    @Transactional
    public TokenCommand renewToken(TokenRenewCommand command) {
        TokenCommand token = jwtProvider.renewAccessToken(command);

        token.getUser().renewToken(token);

        return token;
    }

    @Transactional
    public User checkOauth(SocialCheckCommand command) {
        User user = authenticationSocial(command);
        String newFcmToken = command.getFcmToken();

        handleFcmTokenUpdate(user, newFcmToken);

        return user;
    }

    private User authenticationSocial(SocialCheckCommand command) {
        String loginId = validateToken(command.getIdToken(), command.getType());
        User user = userAdapter.checkSocial(loginId, command.getType());
        String nickname = generateRandomNickname();

        if (user == null) {
            user = userRepository.save(User.createSocial(command, loginId, nickname));
        }

        String accessToken = jwtProvider.createAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(user.getId()));

        user.login(accessToken, refreshToken);

        return user;
    }

    private String validateToken(String idToken, UserType type) {
        String[] jwtParts = idToken.split("\\.");

        String payload = new String(Base64.getDecoder().decode(jwtParts[1]));

        JSONObject jsonPayload = new JSONObject(payload);

        String key = jsonPayload.getString("aud");

        String check = "";

        switch (type) {
            case K :
                check = kakaoKey;
                break;
            case G:
                check = googleKey;
                break;
            case A:
                check = appleKey;
                break;
            default:
                throw new CustomRuntimeException(ErrorResponseCode.NOT_FOUND_SOCIAL);
        }

        if (!check.equals(key)) {
            throw new CustomRuntimeException(ErrorResponseCode.NOT_VALID_SOCIAL_TOKEN);
        }

        return jsonPayload.getString("email");
    }

    public void joinNormal(NormalUserJoinCommand command) {
        userAdapter.checkJoin(command.getLoginId());

        String password = passwordEncoder.encode(command.getPassword());

        String nickname = generateRandomNickname();

        userRepository.save(User.createNormal(command, password, nickname));
    }
}
