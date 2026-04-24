package application.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import jakarta.validation.constraints.NotBlank;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.usuario.UsuarioRequestDto;
import application.model.dto.usuario.UsuarioResponseDto;
import application.model.dto.usuario.UsuarioUpdateDto;
import application.model.entity.usuario.Plan;
import application.model.entity.usuario.Usuario;
import application.repository.PlanRepository;
import application.repository.UsuarioRepository;
import io.micrometer.common.util.StringUtils;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PlanRepository planRepository) {
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto findById(int idUsuario) {
        return toResponse(getUsuarioById(idUsuario));
    }

    @Transactional
    public UsuarioResponseDto create(UsuarioRequestDto request) {
        Plan plan = getPlanById(request.getIdPlan());

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .password(request.getPassword())
                .cuentaBancaria(request.getCuentaBancaria())
                .plan(plan)
                .facturas(new ArrayList<>())
                .series(new ArrayList<>())
                .visualizacionesPersistidas(new ArrayList<>())
                .visualizaciones(new LinkedHashMap<>())
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDto update(int idUsuario, UsuarioUpdateDto request) {
        Usuario usuario = getUsuarioById(idUsuario);
        Plan plan = getPlanById(request.getIdPlan());

        if(StringUtils.isNotBlank(request.getNombre())) {
            usuario.setNombre(request.getNombre());
        }
        if(StringUtils.isNotBlank(request.getPassword())) {
            usuario.setPassword(request.getPassword());
        }
        if(StringUtils.isNotBlank(request.getCuentaBancaria())) {
            usuario.setCuentaBancaria(request.getCuentaBancaria());
        }   
        if(request.getIdPlan() != null) {
            usuario.setPlan(plan);
        }

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void delete(int idUsuario) {
        Usuario usuario = getUsuarioById(idUsuario);
        usuarioRepository.delete(usuario);
    }

    private Usuario getUsuarioById(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "No existe el usuario con id " + idUsuario));
    }

    private Plan getPlanById(int idPlan) {
        return planRepository.findById(idPlan)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "No existe el plan con id " + idPlan));
    }

    private UsuarioResponseDto toResponse(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCuentaBancaria(),
                usuario.getPlan().getIdPlan());
    }

}
