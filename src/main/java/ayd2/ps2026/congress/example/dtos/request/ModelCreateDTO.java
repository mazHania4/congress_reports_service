package ayd2.ps2026.congress.example.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class ModelCreateDTO {

    @NotBlank
    private String data;

}
