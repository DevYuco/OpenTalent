package opentalent.restcontroller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import opentalent.dto.ResennaAdminDto;
import opentalent.entidades.Resenna;
import opentalent.entidades.Usuario;
import opentalent.service.DireccionService;
import opentalent.service.EmpresaService;
import opentalent.service.OfertaService;
import opentalent.service.ProyectoService;
import opentalent.service.ResennaService;
import opentalent.service.SectorService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Tag(name = "03 - Admin - Reseñas", description = "Endpoints para la gestión de reseñas desde el perfil administrador")
public class AdminResennaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ResennaService resennaService;

    @Autowired 
    private OfertaService ofertaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private DireccionService direccionService;

    // Ver todas las reseñas
    @Operation(
        summary = "Obtener todas las reseñas",
        description = "Recupera todas las reseñas registradas en la plataforma, visibles solo para administradores."
    )
    @GetMapping("/resennas")
    public ResponseEntity<?> obtenerTodasResennas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        List<Resenna> resennas = resennaService.buscarTodos();
        List<ResennaAdminDto> listaDto = new ArrayList<>();

        for (Resenna r : resennas) {
            ResennaAdminDto dto = ResennaAdminDto.builder()
                .idResenna(r.getIdResenna())
                .titulo(r.getTitulo())
                .comentario(r.getComentario())
                .puntuacion(r.getPuntuacion())
                .nombreUsuario(r.getUsuario() != null ? r.getUsuario().getNombre() : null)
                .nombreProyecto(r.getProyecto() != null ? r.getProyecto().getNombre() : null)
                .build();

            listaDto.add(dto);
        }

        return ResponseEntity.ok(listaDto);
    }

    // Eliminar una reseña
    @Operation(
        summary = "Eliminar una reseña",
        description = "Elimina una reseña específica por su ID desde el panel de administración."
    )
    @DeleteMapping("/resennas/{id}")
    public ResponseEntity<?> eliminarResenna(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        if (resennaService.buscarUno(id) != null) {
            resennaService.elimnarUno(id);
            return ResponseEntity.ok("Reseña eliminada correctamente.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reseña no encontrada.");
    }
}
