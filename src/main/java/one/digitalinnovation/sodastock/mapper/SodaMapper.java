package one.digitalinnovation.sodastock.mapper;

import one.digitalinnovation.sodastock.dto.SodaDTO;
import one.digitalinnovation.sodastock.entity.Soda;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SodaMapper {

    SodaMapper INSTANCE = Mappers.getMapper(SodaMapper.class);

    Soda toModel(SodaDTO sodaDTO);

    SodaDTO toDTO(Soda soda);

}
