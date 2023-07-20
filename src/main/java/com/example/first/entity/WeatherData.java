package com.example.first.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WeatherData {
    String dateTime;
    String TMP;
    String POP;
    String PCP;
    String PTY;
    String REH;
    String SNO;
    String TMN;
    String TMX;

    @Builder

    public WeatherData(String dateTime, String TMP, String POP, String PCP, String PTY, String REH, String SNO, String TMN, String TMX) {
        this.dateTime = dateTime;
        this.TMP = TMP;
        this.POP = POP;
        this.PCP = PCP;
        this.PTY = PTY;
        this.REH = REH;
        this.SNO = SNO;
        this.TMN = TMN;
        this.TMX = TMX;
    }
}
