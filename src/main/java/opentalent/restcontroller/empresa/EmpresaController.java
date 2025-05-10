package opentalent.restcontroller.empresa;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.RegistroEmpresaDto;
import opentalent.dto.UsuarioDto;
import opentalent.entidades.Direccion;
import opentalent.entidades.Empresa;
import opentalent.entidades.Usuario;
import opentalent.service.DireccionService;
import opentalent.service.EmpresaService;
import opentalent.service.SectorService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/empresa")
@CrossOrigin(origins = "*")
@Tag(name = "07 - Empresa - Perfil", description = "Endpoints para visualizar todo lo relacionado con la empresa")
public class EmpresaController {
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private DireccionService direccionService;
	
	@Autowired
	private SectorService sectorService;
	
	@Operation(
		    summary = "Editar datos de empresa",
		    description = "Permite a un usuario con cuenta de tipo empresa editar sus datos de perfil, incluyendo dirección, empresa y datos personales del responsable."
		)
	@ApiResponse(responseCode = "200", description = "Datos de empresa actualizados correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario o empresa no encontrada")
	@ApiResponse(responseCode = "500", description = "Error interno al procesar la solicitud")
	@PutMapping("/")
	public ResponseEntity<?> editarEmpresa(@RequestBody RegistroEmpresaDto dto) {

	    // 1. Obtener username desde el token (usuario autenticado)
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 2. Buscar al usuario
	    Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
	    if (usuario == null || usuario.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","Usuario o empresa no encontrada."));
	    }

	    Empresa empresa = usuario.getEmpresa();

	    // 3. Actualizar dirección
	    Direccion direccion = usuario.getDireccion();
	    direccion.setCalle(dto.getCalle());
	    direccion.setPais(dto.getPais());
	    direccion.setCodigoPostal(dto.getCodigoPostal());
	    direccion.setProvincia(dto.getProvincia());
	    direccion.setPoblacion(dto.getPoblacion());
	    direccionService.modificarUno(direccion);

	    // 4. Actualizar empresa
	    empresa.setEmail(dto.getEmail());
	    empresaService.modificarUno(empresa);

	    // 5. Actualizar usuario responsable
	    usuario.setNombre(dto.getNombre());
	    usuario.setApellidos(dto.getApellido());
	    usuario.setFechaNacimiento(dto.getFechaNacimiento());
	    usuario.setTelefono(dto.getTelefono());
	    usuario.setEmail(dto.getEmail());
	    usuario.setUsername(dto.getUsername());
	    usuario.setPassword("{noop}" + dto.getPassword()); // Puedes encriptarlo si lo deseas
	    usuario.setFotoPerfil(dto.getFotoPerfil());
	    usuarioService.modificarUno(usuario);

	    return ResponseEntity.ok(Map.of("mensaje","Empresa editada correctamente."));
	}
	
	@Operation(
		    summary = "Obtener todos los sectores",
		    description = "Devuelve una lista con todos los sectores disponibles del sistema."
		)
	@ApiResponse(responseCode = "200", description = "Lista de sectores obtenida correctamente")
	@GetMapping("/sectores")
	public ResponseEntity<?> obtenerTodosLosSectores() {
	    return ResponseEntity.ok(sectorService.buscarTodos());
	}
	
	@Operation(
		    summary = "Obtener perfil de empresa para edición",
		    description = "Devuelve los datos actuales del perfil de empresa del usuario autenticado para ser editados."
		)
	@ApiResponse(responseCode = "200", description = "Datos de perfil de empresa obtenidos correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario o empresa no encontrada")
	@GetMapping("/perfil/editar")
	public ResponseEntity<?> obtenerPerfilEmpresaParaEditar() {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    UsuarioDto usuario = usuarioService.findByUsername(username);

	    if(usuario == null) {
	        return ResponseEntity.status(404).build();
	    }
	    return ResponseEntity.ok(usuario);
	}
}
