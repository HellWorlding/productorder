package com.productorder.order.order.domain;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class OrderIdGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    // [yyMMdd][13자리] -> yyMMdd는 6자리, 뒤는 13자리
    private static final long RANDOM_BOUND = 10_000_000_000_000L; // 10^13
    private static final long MULTIPLIER   = RANDOM_BOUND;        // yyMMdd * 10^13
    private static final DateTimeFormatter YYMMDD = DateTimeFormatter.ofPattern("yyMMdd");
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private OrderIdGenerator() {
    }

    /** @return [yyMMdd][13-digit random] as Long */
    public static long nextId() {
        long datePart = Long.parseLong(LocalDate.now(KST).format(YYMMDD)); // 6 digits
        long randomPart = nextRandom13Digits(); // 0 ~ 10^13-1
        return datePart * MULTIPLIER + randomPart;
    }

    /** 0-padding된 13자리 문자열이 필요하면 문서/로그용으로 사용 */
    public static String nextRandom13DigitsString() {
        return String.format("%013d", nextRandom13Digits());
    }

    private static long nextRandom13Digits() {
        // SecureRandom은 Random을 상속하므로 nextLong(bound) 사용 가능(Java 17+)
        return RANDOM.nextLong(RANDOM_BOUND);
    }
}