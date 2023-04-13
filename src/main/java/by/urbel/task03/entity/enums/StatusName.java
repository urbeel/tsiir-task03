package by.urbel.task03.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusName {
    NEW("New"),
    ACCEPTED("Accepted"),
    FINISHED("Finished"),
    CANCELED("Canceled");
    private final String name;
}
