package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.usuario.Plan;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
}
