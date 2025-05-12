package opentalent.restcontroller.empresa;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.AnnadirOfertaDto;
import opentalent.dto.EmpresaVerDetallesOfertaDto;
import opentalent.dto.OfertasUsuarioEmpresaDto;
import opentalent.dto.UsuarioDto;
import opentalent.entidades.Empresa;
import opentalent.entidades.EstadoOferta;
import opentalent.entidades.Oferta;
import opentalent.entidades.Sector;
import opentalent.entidades.Usuario;
import opentalent.service.OfertaService;
import opentalent.service.SectorService;
import opentalent.service.UsuarioOfertaService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/empresa/ofertas")
@CrossOrigin(origins = "*")
@Tag(name = "08 - Empresa - Ofertas", description = "Endpoints para visualizar todo lo relacionado con las ofertas de empresa")
public class OfertaEmpresaController {
	@Autowired
	private UsuarioService usuarioService;
    
    @Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	private SectorService sectorService;
	
	@Autowired
	private OfertaService ofertaService;

	@Autowired
	private UsuarioOfertaService usuarioOfertaService;
	
	@Operation(
		    summary = "Obtener ofertas activas de la empresa",
		    description = "Devuelve una lista de todas las ofertas activas publicadas por la empresa del usuario autenticado, incluyendo vacantes disponibles."
		)
	@ApiResponse(responseCode = "200", description = "Lista de ofertas obtenida correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario o empresa no encontrada")
	@GetMapping("/")
	public ResponseEntity<?> obtenerOfertasEmpresa() {
	    // 1. Obtener el usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar el usuario
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada."));
	    }

	    // 3. Obtener la empresa
	    Empresa empresa = usuario.getEmpresa();

	    // 4. Obtener ofertas activas de esa empresa
	    List<Oferta> ofertas = ofertaService.findActivasByEmpresaCif(empresa.getCif());
	    
	    // 5. Convertir a DTO incluyendo cálculo de vacantes disponibles
	    List<OfertasUsuarioEmpresaDto> dtoList = ofertas.stream()
	    		.map(oferta -> {
	    			int aceptados = usuarioOfertaService.contarAceptadosPorOferta(oferta.getIdOferta());
	    	        int vacantesDisponibles = oferta.getNumeroPlazas() - aceptados;
	    	        
	    	        return OfertasUsuarioEmpresaDto.builder()
	    	                .idOferta(oferta.getIdOferta())
	    	                .titulo(oferta.getTitulo())
	    	                .descripcion(oferta.getDescripcion())
	    	                .foto(oferta.getFotoContenido())
	    	                .vacantesDisponibles(vacantesDisponibles)
	    	                .build();
	    		}).toList(); 
	   
	    return ResponseEntity.ok(dtoList);
	}
	
	@Operation(
		    summary = "Crear nueva oferta",
		    description = "Permite a una empresa añadir una nueva oferta. El usuario debe tener una empresa asociada y se debe proporcionar un sector válido."
		)
	@ApiResponse(responseCode = "201", description = "Oferta creada correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario o empresa no encontrada, o el sector no existe")
	@PostMapping("/")
	public ResponseEntity<?> annadirOferta(@RequestBody AnnadirOfertaDto dto) {

	    // Obtener el usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();


	    // Buscar usuario y validar que tiene empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada."));
	    }

	    Empresa empresa = usuario.getEmpresa();

	 // Obtener sector por ID (validar que exista)
	    Sector sector = sectorService.findByName(dto.getNombreSector());
	    if (sector == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Mensaje", "Sector no encontrado"));
	    }

	    // Crear la nueva oferta
	    Oferta oferta = Oferta.builder()
	            .titulo(dto.getTitulo())
	            .descripcion(dto.getDescripcion())
	            .modalidad(dto.getModalidad())
	            .tipoOferta(dto.getTipoOferta())
	            .estado(EstadoOferta.ACTIVA) // por defecto
	            .numeroPlazas(dto.getNumeroPlazas())
	            .fotoContenido(dto.getFotoContenido())
	            .empresa(empresa)
	            .sector(sector)
	            .fechaInicio(LocalDate.now())
	            .fechaFin(dto.getFechaFin())
	            .build();

	    // Guardar la oferta
	    ofertaService.insertUno(oferta);

	    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje","Oferta creada correctamente."));
	}
	
	@Operation(
		    summary = "Editar oferta",
		    description = "Permite a un usuario con cuenta de empresa editar una de sus ofertas activas. " +
		                  "Verifica que la oferta pertenezca a la empresa y que el sector proporcionado exista."
		)
	@ApiResponse(responseCode = "200", description = "Oferta actualizada correctamente")
	@ApiResponse(responseCode = "403", description = "El usuario no tiene permiso para editar esta oferta")
	@ApiResponse(responseCode = "404", description = "Usuario, empresa, oferta o sector no encontrado")
	@PutMapping("/{id}")
	public ResponseEntity<?> editarOferta(@PathVariable int id, @RequestBody AnnadirOfertaDto dto) {
	    // 1. Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar usuario y validar empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada"));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Buscar oferta
	    Oferta oferta = ofertaService.buscarUno(id);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Oferta no encontrada"));
	    }

	    // 4. Verificar propiedad
	    if (!oferta.getEmpresa().getCif().equals(empresa.getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes permiso para editar esta oferta"));
	    }

	 // 5. Obtener sector
	    Sector sector = sectorService.findByName(dto.getNombreSector());
	    if (sector == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Mensaje","Sector no encontrado"));
	    }

	    oferta.setTitulo(dto.getTitulo());
	    oferta.setDescripcion(dto.getDescripcion());
	    oferta.setModalidad(dto.getModalidad());
	    oferta.setTipoOferta(dto.getTipoOferta());
	    oferta.setNumeroPlazas(dto.getNumeroPlazas());
	    oferta.setFotoContenido(dto.getFotoContenido());
	    oferta.setSector(sector);
	    oferta.setFechaFin(dto.getFechaFin());

	    ofertaService.modificarUno(oferta);

	    return ResponseEntity.ok("Oferta actualizada correctamente");
	}

	@Operation(
		    summary = "Obtener detalles de una oferta de empresa",
		    description = "Devuelve los detalles completos de una oferta específica publicada por la empresa del usuario autenticado. Verifica que la oferta pertenezca a la empresa."
		)
	@ApiResponse(responseCode = "200", description = "Detalles de la oferta obtenidos correctamente")
	@ApiResponse(responseCode = "403", description = "La oferta no pertenece a la empresa del usuario")
	@ApiResponse(responseCode = "404", description = "Usuario, empresa u oferta no encontrada")
	@GetMapping("/{id}")
	public ResponseEntity<?> obtenerDetalleOferta(@PathVariable int id) {
	    // 1. Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar el usuario y su empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada"));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Buscar la oferta
	    Oferta oferta = ofertaService.buscarUno(id);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Oferta no encontrada"));
	    }

	    // 4. Verificar que la oferta pertenece a la empresa
	    if (!oferta.getEmpresa().getCif().equals(empresa.getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes permiso para ver esta oferta"));
	    }
	    // Calcular vacantes
	    int aceptados = usuarioOfertaService.contarAceptadosPorOferta(oferta.getIdOferta());
        int vacantesDisponibles = oferta.getNumeroPlazas() - aceptados;
        
	    // 5. Mapear a DTO
	    EmpresaVerDetallesOfertaDto detallesDto = EmpresaVerDetallesOfertaDto.builder()
	    		.idOferta(oferta.getIdOferta())
	    		.titulo(oferta.getTitulo())
	    		.descripcion(oferta.getDescripcion())
	    		.foto(oferta.getFotoContenido())
	    		.vacantesDisponibles(vacantesDisponibles)
	    		.fotoEmpresa(empresa.getFoto())
	    		.nombreEmpresa(empresa.getNombreEmpresa())
	    		.direccion(empresa.getDireccion())
	    		.modalidad(oferta.getModalidad())
	    		.build(); 
	    
	    return ResponseEntity.ok(detallesDto);
	}
	
	@Operation(
		    summary = "Obtener postulantes de una oferta",
		    description = "Devuelve una lista de usuarios que se han postulado a una oferta específica de la empresa autenticada. Solo se muestran postulantes pendientes."
		)
	@ApiResponse(responseCode = "200", description = "Lista de postulantes obtenida correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario, empresa o oferta no encontrada")
	@ApiResponse(responseCode = "403", description = "La oferta no pertenece a la empresa del usuario autenticado")
	@GetMapping("/{id}/postulantes")
	public ResponseEntity<?> obtenerPostulantesDeOferta(@PathVariable int id) {
	    // 1. Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar usuario y validar empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada"));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Buscar la oferta
	    Oferta oferta = ofertaService.buscarUno(id);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Oferta no encontrada"));
	    }

	    // 4. Verificar que la oferta pertenece a la empresa autenticada
	    if (!oferta.getEmpresa().getCif().equals(empresa.getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes permiso para ver esta información"));
	    }

	    // 5. Obtener postulantes
	    List<Usuario> postulantes = usuarioOfertaService.postulantesPendientes(id);

	    // 6. Mappear dto
	    List<UsuarioDto> dtoList = postulantes.stream()
	    		.map(u -> {
	    			UsuarioDto dto = modelMapper.map(u, UsuarioDto.class); 
	    			if(usuario.getRol() != null) {
	    				dto.setRol(u.getRol().getNombre()); 
	    			}
	    			return dto; 
	    		}).toList(); 
	    
	    return ResponseEntity.ok(dtoList);
	}
	
	@Operation(
		    summary = "Cerrar oferta",
		    description = "Permite a una empresa cerrar una oferta publicada. Cambia el estado de la oferta a 'CERRADA' y marca todas las postulaciones relacionadas como cerradas."
		)
	@ApiResponse(responseCode = "200", description = "Oferta cerrada correctamente y postulaciones actualizadas")
	@ApiResponse(responseCode = "404", description = "Usuario, empresa o oferta no encontrada")
	@ApiResponse(responseCode = "403", description = "La oferta no pertenece a la empresa del usuario autenticado")
	@PutMapping("/{id}/cerrar")
	public ResponseEntity<?> cerrarOferta(@PathVariable int id) {
	    // 1. Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar usuario y empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada"));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Buscar oferta
	    Oferta oferta = ofertaService.buscarUno(id);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Oferta no encontrada"));
	    }

	    // 4. Verificar que la oferta pertenece a su empresa
	    if (!oferta.getEmpresa().getCif().equals(empresa.getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes permiso para cerrar esta oferta"));
	    }

	    // 5. Cambiar el estado a CERRADA
	    oferta.setEstado(EstadoOferta.CERRADA);
	    ofertaService.modificarUno(oferta);
	    
	    //6. Cerrar todas las postulaciones relacionadas(tabla usuario_oferta)
	    int actualizados = usuarioOfertaService.cerrarPostulacionesPorOferta(id);
	    
	    //7. Confirmar el resultado
	    return ResponseEntity.ok(Map.of("mensaje","Oferta cerrada correctamente. Se actualizaron " + actualizados + " postulaciones."));  
	}
	
	@Operation(
		    summary = "Aceptar postulante a una oferta",
		    description = "Permite al responsable de una empresa aceptar la solicitud de un usuario a una oferta. " +
		                  "Solo se puede aceptar si la oferta pertenece a la empresa autenticada."
		)
	@ApiResponse(responseCode = "200", description = "Postulante aceptado correctamente")
	@ApiResponse(responseCode = "400", description = "No se pudo aceptar la solicitud")
	@ApiResponse(responseCode = "403", description = "El usuario no tiene permiso sobre esta oferta")
	@ApiResponse(responseCode = "404", description = "Usuario, empresa u oferta no encontrada")
	@PutMapping("/{idOferta}/postulantes/{idUsuario}/aceptar")
	public ResponseEntity<?> aceptarPostulante(@PathVariable int idOferta, @PathVariable int idUsuario) {
	    // 1. Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar usuario y su empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada"));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Buscar la oferta
	    Oferta oferta = ofertaService.buscarUno(idOferta);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Oferta no encontrada"));
	    }

	    // 4. Verificar que la oferta pertenece a la empresa del usuario autenticado
	    if (!oferta.getEmpresa().getCif().equals(empresa.getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes permiso para esta operación"));
	    }

	    // 5. Aceptar postulante (actualiza estado en la tabla usuario_oferta)
	    int actualizados = usuarioOfertaService.aceptarSolicitud(idOferta, idUsuario);

	    if (actualizados > 0) {
	        return ResponseEntity.ok(Map.of("mensaje","Postulante aceptado correctamente"));
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje","No se pudo aceptar la solicitud"));
	    }
	}
	
	@Operation(
		    summary = "Rechazar postulante a una oferta",
		    description = "Permite a una empresa rechazar la postulación de un usuario a una de sus ofertas. Verifica que la oferta pertenezca a la empresa autenticada."
		)
	@ApiResponse(responseCode = "200", description = "Postulante rechazado correctamente")
	@ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene permiso para modificar esta oferta")
	@ApiResponse(responseCode = "404", description = "Usuario, empresa, oferta o postulación no encontrada")
	@PutMapping("/{idOferta}/postulantes/{idUsuario}/rechazar")
	public ResponseEntity<?> rechazarPostulante(@PathVariable int idOferta, @PathVariable int idUsuario) {
	    // 1. Obtener usuario autenticado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar usuario y validar empresa
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada"));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Verificar que la oferta existe y pertenece a esta empresa
	    Oferta oferta = ofertaService.buscarUno(idOferta);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Oferta no encontrada"));
	    }

	    if (!oferta.getEmpresa().getCif().equals(empresa.getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje","No tienes permiso para modificar esta oferta"));
	    }

	 // 4. Ejecutar el servicio que actualiza el estado
	    int resultado = usuarioOfertaService.rechazarSolicitud(idOferta, idUsuario);

	    if (resultado == 0) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","No se encontró la postulación"));
	    }

	    return ResponseEntity.ok(Map.of("mensaje","Postulante rechazado correctamente"));
	}
	
	@Operation(
		    summary = "Obtener datos de oferta para edición",
		    description = "Permite a la empresa autenticada obtener los datos de una oferta que desea editar."
		)
	@ApiResponse(responseCode = "200", description = "Datos de oferta obtenidos correctamente")
	@ApiResponse(responseCode = "403", description = "No tienes permiso para acceder a esta oferta")
	@ApiResponse(responseCode = "404", description = "Oferta no encontrada")
	@GetMapping("/{id}/editar")
	public ResponseEntity<?> obtenerOfertaParaEditar(@PathVariable int id) {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);

	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario o empresa no encontrada"));
	    }

	    Oferta oferta = ofertaService.buscarUno(id);
	    if (oferta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Oferta no encontrada"));
	    }

	    if (!oferta.getEmpresa().getCif().equals(usuario.getEmpresa().getCif())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("mensaje", "No tienes permiso para ver esta oferta"));
	    }

	    AnnadirOfertaDto dto = AnnadirOfertaDto.builder()
	            .titulo(oferta.getTitulo())
	            .descripcion(oferta.getDescripcion())
	            .nombreSector(oferta.getSector() != null ? oferta.getSector().getNombre() : null)
	            .fotoContenido(oferta.getFotoContenido())
	            .numeroPlazas(oferta.getNumeroPlazas())
	            .tipoOferta(oferta.getTipoOferta())
	            .modalidad(oferta.getModalidad())
	            .fechaFin(oferta.getFechaFin())
	            .build();

	    return ResponseEntity.ok(dto);
	}
}
