package ayd2.ps2026.congress.example.controllers;

import ayd2.ps2026.congress.common.exceptions.NotFoundException;
import ayd2.ps2026.congress.example.dtos.request.ModelCreateDTO;
import ayd2.ps2026.congress.example.mappers.ModelMapper;
import ayd2.ps2026.congress.example.services.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/example")
@RequiredArgsConstructor
public class ExampleController {
    private final ModelService modelService;
    private final ModelMapper modelMapper;

    /**
     * example
     */
    @Operation(summary = "example",
            description = "example",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exito"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @PostMapping("/example")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void example(@RequestBody @Valid ModelCreateDTO modelCreateDTO) {

    }


    /**
     * example
     **/
    @Operation(summary = "example",
            description = "example",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Exitoso"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
            })
    @GetMapping("/{example}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void example(@PathVariable String example) throws NotFoundException {

    }

}
