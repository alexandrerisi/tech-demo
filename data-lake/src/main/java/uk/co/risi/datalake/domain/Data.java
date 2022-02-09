package uk.co.risi.datalake.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@lombok.Data
public class Data implements Comparable<Data> {

    @Id
    @JsonIgnore
    protected UUID uuid;
    protected LocalDateTime timestamp;

    @Override
    public int compareTo(Data o) {
        return o.timestamp.compareTo(timestamp);
    }
}
