package one.digitalinnovation.sodastock.builder;

import lombok.Builder;
import one.digitalinnovation.sodastock.dto.SodaDTO;
import one.digitalinnovation.sodastock.enums.SodaSize;

@Builder
public class SodaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Mineiro";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private SodaSize size = SodaSize.BIG;

    public SodaDTO toSodaDTO() {
        return new SodaDTO(id,
                name,
                max,
                quantity,
                size);
    }



}
