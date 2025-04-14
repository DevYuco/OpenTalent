package opentalent.restcontroller;

import java.math.BigDecimal;
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
import opentalent.dto.CambiarFavoritoDto;
import opentalent.dto.EmpresaDto;
import opentalent.dto.EmpresaHomeUsuarioDto;
import opentalent.dto.EstadoCandidatoDto;
import opentalent.dto.OfertaDetallesEmpresaDto;
import opentalent.dto.OfertaDetallesVistaDto;
import opentalent.dto.OfertaTodasDto;
import opentalent.dto.ProyectoDetallesVistaDto;
import opentalent.dto.ProyectosVistaDto;
import opentalent.dto.ResennaDetallesEmpresaDto;
import opentalent.dto.ResennaDto;
import opentalent.dto.UsuarioDto;
import opentalent.dto.UsuarioVistaDetalleProyectoDto;
import opentalent.entidades.Empresa;
import opentalent.entidades.EstadoAplicacion;
import opentalent.entidades.Oferta;
import opentalent.entidades.Proyecto;
import opentalent.entidades.Resenna;
import opentalent.entidades.Usuario;
import opentalent.entidades.UsuarioOferta;
import opentalent.entidades.UsuarioProyecto;
import opentalent.entidades.Valoracion;
import opentalent.service.EmpresaService;
import opentalent.service.OfertaService;
import opentalent.service.ProyectoService;
import opentalent.service.ResennaService;
import opentalent.service.UsuarioOfertaService;
import opentalent.service.UsuarioProyectoService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioOfertaService usuarioOfertaService;

    @Autowired
    private UsuarioProyectoService usuarioProyectoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResennaService resennaService;

    @Operation(summary = "Empresas destacadas", description = "Devuelve una lista de empresas activas con el flag 'destacado' activado")
    @ApiResponse(responseCode = "200", description = "Listado de empresas exitoso")
    @GetMapping("/home")
    public ResponseEntity<List<EmpresaHomeUsuarioDto>> home() {
    	List<Empresa> empresas = empresaService.findByDestacadoYActivo(); 
    	
    	List<EmpresaHomeUsuarioDto> empresasDto = empresas.stream()
    		    .map(emp -> modelMapper.map(emp, EmpresaHomeUsuarioDto.class))
    		    .toList();

        return ResponseEntity.ok(empresasDto);
    }

    @Operation(summary = "Detalles de empresa", description = "Obtiene los datos de una empresa a partir de su CIF")
    @GetMapping("/detallesempresa/{cif}")
    public ResponseEntity<EmpresaDto> detallesEmpresa(@PathVariable String cif) {
        Empresa empresa = empresaService.buscarUno(cif);
        
        if (empresa == null) {
        	return ResponseEntity.notFound().build();
        }
        
        // Mapear los datos básicos de la empresa
        EmpresaDto empresaDto = modelMapper.map(empresa, EmpresaDto.class);
        // Obtener ofertas activas asociadas a la empresa
        List<Oferta> ofertas = ofertaService.findActivasByEmpresaCif(cif);
        // Obtener reseñas de la empresa
        List<Resenna> resennas = resennaService.findByEmpresaCif(cif);
        //Calcular inscritos a las ofertas de una empresa
        int inscritos = usuarioOfertaService.contarInscritosPorEmpresa(cif);
        // Mapear solo los campos necesarios de cada oferta
        List<OfertaDetallesEmpresaDto> ofertasDto = ofertas.stream()
            .map(o -> modelMapper.map(o, OfertaDetallesEmpresaDto.class))
            .toList();
        // Mapear dtos necesarios de las resennas
        List<ResennaDetallesEmpresaDto> resennasDto = resennas.stream()
        	    .map(r -> modelMapper.map(r,ResennaDetallesEmpresaDto.class))
        	    .toList();
        //insertar ofertas y resennas
        empresaDto.setOfertas(ofertasDto);
        empresaDto.setResennas(resennasDto);
        empresaDto.setNumeroInscritos(inscritos);
        
        return ResponseEntity.ok(empresaDto);
    }

    @Operation(summary = "Detalles de oferta", description = "Devuelve los datos de una oferta específica")
    @GetMapping("/detallesoferta/{id}")
    public ResponseEntity<OfertaDetallesVistaDto> detallesOferta(@PathVariable Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();   
    	
    	Oferta oferta = ofertaService.buscarUno(id);
    	
    	//Verificar oferta
    	if (oferta == null) {
    		return ResponseEntity.notFound().build();
    	}
    	
    	Empresa empresa = oferta.getEmpresa();

        // Obtener info de la tabla intermedia UsuarioOferta
        UsuarioOferta usuarioOferta = usuarioOfertaService.findByUsernameAndIdOferta(username, id);
        //Calcular vacantes
        int aceptados = usuarioOfertaService.contarAceptadosPorOferta(id);
        int vacantesDisponibles = oferta.getNumeroPlazas() - aceptados;
        

        OfertaDetallesVistaDto dto = OfertaDetallesVistaDto.builder()
            .idOferta(oferta.getIdOferta())
            .titulo(oferta.getTitulo())
            .descripcion(oferta.getDescripcion())
            .modalidad(oferta.getModalidad() != null ? oferta.getModalidad().name() : null)
            .imagenOferta(oferta.getFotoContenido())
            .nombreEmpresa(empresa.getNombreEmpresa())
            .fotoEmpresa(empresa.getFoto())
            .direccionEmpresa(empresa.getDireccion())
            .estadoAplicacion(usuarioOferta != null ? usuarioOferta.getEstado().name() : "NO_APLICADO")
            .esFavorita(usuarioOferta != null && usuarioOferta.isFavorito())
            .vacantesDisponibles(vacantesDisponibles)
            .build();

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar ofertas activas", description = "Devuelve todas las ofertas activas con info resumida y favoritas del usuario")
    @GetMapping("/ofertas")
    public ResponseEntity<List<OfertaTodasDto>> todasOfertas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Oferta> ofertas = ofertaService.buscarOfertasActivas();

        List<OfertaTodasDto> dtoList = ofertas.stream()
        	    .map(oferta -> {
        	        boolean favorita = usuarioOfertaService.esFavorita(username, oferta.getIdOferta());

        	        return OfertaTodasDto.builder()
        	            .idOfeta(oferta.getIdOferta())
        	            .fotoContenido(oferta.getFotoContenido())
        	            .titulo(oferta.getTitulo())
        	            .descripcion(oferta.getDescripcion())
        	            .foto(oferta.getEmpresa() != null ? oferta.getEmpresa().getFoto() : null)
        	            .direccion(oferta.getEmpresa() != null ? oferta.getEmpresa().getDireccion() : null)
        	            .esFavorita(favorita)
        	            .build();
        	    })
        	    .toList();

        return ResponseEntity.ok(dtoList);
    }


    @Operation(summary = "Listar proyectos activos", description = "Devuelve todos los proyectos disponibles y activos")
    @GetMapping("/proyectos")
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
    @GetMapping("/detallesproyecto/{id}")
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

    @Operation(summary = "Añadir o quitar oferta de favoritos", description = "Marca o desmarca una oferta como favorita para un usuario")
    @GetMapping("/añadirofertafav/{username}/{idOferta}")
    public ResponseEntity<Boolean> añadirOfertaFavoritos(@PathVariable String username, @PathVariable int idOferta) {
        if (usuarioOfertaService.comprobarFavorito(username, idOferta)) {
            usuarioOfertaService.eliminarOfertaFavoritos(username, idOferta);
            return ResponseEntity.ok(false);
        } else {
            usuarioOfertaService.añadirOfertaFavoritos(username, idOferta);
            return ResponseEntity.ok(true);
        }
    }

    @Operation(summary = "Añadir o quitar proyecto de favoritos", description = "Marca o desmarca un proyecto como favorito para un usuario")
    @GetMapping("/añadirproyectofav/{username}/{idProyecto}")
    public ResponseEntity<Boolean> añadirProyectoFavoritos(@PathVariable String username, @PathVariable int idProyecto) {
        if (usuarioProyectoService.comprobarFavorito(username, idProyecto)) {
            usuarioProyectoService.quitarProyectoFavoritos(username, idProyecto);
            return ResponseEntity.ok(false);
        } else {
            usuarioProyectoService.añadirProyectoFavoritos(username, idProyecto);
            return ResponseEntity.ok(true);
        }
    }

    @Operation(summary = "Añadir reseña a empresa", description = "Permite a un usuario publicar una reseña hacia una empresa con valoración y comentario")
    @PostMapping("/addresenna")
    public ResponseEntity<Boolean> añadirReseña(@RequestBody ResennaDto resennaDto) {
        Usuario usuario = usuarioService.buscarPorUsernameEntidad(resennaDto.getUsername());
        Empresa empresa = empresaService.buscarUno(resennaDto.getCif());
        Valoracion valoracionEnum;
        
        if (usuario == null || empresa == null) {
        	return ResponseEntity.notFound().build();
        }
        
        BigDecimal puntuacion = resennaDto.getPuntuacion();
        
        if (puntuacion == null) {
        	return ResponseEntity.badRequest().body(false);
        }
       
        if (puntuacion.compareTo(BigDecimal.valueOf(3)) < 0) {
        	valoracionEnum = Valoracion.NEGATIVA;
        }else if (puntuacion.compareTo(BigDecimal.valueOf(3)) == 0) {
        	valoracionEnum = Valoracion.NEUTRAL;
        }else {
        	valoracionEnum = Valoracion.POSITIVA;
        }

        Resenna resenna = Resenna.builder()
        	    .titulo(resennaDto.getTitulo())
        	    .comentario(resennaDto.getComentario())
        	    .valoracion(valoracionEnum)
        	    .puntuacion(puntuacion)
        	    .usuario(usuario)
        	    .empresa(empresa)
        	    .build(); 
        
        resennaService.insertUno(resenna);
        
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Proyectos favoritos", description = "Obtiene los proyectos activos marcados como favoritos por el usuario")
    @GetMapping("/proyectosfavs")
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

    @Operation(summary = "Ofertas favoritas", description = "Obtiene las ofertas activas marcadas como favoritas por el usuario")
    @GetMapping("/ofertasfavs")
    public ResponseEntity<List<OfertaTodasDto>> ofertasFavoritosActivasPorUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Oferta> ofertas = usuarioOfertaService.buscarOfertasFavsActivasPorUsername(username);

        List<OfertaTodasDto> dtoList = ofertas.stream()
        	    .map(oferta -> {
        	        boolean favorita = usuarioOfertaService.esFavorita(username, oferta.getIdOferta());

        	        return OfertaTodasDto.builder()
        	            .idOfeta(oferta.getIdOferta())
        	            .fotoContenido(oferta.getFotoContenido())
        	            .titulo(oferta.getTitulo())
        	            .descripcion(oferta.getDescripcion())
        	            .foto(oferta.getEmpresa() != null ? oferta.getEmpresa().getFoto() : null)
        	            .direccion(oferta.getEmpresa() != null ? oferta.getEmpresa().getDireccion() : null)
        	            .esFavorita(favorita)
        	            .build();
        	    })
        	    .toList();

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Cambiar estado de favorito", description = "Modifica el valor booleano de una oferta marcada como favorita para un usuario")
    @PostMapping("/cambiarestadofavorito")
    public ResponseEntity<Integer> cambiarEstadoFavoritoUsername(@RequestBody CambiarFavoritoDto dto) {
        return ResponseEntity.ok(usuarioOfertaService.cambiarEstadoFavorito(dto.isEstado(), dto.getUsername(), dto.getIdOferta()));
    }

    @Operation(summary = "Mis proyectos activos", description = "Devuelve los proyectos activos donde el usuario es propietario")
    @GetMapping("/misproyectos/{username}")
    public ResponseEntity<List<Proyecto>> buscarProyectosPropietarioYActivos(@PathVariable String username) {
        
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
    @PostMapping("/editarproyecto")
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
    @GetMapping("/postulantes/{idProyecto}")
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
    @PostMapping("/respondersolicitud")
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
    @PostMapping("/cancelarproyecto/{idProyecto}")
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
