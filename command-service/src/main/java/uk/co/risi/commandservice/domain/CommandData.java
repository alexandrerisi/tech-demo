package uk.co.risi.commandservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommandData {

    private LocalDateTime time;
    private String command;

    public static CommandData jsonToData(CommandIngestion ingest) {
        return new CommandData(ingest.getTime(), ingest.getCommand());
    }
}
