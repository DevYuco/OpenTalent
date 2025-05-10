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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import opentalent.dto.ProyectoAdminDto;
import opentalent.entidades.Proyecto;
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
@Tag(name = "07 - Admin - Proyectos", description = "Endpoints para la gestión de proyectos desde el perfil administrador")
public class AdminProyectoController {

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

    // Obtener todos los proyectos
    @Operation(
        summary = "Obtener todos los proyectos",
        description = "Recupera una lista de todos los proyectos disponibles en la plataforma desde el panel de administración."
    )
    @GetMapping("/proyectos")
    public ResponseEntity<?> obtenerTodosProyectos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        List<Proyecto> proyectos = proyectoService.buscarTodos();
        List<ProyectoAdminDto> listaDto = new ArrayList<>();

        for (Proyecto proyecto : proyectos) {
            ProyectoAdminDto dto = ProyectoAdminDto.builder()
                .idProyecto(proyecto.getIdProyecto())
                .nombre(proyecto.getNombre())
                .descripcion(proyecto.getDescripcion())
                .fechaInicio(proyecto.getFechaInicio())
                .fechaFin(proyecto.getFechaFin())
                .plazas(proyecto.getPlazas())
                .activo(proyecto.isActivo())
                .build();

            listaDto.add(dto);
        }

        return ResponseEntity.ok(listaDto);
    }

    // Eliminar un proyecto por ID
    @Operation(
        summary = "Eliminar un proyecto",
        description = "Elimina un proyecto específico por su ID desde el panel del administrador."
    )
    @DeleteMapping("/proyectos/{id}")
    public ResponseEntity<?> eliminarProyecto(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        Proyecto proyecto = proyectoService.buscarUno(id);
        if (proyecto != null) {
            proyectoService.elimnarUno(id);
            return ResponseEntity.ok("Proyecto eliminado correctamente.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proyecto no encontrado.");
    }

    // Cambiar el estado de un proyecto (activo/inactivo)
    @Operation(
        summary = "Cambiar estado de un proyecto",
        description = "Cambia el estado de un proyecto (activo/inactivo) alternando su valor actual por el opuesto."
    )
    @PutMapping("/proyectos/{id}/estado")
    public ResponseEntity<?> cambiarEstadoProyecto(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        Proyecto proyecto = proyectoService.buscarUno(id);
        if (proyecto != null) {
            proyecto.setActivo(!proyecto.isActivo());
            proyectoService.modificarUno(proyecto);
            return ResponseEntity.ok("Estado del proyecto actualizado correctamente.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proyecto no encontrado.");
    }
}
