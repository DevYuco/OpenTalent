package opentalent.restcontroller.usuario;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.EstadoCandidatoDto;
import opentalent.dto.ProyectoDetallesVistaDto;
import opentalent.dto.ProyectosVistaDto;
import opentalent.dto.UsuarioDto;
import opentalent.dto.UsuarioVistaDetalleProyectoDto;
import opentalent.entidades.EstadoAplicacion;
import opentalent.entidades.Proyecto;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioProyecto;
import opentalent.service.ProyectoService;
import opentalent.service.UsuarioProyectoService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/usuario/proyectos")
@CrossOrigin(origins = "*")
@Tag(name = "Usuario - Proyectos", description = "Endpoints para gestionar proyectos desde el perfil usuario")
public class ProyectoUsuarioControler {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioProyectoService usuarioProyectoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;
    
    @Operation(summary = "Listar proyectos activos", description = "Devuelve todos los proyectos disponibles y activos")
    @GetMapping("/")
    public ResponseEntity<List<ProyectosVistaDto>> todosProyectos() {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	List<Proyecto> proyectos = proyectoService.buscarTodosActivos(); 
    	
    	List<ProyectosVistaDto> proyectosDto = proyectos.stream()
    			.map(p -> {
    				ProyectosVistaDto dto = modelMapper.map(p, ProyectosVistaDto.class); 
    				boolean favorito = usuarioProyectoService.esFavorita(username, p.getIdProyecto()); 
    				dto.setEsFavorito(favorito);
    				int aceptados = usuarioProyectoService.contarInscritosPorProyecto(p.getIdProyecto()); 
    				int plazas = p.getPlazas() - aceptados; 
    				dto.setPlazasDisponibles(plazas); 
    				return dto; 
    			})
				.toList(); 
    	
    	return ResponseEntity.ok(proyectosDto);
    }

    @Operation(summary = "Detalles de proyecto", description = "Devuelve los detalles de un proyecto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDetallesVistaDto> detallesProyecto(@PathVariable int id) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	Proyecto proyecto = proyectoService.buscarUno(id); 
    	Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
    	UsuarioProyecto usuarioProyecto = usuarioProyectoService.findByUsernameAndIdProyecto(username, id); 
    	
    	if(proyecto == null || usuario == null) {
    		return ResponseEntity.notFound().build();
    	}

    	int aceptados = usuarioProyectoService.contarInscritosPorProyecto(id); 
        int vacantesDisponibles = proyecto.getPlazas() - aceptados;
        // Participantes aceptados
        List<UsuarioVistaDetalleProyectoDto> participantes = usuarioProyectoService.findUsuariosAceptadosByProyecto(id);

        ProyectoDetallesVistaDto dto = ProyectoDetallesVistaDto.builder()
            .idProyecto(proyecto.getIdProyecto())
        	//propietario
        	.nombreUsuario(usuario.getNombre())
            .apellidos(usuario.getApellidos())
            .fotoUsuario(usuario.getFotoPerfil())
            //Proyecto
            .fechaInicio(proyecto.getFechaInicio())
            .titulo(proyecto.getNombre())
            .foto(proyecto.getFoto())
            .fotoContenido(proyecto.getFotoContenido())
            .descripcion(proyecto.getDescripcion())
            .plazasRestantes(vacantesDisponibles)
            .esFavorito(usuarioProyecto != null && usuarioProyecto.isFavorito())
            //otros datos
            .estadoAplicacion(usuarioProyecto != null ? usuarioProyecto.getEstado().name() : "NO_APLICADO")
            //participantes
            .participantes(participantes)
            .build();

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Proyectos favoritos", description = "Obtiene los proyectos activos marcados como favoritos por el usuario")
    @GetMapping("/favoritos")
    public ResponseEntity<List<ProyectosVistaDto>> proyectosFavoritosActivosPorUsername() {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	List<Proyecto> proyectos = usuarioProyectoService.buscarProyectosFavsActivosPorUsername(username);
    	List<ProyectosVistaDto> proyectosDto = proyectos.stream()
    			.map(p -> {
    				ProyectosVistaDto dto = modelMapper.map(p, ProyectosVistaDto.class); 
    				boolean favorito = usuarioProyectoService.esFavorita(username, p.getIdProyecto()); 
    				dto.setEsFavorito(favorito);
    				int aceptados = usuarioProyectoService.contarInscritosPorProyecto(p.getIdProyecto()); 
    				int plazas = p.getPlazas() - aceptados; 
    				dto.setPlazasDisponibles(plazas); 
    				return dto; 
    			})
				.toList(); 
    	
    	return ResponseEntity.ok(proyectosDto);
    }



    @Operation(summary = "Mis proyectos activos", description = "Devuelve los proyectos activos donde el usuario es propietario")
    @GetMapping("/mis-proyectos")
    public ResponseEntity<List<Proyecto>> buscarProyectosPropietarioYActivos() {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	return ResponseEntity.ok(usuarioProyectoService.buscarProyectosPropietarioYActivo(username));
    }
    
    @Operation(
    	    summary = "Editar un proyecto",
    	    description = "Modifica los datos de un proyecto existente. "
    	                + "Solo el propietario autenticado puede editarlo."
    	)
    @ApiResponse(responseCode = "200", description = "Proyecto editado correctamente")
    @ApiResponse(responseCode = "403", description = "El usuario no es propietario del proyecto")
    @ApiResponse(responseCode = "404", description = "Proyecto no proporcionado")
    @PostMapping("/")
    public ResponseEntity<Proyecto> editarProyecto(@RequestBody Proyecto proyecto) {
        if (proyecto == null) {
            return ResponseEntity.notFound().build();
        }

        // Extraer el username del usuario autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Verificar si es propietario del proyecto
        if (!usuarioProyectoService.esPropietarioDelProyecto(username, proyecto.getIdProyecto())) {
            return ResponseEntity.status(403).build();
        }

        // Marcar el proyecto como activo (siempre)
        proyecto.setActivo(true);

        // Guardar los cambios
        return ResponseEntity.ok(proyectoService.modificarUno(proyecto));
    }

    @Operation(summary = "Postulantes a un proyecto", description = "Devuelve la lista de usuarios que se han postulado a un proyecto y están pendientes")
    @GetMapping("/{idProyecto}/postulantes")
    public ResponseEntity<List<UsuarioDto>> postulantesDeProyectoActivo(@PathVariable int idProyecto) {
        List<Usuario> usuarios = usuarioProyectoService.postulantesPendientes(idProyecto);
        
        List<UsuarioDto> dtos = usuarios.stream().map(usuario -> {
            UsuarioDto dto = modelMapper.map(usuario, UsuarioDto.class);
            if (usuario.getRol() != null) dto.setRol(usuario.getRol().getNombre());
            return dto;
        }).toList();
        
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Responder solicitud de proyecto", description = "Permite aceptar o rechazar una solicitud de participación de un usuario en un proyecto. Solo el propietario del proyecto puede realizar esta acción.")
    @PostMapping("/responder-solicitud")
    public ResponseEntity<Integer> modificarEstadoSolicitud(@RequestBody EstadoCandidatoDto dto) {
        
        // Validación de entrada
        if (dto.getEstado() == null || 
            dto.getIdProyecto() == null || 
            dto.getIdUsuario() == null || 
            !(dto.getEstado() == EstadoAplicacion.ACEPTADO || dto.getEstado() == EstadoAplicacion.RECHAZADO)) {
            return ResponseEntity.notFound().build();
        }

        // Seguridad: comprobar que el usuario autenticado es el propietario del proyecto
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println();
        if (!usuarioProyectoService.esPropietarioDelProyecto(username, dto.getIdProyecto())) {
            return ResponseEntity.status(403).body(0); // No autorizado
        }

        // Si se acepta la solicitud, marcar como favorito
        if (dto.getEstado() == EstadoAplicacion.ACEPTADO) {
            Usuario usuario = usuarioService.buscarUno(dto.getIdUsuario());
            usuarioProyectoService.añadirProyectoFavoritos(usuario.getUsername(), dto.getIdProyecto());
        }

        // Actualizar el estado
        return ResponseEntity.ok(
            usuarioProyectoService.modificarEstadoProyecto(dto.getEstado(), dto.getIdUsuario(), dto.getIdProyecto())
        );
    }

    @Operation(
    	    summary = "Cancelar proyecto",
    	    description = "Permite a un usuario con rol USUARIO cancelar su propio proyecto. "
    	                + "Esto marca el proyecto como inactivo y rechaza todas las solicitudes asociadas."
    	)
    @ApiResponse(responseCode = "200", description = "Proyecto cancelado correctamente")
    @ApiResponse(responseCode = "403", description = "El usuario no es propietario del proyecto")
    @PostMapping("/{idProyecto}/cancelar")
    public ResponseEntity<Integer> cancelarProyecto(@PathVariable int idProyecto) {
        // Obtener el username del usuario autenticado desde el JWT
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validar que el usuario es el propietario del proyecto
        if (!usuarioProyectoService.esPropietarioDelProyecto(username, idProyecto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(0);
        }

        // Rechazar todas las solicitudes pendientes asociadas al proyecto
        usuarioProyectoService.rechazarSolicitudesPendientesPorProyecto(idProyecto);

        // Cancelar el proyecto (marcar como inactivo)
        return ResponseEntity.ok(proyectoService.cancelarProyecto(idProyecto));
    }
    
    
}
