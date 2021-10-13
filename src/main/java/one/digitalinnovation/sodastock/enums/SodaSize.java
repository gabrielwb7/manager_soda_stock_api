package one.digitalinnovation.sodastock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SodaSize {

    VERYSMALL("350ml"),
    SMALL("600ml"),
    REGULAR("1L"),
    BIG("2L"),
    VERYBIG("2.5L");


    private final String description;
}
