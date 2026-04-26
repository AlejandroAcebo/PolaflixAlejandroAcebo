package application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.plan.PlanResponseDto;
import application.model.entity.usuario.Plan;
import application.repository.PlanRepository;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<PlanResponseDto> findAll() {
        return planRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponseDto findById(int idPlan) {
        return toResponse(getPlanById(idPlan));
    }

    private Plan getPlanById(int idPlan) {
        return planRepository.findById(idPlan)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el plan con id " + idPlan));
    }

    private PlanResponseDto toResponse(Plan plan) {
        String tipoPlan = plan.getClass().getSimpleName();
        return new PlanResponseDto(
                plan.getIdPlan(),
                plan.getPrecio(),
                tipoPlan
        );
    }
}
