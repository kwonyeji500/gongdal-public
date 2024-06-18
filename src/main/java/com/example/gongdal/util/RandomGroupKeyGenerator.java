package com.example.gongdal.util;

import com.example.gongdal.repository.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class RandomGroupKeyGenerator {

    private static final String UPPER_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final int LENGTH = 4;
    private static final SecureRandom RANDOM = new SecureRandom();


    private final GroupRepository groupRepository;

    public String generateUniqueKey() {
        String key;
        do {
            key = generateRandomKey();
        } while (groupRepository.existsByKey(key));
        return key;
    }

    private String generateRandomKey() {
        StringBuilder sb = new StringBuilder(LENGTH * 2);
        for (int i = 0; i < LENGTH; i++) {
            sb.append(UPPER_ALPHABETS.charAt(RANDOM.nextInt(UPPER_ALPHABETS.length())));
        }
        for (int i = 0; i < LENGTH; i++) {
            sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }
}

