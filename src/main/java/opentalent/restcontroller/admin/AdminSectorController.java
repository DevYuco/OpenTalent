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

@Tag(name = "13 - Admin - Sectores", description = "Endpoints para gestionar los sectores profesionales desde el panel del administrador")

public class AdminSectorController {
	
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
	
	
	//----SECTORES-------
	
	
	@Operation(
		    summary = "Listar todos los sectores",
		    description = "Recupera todos los sectores registrados en el sistema. Visible solo para administradores."
		)
		@GetMapping("/sectores")
		public ResponseEntity<?> obtenerTodosSectores() {

		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String username = auth.getName();

		    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		    if (usuario == null) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		    }

		    List<Sector> sectores = sectorService.buscarTodos();
		    return ResponseEntity.ok(sectores);
		}
		
	@Operation(
		    summary = "Eliminar un sector",
		    description = "Elimina un sector existente del sistema a partir de su ID."
		)
		@DeleteMapping("/sectores/{id}")
		public ResponseEntity<?> eliminarSector(@PathVariable int id) {

		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String username = auth.getName();

		    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		    if (usuario == null) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		    }

		    int resultado = sectorService.elimnarUno(id);
		    if (resultado == 1) {
		        return ResponseEntity.ok("Sector eliminado correctamente.");
		    } else {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sector no encontrado.");
		    }
		}
	@Operation(
		    summary = "Añadir un nuevo sector",
		    description = "Registra un nuevo sector en la plataforma, siempre que no exista previamente con el mismo nombre."
		)
		@PostMapping("/sectores")
		public ResponseEntity<?> anadirSector(@RequestBody Sector nuevoSector) {

		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String username = auth.getName();

		    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
		    if (usuario == null) {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		    }

		    // 🐞 Debug: Verificar qué llega en el body
		    System.out.println(">> Sector recibido: " + nuevoSector);
		    System.out.println(">> Nombre: " + nuevoSector.getNombre());

		    if (sectorService.findByName(nuevoSector.getNombre()) != null) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un sector con ese nombre.");
		    }

		    Sector creado = sectorService.insertUno(nuevoSector);
		    if (creado != null) {
		        return ResponseEntity.status(HttpStatus.CREATED).body("Sector añadido correctamente.");
		    } else {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el sector.");
		    }
		}


}
