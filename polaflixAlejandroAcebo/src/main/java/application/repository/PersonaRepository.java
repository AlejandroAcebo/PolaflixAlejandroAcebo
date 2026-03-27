package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.persona.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}
