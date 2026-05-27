package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceConflictException;
import application.exception.ResourceNotFoundException;
import application.model.dto.usuario.UsuarioRequestDto;
import application.model.dto.usuario.UsuarioUpdateDto;
import application.model.entity.usuario.Plan;
import application.model.entity.usuario.Usuario;
import application.repository.PlanRepository;
import application.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PlanRepository planRepository) {
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(int idUsuario) {
        return getUsuarioById(idUsuario);
    }

    @Transactional
    public Usuario create(UsuarioRequestDto request) {
        Plan plan = getPlanById(request.getIdPlan());
        String nombre = request.getNombre().trim();
        validarNombreDisponible(nombre);

        Usuario usuario = Usuario.crear(
                nombre,
                request.getContrasena(),
                request.getCuentaBancaria(),
                plan,
                request.getCargos());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(int idUsuario, UsuarioUpdateDto request) {
        Usuario usuario = getUsuarioById(idUsuario);

        if(tieneTexto(request.getNombre())) {
            String nombre = request.getNombre().trim();
            validarNombreDisponible(nombre, idUsuario);
        }
        Plan plan = request.getIdPlan() == null ? null : getPlanById(request.getIdPlan());
        usuario.actualizarPerfil(
                request.getNombre(),
                request.getCuentaBancaria(),
                request.getContrasena(),
                plan,
                request.getCargos());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void delete(int idUsuario) {
        Usuario usuario = getUsuarioById(idUsuario);
        usuarioRepository.delete(usuario);
    }

    private Usuario getUsuarioById(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario));
    }

    private Plan getPlanById(int idPlan) {
        return planRepository.findById(idPlan)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el plan con id " + idPlan));
    }

    private void validarNombreDisponible(String nombre) {
        if (usuarioRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ResourceConflictException("Ya existe un usuario con ese nombre");
        }
    }

    private void validarNombreDisponible(String nombre, int idUsuario) {
        if (usuarioRepository.existsByNombreIgnoreCaseAndIdUsuarioNot(nombre, idUsuario)) {
            throw new ResourceConflictException("Ya existe un usuario con ese nombre");
        }
    }

    private boolean tieneTexto(String value) {
        return value != null && !value.isBlank();
    }

}
