package one.digitalinnovation.sodastock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.digitalinnovation.sodastock.enums.SodaSize;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SodaDTO {

    private Long id;


    @Size(min = 1, max = 200)
    @NotNull
    private String name;


    @Max(500)
    @NotNull
    private Integer max;


    @Max(100)
    @NotNull
    private Integer quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SodaSize size;

}