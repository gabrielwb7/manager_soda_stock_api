package one.digitalinnovation.sodastock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.sodastock.dto.SodaDTO;
import one.digitalinnovation.sodastock.exception.SodaAlreadyRegisteredException;
import one.digitalinnovation.sodastock.exception.SodaNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manage soda stock.")
public interface SodaControllerDocs {

    @ApiOperation(value = "Soda creation operation.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success soda creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value")
    })
    SodaDTO createSoda(SodaDTO sodaDTO) throws SodaAlreadyRegisteredException;



    @ApiOperation(value = "Returns soda found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success soda found in system"),
            @ApiResponse(code = 404, message = "Soda with given name not found")
    })
    SodaDTO findByName(@PathVariable String name) throws SodaNotFoundException;

    @ApiOperation(value = "Returns a list of all sodas registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all sodas registered in the system")
    })
    List<SodaDTO> listSodas();


    @ApiOperation(value = "Delete a soda found by a given valid ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success soda delete in the system"),
            @ApiResponse(code = 404, message = "Soda with given ID not found")
    })
    void deleteById(@PathVariable Long id) throws SodaNotFoundException;
}