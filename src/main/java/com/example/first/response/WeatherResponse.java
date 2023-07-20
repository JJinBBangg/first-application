package com.example.first.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WeatherResponse {
    String dateTime;
    String date;
    String TMP;
    String POP;
    String PCP;
    String PTY;
    String REH;
    String SNO;

    @Builder

    public WeatherResponse(String dateTime, String TMP, String POP, String PCP, String PTY, String REH, String SNO) {
        this.dateTime = dateTime;
        this.TMP = TMP;
        this.POP = POP;
        this.PCP = PCP;
        this.PTY = PTY;
        this.REH = REH;
        this.SNO = SNO;
    }
}
