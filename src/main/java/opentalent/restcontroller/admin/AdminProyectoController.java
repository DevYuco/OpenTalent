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

@Tag(name = "11 - Admin - Proyectos", description = "Endpoints para gestionar proyectos desde el panel del administrador")

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
		
		//----Proyectos----
		
		@Operation(
			    summary = "Listar todos los proyectos",
			    description = "Recupera una lista de todos los proyectos registrados en la plataforma. Solo accesible por administradores."
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

		@Operation(
			    summary = "Eliminar proyecto",
			    description = "Elimina un proyecto existente a partir de su ID. Solo visible para administradores."
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

		
		@Operation(
			    summary = "Cambiar estado de un proyecto",
			    description = "Permite activar o desactivar un proyecto alternando su estado actual. Únicamente para administradores."
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
