package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdUsuarioNot(String nombre, int idUsuario);
}
