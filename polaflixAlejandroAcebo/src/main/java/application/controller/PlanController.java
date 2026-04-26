package application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.dto.plan.PlanResponseDto;
import application.service.PlanService;

@RestController
@RequestMapping("/planes")
@Validated
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public List<PlanResponseDto> getPlanes() {
        return planService.findAll();
    }

    @GetMapping("/{id}")
    public PlanResponseDto getPlanById(@PathVariable int id) {
        return planService.findById(id);
    }
}
