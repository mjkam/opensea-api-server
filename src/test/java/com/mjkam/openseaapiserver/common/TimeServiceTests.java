package com.mjkam.openseaapiserver.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeServiceTests {
    private TimeService sut;

    @Test
    @DisplayName("clock 시간 기준으로 시간 리턴")
    void Given_FixedClock_When_GetCurrentDateTime_Then_ReturnLocalDateTimeByFixedClock() {
        //given
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        sut = new TimeService(clock);

        //when
        LocalDateTime dut = sut.getCurrentDateTime();

        //then
        assertThat(dut).isEqualTo(LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS));
    }
}

