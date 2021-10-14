package one.digitalinnovation.sodastock.service;

import one.digitalinnovation.sodastock.builder.SodaDTOBuilder;
import one.digitalinnovation.sodastock.dto.SodaDTO;
import one.digitalinnovation.sodastock.entity.Soda;
import one.digitalinnovation.sodastock.exception.SodaAlreadyRegisteredException;
import one.digitalinnovation.sodastock.exception.SodaNotFoundException;
import one.digitalinnovation.sodastock.exception.SodaStockExceededException;
import one.digitalinnovation.sodastock.mapper.SodaMapper;
import one.digitalinnovation.sodastock.repository.SodaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SodaServiceTest {

    private static final long INVALID_SODA_ID = 1L;

    @Mock
    private SodaRepository sodaRepository;

    private SodaMapper sodaMapper = SodaMapper.INSTANCE;

    @InjectMocks
    private SodaService sodaService;

    @Test
    void whenSodaInformedThenItShouldBeCreated() throws SodaAlreadyRegisteredException {

        SodaDTO sodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSavedSoda = sodaMapper.toModel(sodaDTO);

        when(sodaRepository.findByName(sodaDTO.getName())).thenReturn(Optional.empty());
        when(sodaRepository.save(expectedSavedSoda)).thenReturn(expectedSavedSoda);

        SodaDTO createSodaDTO = sodaService.createSoda(sodaDTO);

        assertEquals(sodaDTO.getId(), createSodaDTO.getId());
        assertEquals(sodaDTO.getName(), createSodaDTO.getName());
        assertEquals(sodaDTO.getQuantity(), createSodaDTO.getQuantity());
    }

    @Test
    void whenAlreadyRegisteredSodaInformedThenAnExceptionShouldBeThrow() throws SodaAlreadyRegisteredException {

        SodaDTO sodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda duplicatedSoda = sodaMapper.toModel(sodaDTO);

        when(sodaRepository.findByName(sodaDTO.getName())).thenReturn(Optional.of(duplicatedSoda));

        assertThrows(SodaAlreadyRegisteredException.class, () -> sodaService.createSoda(sodaDTO));

    }

    @Test
    void whenValidSodaNameIsGivenThenReturnASoda() throws SodaNotFoundException {

        SodaDTO expectedFoundSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedFoundSoda = sodaMapper.toModel(expectedFoundSodaDTO);

        when(sodaRepository.findByName(expectedFoundSoda.getName())).thenReturn(Optional.of(expectedFoundSoda));

        SodaDTO foundSodaDTO = sodaService.findByName(expectedFoundSodaDTO.getName());

        assertEquals(expectedFoundSodaDTO.getName(), foundSodaDTO.getName());

    }

    @Test
    void whenNoRegisteredSodaNameIsGivenThenAThrowAnException() {

        SodaDTO expectedFoundSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();

        when(sodaRepository.findByName(expectedFoundSodaDTO.getName())).thenReturn(Optional.empty());

        assertThrows(SodaNotFoundException.class, () -> sodaService.findByName(expectedFoundSodaDTO.getName()));

    }

    @Test
    void whenListSodasIsCalledThenReturnAListOfSodas() {

        SodaDTO expectedFoundSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedFoundSoda = sodaMapper.toModel(expectedFoundSodaDTO);

        when(sodaRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundSoda));

        List<SodaDTO> foundListSodaDTO = sodaService.listAll();

        assertThat(foundListSodaDTO, is(not(empty())));
        assertThat(foundListSodaDTO.get(0), is(equalTo(expectedFoundSodaDTO)));

    }

    @Test
    void whenListSodasIsCalledThenReturnAnEmptyListOfSodas() {

        when(sodaRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<SodaDTO> foundListSodaDTO = sodaService.listAll();

        assertThat(foundListSodaDTO, is((empty())));

    }

    @Test
    void whenDeleteIsCalledWithValidIdThenASodaShouldBeDelete() throws SodaNotFoundException {

        SodaDTO expectedDeleteSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedDeleteSoda = sodaMapper.toModel(expectedDeleteSodaDTO);

        when(sodaRepository.findById(expectedDeleteSodaDTO.getId())).thenReturn(Optional.of(expectedDeleteSoda));
        doNothing().when(sodaRepository).deleteById(expectedDeleteSodaDTO.getId());

        sodaService.deleteById(expectedDeleteSodaDTO.getId());

        verify(sodaRepository, times(1)).findById(expectedDeleteSodaDTO.getId());
        verify(sodaRepository, times(1)).deleteById(expectedDeleteSodaDTO.getId());

    }

    @Test
    void whenDeleteIsCalledWithInvalidIdIsGivenThenAThrowAnException() {

        when(sodaRepository.findById(INVALID_SODA_ID)).thenReturn(Optional.empty());

        assertThrows(SodaNotFoundException.class, () -> sodaService.deleteById(INVALID_SODA_ID));

    }

    @Test
    void whenIncrementIsCalledThenIncrementSodaStock() throws SodaNotFoundException, SodaStockExceededException {

        SodaDTO expectedSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSavedSoda = sodaMapper.toModel(expectedSodaDTO);

        when(sodaRepository.findById(expectedSodaDTO.getId())).thenReturn(Optional.of(expectedSavedSoda));
        when(sodaRepository.save(expectedSavedSoda)).thenReturn(expectedSavedSoda);

        int quantityToIncrement = 10;
        int expectedQuantityAfterToIncrement = expectedSodaDTO.getQuantity() + quantityToIncrement;

        SodaDTO incrementedSodaDTO = sodaService.increment(expectedSodaDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterToIncrement, equalTo(incrementedSodaDTO.getQuantity()));
        assertThat(expectedQuantityAfterToIncrement, lessThan(incrementedSodaDTO.getMax()));

    }

    @Test
    void whenIncrementIsGreaterThanMaxStockSodaThenThrowExcepetion() throws SodaNotFoundException, SodaStockExceededException {

        SodaDTO expectedSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSoda = sodaMapper.toModel(expectedSodaDTO);

        when(sodaRepository.findById(expectedSodaDTO.getId())).thenReturn(Optional.of(expectedSoda));

        int quantityToIncrement = 100;

        assertThrows(SodaStockExceededException.class, ()-> sodaService.increment(expectedSodaDTO.getId(), quantityToIncrement));

    }


    @Test
    void whenIncrementAfterSumIsGreaterThanMaxStockSodaThenThrowExcepetion() {

        SodaDTO expectedSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSoda = sodaMapper.toModel(expectedSodaDTO);

        when(sodaRepository.findById(expectedSodaDTO.getId())).thenReturn(Optional.of(expectedSoda));

        int quantityToIncrement = 100;

        assertThrows(SodaStockExceededException.class, ()-> sodaService.increment(expectedSodaDTO.getId(), quantityToIncrement));

    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenAThrowAnException() {

        int quantityToIncrement = 100;

        when(sodaRepository.findById(INVALID_SODA_ID)).thenReturn(Optional.empty());

        assertThrows(SodaNotFoundException.class, () -> sodaService.increment(INVALID_SODA_ID, quantityToIncrement));

    }

    @Test
    void whenDecrementIsCalledThenDecrementSodaStock() throws SodaNotFoundException, SodaStockExceededException {

        SodaDTO expectedSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSavedSoda = sodaMapper.toModel(expectedSodaDTO);

        when(sodaRepository.findById(expectedSodaDTO.getId())).thenReturn(Optional.of(expectedSavedSoda));
        when(sodaRepository.save(expectedSavedSoda)).thenReturn(expectedSavedSoda);

        int quantityToDecrement = 5;
        int expectedQuantityAfterToDecrement = expectedSodaDTO.getQuantity() - quantityToDecrement;

        SodaDTO decrementedSodaDTO = sodaService.decrement(expectedSodaDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterToDecrement, equalTo(decrementedSodaDTO.getQuantity()));
        assertThat(expectedQuantityAfterToDecrement, greaterThan(0));

    }

    @Test
    void whenDecrementIsCalledToEmptyStockThenEmptySodaStock() throws SodaNotFoundException, SodaStockExceededException {

        SodaDTO expectedSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSavedSoda = sodaMapper.toModel(expectedSodaDTO);

        when(sodaRepository.findById(expectedSodaDTO.getId())).thenReturn(Optional.of(expectedSavedSoda));
        when(sodaRepository.save(expectedSavedSoda)).thenReturn(expectedSavedSoda);

        int quantityToDecrement = 10;
        int expectedQuantityAfterToDecrement = expectedSodaDTO.getQuantity() - quantityToDecrement;

        SodaDTO decrementedSodaDTO = sodaService.decrement(expectedSodaDTO.getId(), quantityToDecrement);

        assertThat(expectedQuantityAfterToDecrement, equalTo(decrementedSodaDTO.getQuantity()));
        assertThat(expectedQuantityAfterToDecrement, equalTo(0));
    }


    @Test
    void whenDecrementIsLowerThanZeroThenThrowException() {
        SodaDTO expectedSodaDTO = SodaDTOBuilder.builder().build().toSodaDTO();
        Soda expectedSoda = sodaMapper.toModel(expectedSodaDTO);

        when(sodaRepository.findById(expectedSodaDTO.getId())).thenReturn(Optional.of(expectedSoda));

        int quantityToDecrement = 80;

        assertThrows(SodaStockExceededException.class, () -> sodaService.decrement(expectedSodaDTO.getId(), quantityToDecrement));
    }


    @Test
    void whenDecrementIsCalledWithInvalidIdThenAThrowAnException() {

        int quantityToDecrement = 100;

        when(sodaRepository.findById(INVALID_SODA_ID)).thenReturn(Optional.empty());

        assertThrows(SodaNotFoundException.class, () -> sodaService.decrement(INVALID_SODA_ID, quantityToDecrement));

    }
}





























