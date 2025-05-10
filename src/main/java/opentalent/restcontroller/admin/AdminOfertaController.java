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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;

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
@Tag(name = "05 - Admin - Ofertas", description = "Endpoints para la gestión de ofertas desde el perfil administrador")
public class AdminOfertaController {
	
	
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
		
		
		//---Ofertas----
		
		// Obtener todas las ofertas
		@Operation(
			    summary = "Obtener todas las ofertas",
			    description = "Recupera una lista con todas las ofertas disponibles en la plataforma, visible para administradores."
			)

		@GetMapping("/ofertas")
		public ResponseEntity<?> obtenerTodasOfertas() {

		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String username = auth.getName();

		    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		    if (usuario == null) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		    }

		    List<Oferta> ofertas = ofertaService.buscarTodos();
		    List<OfertaAdminDto> listaDto = new ArrayList<>();

		    for (Oferta o : ofertas) {
		        OfertaAdminDto dto = OfertaAdminDto.builder()
		            .idOferta(o.getIdOferta())
		            .titulo(o.getTitulo())
		            .descripcion(o.getDescripcion())
		            .modalidad(o.getModalidad() != null ? o.getModalidad().name() : null)
		            .tipoOferta(o.getTipoOferta() != null ? o.getTipoOferta().name() : null)
		            .estado(o.getEstado() != null ? o.getEstado().name() : null)
		            .numeroPlazas(o.getNumeroPlazas())
		            .nombreEmpresa(o.getEmpresa() != null ? o.getEmpresa().getNombreEmpresa() : null)
		            .nombreSector(o.getSector() != null ? o.getSector().getNombre() : null)
		            .build();

		        listaDto.add(dto);
		    }

		    return ResponseEntity.ok(listaDto);
		}


		// Eliminar una oferta por ID
		@Operation(
		        summary = "Eliminar una oferta",
		        description = "Permite al administrador eliminar una oferta específica por su ID."
		    )
		    @DeleteMapping("/ofertas/{id}")
		    public ResponseEntity<?> eliminarOferta(@PathVariable int id) {
		        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		        String username = auth.getName();

		        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		        if (usuario == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		        }

		        if (ofertaService.buscarUno(id) != null) {
		            ofertaService.elimnarUno(id);
		            return ResponseEntity.ok("Oferta eliminada correctamente.");
		        }

		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta no encontrada.");
		    }

		    // Cambiar el estado de una oferta (ACTIVA, CERRADA, PENDIENTE)
		    @Operation(
		        summary = "Cambiar el estado de una oferta",
		        description = "Actualiza el estado de una oferta específica (ACTIVA, CERRADA, PENDIENTE) por su ID."
		    )
		    @PutMapping("/ofertas/{id}/estado")
		    public ResponseEntity<?> cambiarEstadoOferta(@PathVariable int id, @RequestParam String estado) {
		        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		        String username = auth.getName();

		        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		        if (usuario == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		        }

		        Oferta oferta = ofertaService.buscarUno(id);
		        if (oferta != null) {
		            try {
		                oferta.setEstado(EstadoOferta.valueOf(estado.toUpperCase()));
		                ofertaService.modificarUno(oferta);
		                return ResponseEntity.ok("Estado de oferta actualizado correctamente.");
		            } catch (IllegalArgumentException e) {
		                return ResponseEntity.badRequest().body("Estado no válido. Usa: ACTIVA, CERRADA, PENDIENTE.");
		            }
		        }

		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta no encontrada.");
		    }

		    // Modificar el sector de una oferta
		    @Operation(
		        summary = "Modificar el sector de una oferta",
		        description = "Permite actualizar el sector asociado a una oferta mediante su nombre y el ID de la oferta."
		    )
		    @PutMapping("/ofertas/{id}/sector")
		    public ResponseEntity<?> cambiarSectorOferta(@PathVariable int id, @RequestParam String nombreSector) {
		        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		        String username = auth.getName();

		        Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		        if (usuario == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		        }

		        Oferta oferta = ofertaService.buscarUno(id);
		        if (oferta != null) {
		            Sector sector = sectorService.findByName(nombreSector);
		            if (sector != null) {
		                oferta.setSector(sector);
		                ofertaService.modificarUno(oferta);
		                return ResponseEntity.ok("Sector de oferta actualizado correctamente.");
		            } else {
		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sector no encontrado.");
		            }
		        }

		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Oferta no encontrada.");
		    }

		

}

