package application.model.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanResponseDto {
    
    @JsonProperty("idPlan")
    private int idPlan;
    
    @JsonProperty("precio")
    private double precio;
    
    @JsonProperty("tipoPlan")
    private String tipoPlan;
}
