// package application.controller;
// import org.springframework.web.bind.annotation.RestController;

// import java.net.URI;
// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// @RestController
// @RequestMapping("/usuarios")
// @Validated
// public class SerieController {

//     private final SerieService serieService;

//     public SerieController(SerieService serieService) {
//         this.serieService = serieService;
//     }

//     @GetMapping
//     public List<SerieResponseDto> getSeries() {
//         return serieService.findAll();
//     }

//     @GetMapping("/{id}")
//     public UsuarioResponseDto getUsuarioById(@PathVariable int id) {
//         UsuarioResponseDto usuario = usuarioService.findById(id);
//         return usuario;
//     }

//     @PostMapping
//     public ResponseEntity<UsuarioResponseDto> createUsuario(@Valid @RequestBody UsuarioRequestDto request) {
//         UsuarioResponseDto createdUsuario = usuarioService.create(request);
//         return ResponseEntity.created(URI.create("/usuarios/" + createdUsuario.getIdUsuario())).body(createdUsuario);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable int id, @Valid @RequestBody UsuarioUpdateDto request) {
//         UsuarioResponseDto updatedUsuario = usuarioService.update(id, request);
//         return ResponseEntity.ok(updatedUsuario);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteUsuario(@PathVariable int id) {
//         usuarioService.delete(id);
//         return ResponseEntity.ok().build();
//     }
// }