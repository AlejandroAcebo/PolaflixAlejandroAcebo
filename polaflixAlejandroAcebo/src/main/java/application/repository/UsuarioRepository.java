package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
