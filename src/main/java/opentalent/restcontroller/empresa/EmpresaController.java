package opentalent.restcontroller.empresa;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import opentalent.entidades.Usuario;
import opentalent.service.DireccionService;
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
	private DireccionService direccionService;
	
	@Autowired
	private SectorService sectorService;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@Autowired 
	private PasswordEncoder passwordEncoder; 
	
	@Operation(
		    summary = "Editar datos de empresa",
		    description = "Permite a un usuario con cuenta de tipo empresa editar sus datos de perfil, incluyendo dirección, empresa y datos personales del responsable."
		)
	@ApiResponse(responseCode = "200", description = "Datos de empresa actualizados correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario o empresa no encontrada")
	@ApiResponse(responseCode = "500", description = "Error interno al procesar la solicitud")
	@PutMapping("/")
	public ResponseEntity<?> editarEmpresa(@RequestBody RegistroEmpresaDto dto) {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    Usuario usuarioActual = usuarioService.buscarPorUsernameEntidad(username);

	    if (usuarioActual == null || usuarioActual.getEmpresa() == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario o empresa no encontrada."));
	    }

	    // Comprobación de duplicado de email
	    Usuario otro = usuarioService.findByEmail(dto.getEmail());
	    if (otro != null && !otro.getEmail().equals(usuarioActual.getEmail())) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El correo electrónico ya está en uso por otro usuario"));
	    }

	    // Actualizar datos personales del responsable
	    usuarioActual.setNombre(dto.getNombre());
	    usuarioActual.setApellidos(dto.getApellidos());
	    usuarioActual.setFechaNacimiento(dto.getFechaNacimiento());
	    usuarioActual.setTelefono(dto.getTelefono());
	    usuarioActual.setEmail(dto.getEmail());
	    usuarioActual.setUsername(dto.getUsername());
	    usuarioActual.setFotoPerfil(dto.getFotoPerfil());
	    
	    
        // Actualizar dirección
	    Direccion direccion = usuarioActual.getDireccion();
	    direccion.setCalle(dto.getCalle());
	    direccion.setPais(dto.getPais());
	    direccion.setCodigoPostal(dto.getCodigoPostal());
	    direccion.setProvincia(dto.getProvincia());
	    direccion.setPoblacion(dto.getPoblacion());
	    direccionService.modificarUno(direccion);
	    
	    
	    // Si se envía nueva contraseña
	    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	        //usuarioActual.setPassword("{noop}" + dto.getPassword());
            usuarioActual.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }


	    Usuario actualizado = usuarioService.modificarUno(usuarioActual);
	    UsuarioDto respuesta = modelMapper.map(actualizado, UsuarioDto.class);

	    return ResponseEntity.ok(respuesta);
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
