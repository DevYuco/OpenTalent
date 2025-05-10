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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import opentalent.dto.UsuarioAdminDto;
import opentalent.entidades.Direccion;
import opentalent.entidades.Empresa;
import opentalent.entidades.EstadoOferta;
import opentalent.entidades.Oferta;
import opentalent.entidades.Proyecto;
import opentalent.entidades.Resenna;
import opentalent.entidades.Sector;
import opentalent.entidades.Usuario;
import opentalent.service.DireccionService;
import opentalent.service.EmpresaService;
import opentalent.service.OfertaService;
import opentalent.service.ProyectoService;
import opentalent.service.ResennaService;
import opentalent.service.SectorService;
import opentalent.service.UsuarioService;
import opentalent.dto.EmpresaAdminDto;
import opentalent.dto.OfertaAdminDto;
import opentalent.dto.ProyectoAdminDto;
import opentalent.dto.RegistroEmpresaAdminDto;
import opentalent.dto.ResennaAdminDto;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@Tag(name = "02 - Admin - Usuarios", description = "Endpoints para la gestión de usuarios desde el perfil administrador")
public class AdminUsuarioController {

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

    // Obtener todos los usuarios
    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Devuelve una lista con todos los usuarios registrados, accesible solo por administradores."
    )
    @GetMapping("/usuarios")
    public ResponseEntity<?> obtenerTodosUsuarios() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        List<Usuario> usuarios = usuarioService.buscarTodos();
        List<UsuarioAdminDto> listaDto = new ArrayList<>();

        for (Usuario u : usuarios) {
            UsuarioAdminDto dto = UsuarioAdminDto.builder()
                .idUsuario(u.getIdUsuario())
                .nombre(u.getNombre())
                .apellidos(u.getApellidos())
                .email(u.getEmail())
                .activo(u.isActivo())
                .nombreEmpresa(u.getEmpresa() != null ? u.getEmpresa().getNombreEmpresa() : null)
                .nombreRol(u.getRol() != null ? u.getRol().getNombre() : null)
                .build();

            listaDto.add(dto);
        }

        return ResponseEntity.ok(listaDto);
    }

    // Eliminar un usuario
    @Operation(
        summary = "Eliminar un usuario",
        description = "Elimina un usuario del sistema a partir de su ID. Requiere permisos de administrador."
    )
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        int resultado = usuarioService.elimnarUno(id);
        if (resultado == 1) {
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }

    // Cambiar estado de un usuario
    @Operation(
        summary = "Cambiar estado de un usuario",
        description = "Activa o desactiva a un usuario alternando su estado actual. Requiere permisos de administrador."
    )
    @PutMapping("/usuarios/{id}/estado")
    public ResponseEntity<?> cambiarEstadoUsurio(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario authUsuario = usuarioService.buscarPorUsernameEntidad(username);
        if (authUsuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        Usuario usuario = usuarioService.buscarUno(id);
        if (usuario != null) {
            usuario.setActivo(!usuario.isActivo());
            usuarioService.modificarUno(usuario);
            return ResponseEntity.ok("Estado de usuario actualizado correctamente.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }
}
