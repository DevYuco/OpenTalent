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

@Tag(name = "12 - Admin - Reseñas", description = "Endpoints para gestionar reseñas de proyectos desde el panel del administrador")

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
		
		//----Reseñass-----
		
		// Ver todas las reseñas
		@Operation(
			    summary = "Listar todas las reseñas",
			    description = "Devuelve una lista con todas las reseñas de proyectos registradas en la plataforma. Accesible solo por administradores."
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
			    description = "Elimina una reseña específica a partir de su ID. Solo puede hacerlo un administrador."
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
