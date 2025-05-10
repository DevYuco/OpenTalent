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

@Tag(name = "09 - Admin - Empresas", description = "Endpoints para registrar y gestionar empresas desde el panel de administración")

public class AdminEmpresaController {
	
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
	
	

	@Operation(
		    summary = "Registrar nueva empresa",
		    description = "Permite al administrador registrar manualmente una nueva empresa, asociándola a un sector existente y guardando su dirección."
		)
	@PostMapping("/empresas")
	public ResponseEntity<?> registrarEmpresa(@RequestBody RegistroEmpresaAdminDto dto) {

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();

	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
	    }

	    if (empresaService.buscarUno(dto.getCif()) != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe una empresa con ese CIF.");
	    }

	    // Crear dirección
	    Direccion direccion = Direccion.builder()
	        .calle(dto.getCalle())
	        .poblacion(dto.getPoblacion())
	        .provincia(dto.getProvincia())
	        .pais(dto.getPais())
	        .codigoPostal(dto.getCodigoPostal())
	        .build();

	    Direccion direccionGuardada = direccionService.insertUno(direccion);
	    if (direccionGuardada == null) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la dirección.");
	    }

	    // 💬 Depuración: imprimir nombre recibido para buscar sector
	    System.out.println("==> Nombre de sector recibido en DTO: [" + dto.getNombreSector() + "]");

	    // Buscar sector
	    Sector sector = sectorService.findByName(dto.getNombreSector());


	    // 💬 Depuración: resultado de búsqueda de sector
	    System.out.println("==> Resultado de buscarPorNombre: " + (sector != null ? sector.getNombre() : "null"));

	    if (sector == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sector no encontrado.");
	    }

	    // Crear empresa
	    Empresa empresa = Empresa.builder()
	        .cif(dto.getCif())
	        .nombreEmpresa(dto.getNombreEmpresa())
	        .email(dto.getEmail())
	        .descripcion(dto.getDescripcion())
	        .foto(dto.getFoto())
	        .fotoContenido(dto.getFotoContenido())
	        .destacado(dto.isDestacado())
	        .activo(true)
	        .direccion(direccionGuardada)
	        .build();

	    empresa.setSectores(List.of(sector));

	    empresaService.insertUno(empresa);

	    return ResponseEntity.status(HttpStatus.CREATED).body("Empresa creada correctamente.");
	}
}