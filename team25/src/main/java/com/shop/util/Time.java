package com.shop.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Time {

    public static void main(String[] args) {
        // 현재 LocalDateTime 가져오기
        LocalDateTime localtime = LocalDateTime.now();

        // LocalDateTime을 Timestamp로 변환
        Timestamp timestamp = Timestamp.valueOf(localtime);

        // 비교할 LocalDateTime 설정 (예: 30일 전)
        LocalDateTime thresholdDateTime = localtime.minusDays(30);

        // LocalDateTime을 Timestamp로 변환
        Timestamp thresholdTimestamp = Timestamp.valueOf(thresholdDateTime);

        // Timestamp 비교
        if (timestamp.after(thresholdTimestamp)) {
            System.out.println("현재 시간은 30일 전의 시간보다 나중입니다.");
        } else if (timestamp.before(thresholdTimestamp)) {
            System.out.println("현재 시간은 30일 전의 시간보다 이전입니다.");
        } else {
            System.out.println("현재 시간과 30일 전의 시간이 같습니다.");
        }
    }
}
