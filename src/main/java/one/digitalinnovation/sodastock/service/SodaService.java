package one.digitalinnovation.sodastock.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.sodastock.dto.SodaDTO;
import one.digitalinnovation.sodastock.entity.Soda;
import one.digitalinnovation.sodastock.exception.SodaAlreadyRegisteredException;
import one.digitalinnovation.sodastock.exception.SodaNotFoundException;
import one.digitalinnovation.sodastock.exception.SodaStockExceededException;
import one.digitalinnovation.sodastock.mapper.SodaMapper;
import one.digitalinnovation.sodastock.repository.SodaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SodaService {

    private final SodaRepository sodaRepository;
    private final SodaMapper sodaMapper = SodaMapper.INSTANCE;

    public SodaDTO createSoda(SodaDTO sodaDTO) throws SodaAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(sodaDTO.getName());
        Soda soda = sodaMapper.toModel(sodaDTO);
        Soda savedSoda = sodaRepository.save(soda);
        return sodaMapper.toDTO(savedSoda);
    }

    public SodaDTO findByName(String name) throws SodaNotFoundException {
        Soda foundSoda = sodaRepository.findByName(name).orElseThrow(() -> new SodaNotFoundException(name));
        return sodaMapper.toDTO(foundSoda);
    }

    public List<SodaDTO> listAll() {
        return sodaRepository.findAll()
                .stream()
                .map(sodaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById (Long id) throws SodaNotFoundException {
        verifyIsExists(id);
        sodaRepository.deleteById(id);
    }

    public SodaDTO increment(Long id, int quantityToIncrement) throws SodaNotFoundException , SodaStockExceededException {

        Soda sodaToIncrementStock = verifyIsExists(id);
        int quantityAfterIncrement = quantityToIncrement + sodaToIncrementStock.getQuantity();

        if (quantityAfterIncrement <= sodaToIncrementStock.getMax()) {
            sodaToIncrementStock.setQuantity(sodaToIncrementStock.getQuantity() + quantityToIncrement);
            Soda incrementedSodaStock = sodaRepository.save(sodaToIncrementStock);
            return sodaMapper.toDTO(incrementedSodaStock);
        }

        throw new SodaStockExceededException(id, quantityToIncrement);
    }

    private Soda verifyIsExists (Long id) throws SodaNotFoundException {
        return sodaRepository.findById(id)
                .orElseThrow(() -> new SodaNotFoundException(id));
    }

    private void verifyIfIsAlreadyRegistered(String name) throws SodaAlreadyRegisteredException {

        Optional<Soda> optSavedSoda = sodaRepository.findByName(name);
        if (optSavedSoda.isPresent()) {
            throw new SodaAlreadyRegisteredException(name);
        }
    }

}
