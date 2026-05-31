package application.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.usuario.Plan;
import application.model.view.Views;
import application.service.PlanService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/planes")
@Validated
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Plan> getPlanes() {
        return planService.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Plan getPlanById(@PathVariable("id") @Positive int id) {
        return planService.findById(id);
    }
}
