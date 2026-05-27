package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.usuario.Plan;
import application.repository.PlanRepository;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Plan findById(int idPlan) {
        return getPlanById(idPlan);
    }

    private Plan getPlanById(int idPlan) {
        return planRepository.findById(idPlan)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el plan con id " + idPlan));
    }
}
