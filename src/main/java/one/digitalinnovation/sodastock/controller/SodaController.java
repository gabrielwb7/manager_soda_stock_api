package one.digitalinnovation.sodastock.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.sodastock.dto.QuantityDTO;
import one.digitalinnovation.sodastock.dto.SodaDTO;
import one.digitalinnovation.sodastock.exception.SodaAlreadyRegisteredException;
import one.digitalinnovation.sodastock.exception.SodaNotFoundException;
import one.digitalinnovation.sodastock.exception.SodaStockExceededException;
import one.digitalinnovation.sodastock.service.SodaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sodas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SodaController {

    private final SodaService sodaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SodaDTO createSoda(@RequestBody @Valid SodaDTO sodaDTO) throws SodaAlreadyRegisteredException {
        return sodaService.createSoda(sodaDTO);
    }

    @GetMapping("/{name}")
    public SodaDTO findByName(@PathVariable String name) throws SodaNotFoundException {
        return sodaService.findByName(name);
    }

    @GetMapping
    public List<SodaDTO> listSodas() {
        return sodaService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws SodaNotFoundException {
        sodaService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public SodaDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws SodaNotFoundException, SodaStockExceededException {
        return sodaService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public SodaDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws SodaNotFoundException, SodaStockExceededException{
        return sodaService.decrement(id, quantityDTO.getQuantity());
    }


}

