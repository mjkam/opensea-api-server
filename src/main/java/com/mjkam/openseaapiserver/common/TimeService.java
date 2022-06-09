package com.mjkam.openseaapiserver.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final Clock clock;

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(clock);
    }
}
