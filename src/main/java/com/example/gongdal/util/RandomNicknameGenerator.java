package com.example.gongdal.util;

import java.util.Random;

public class RandomNicknameGenerator {
    private static final String[] ADJECTIVES = {
            "신나는", "신비로운", "환상적인", "매력적인", "우아한",
            "재미있는", "반짝이는", "귀여운", "깜찍한", "화려한"
    };

    private static final String[] NOUNS = {
            "마법사", "은신자", "용사", "여왕", "탐험가",
            "유령", "도둑", "음악가", "요리사", "탐험대"
    };

    public static String generateRandomNickname() {
        Random random = new Random();
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        return adjective + noun;
    }
}
