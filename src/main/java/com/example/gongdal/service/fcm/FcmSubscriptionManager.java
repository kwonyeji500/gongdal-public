package com.example.gongdal.service.fcm;

import com.example.gongdal.controller.user.adapter.UserAdapter;
import com.example.gongdal.entity.group.Group;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmSubscriptionManager {
    private final UserAdapter userAdapter;
    @Value("${fcm.topic}")
    private String topicName;

    public void subscribeToTopics(String newFcmToken, List<String> topics) {
        for (String topic : topics) {
            try {
                FirebaseMessaging.getInstance().subscribeToTopic(
                        Arrays.asList(newFcmToken), topic + topicName);
            } catch (FirebaseMessagingException e) {
                log.error("Firebase subscribeToTopics FirebaseMessagingException: " + e.getMessage());
            }
        }
    }

    public void unsubscribeFromTopics(String oldFcmToken, List<String> topics) {
        for (String topic : topics) {
            try {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(
                        Arrays.asList(oldFcmToken), topic + topicName);
            } catch (FirebaseMessagingException e) {
                log.error("Firebase unsubscribeFromTopics FirebaseMessagingException: " + e.getMessage());
            }
        }
    }

    public void unsubscribeFromTokens(List<String> tokens, String topic) {
            try {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(
                        tokens, topic + topicName);
            } catch (FirebaseMessagingException e) {
                log.error("Firebase unsubscribeFromTopics FirebaseMessagingException: " + e.getMessage());
            }
    }

    public List<String> getSubscribedTopics(List<Group> groups) {
        List<String> topicList = new ArrayList<>(groups.stream().map(Group::getKey).toList());

        topicList.add("all");

        return topicList;
    }
}
