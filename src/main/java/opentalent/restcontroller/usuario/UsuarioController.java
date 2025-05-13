package opentalent.restcontroller.usuario;

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
import opentalent.dto.RegistroDto;
import opentalent.dto.UsuarioDto;
import opentalent.entidades.Direccion;
import opentalent.entidades.Usuario;
import opentalent.service.DireccionService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
@Tag(name = "05 - Usuario - Perfil", description = "Endpoints para gestionar datos del usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private DireccionService direccionService; 
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@Autowired 
	private PasswordEncoder passwordEncoder; 
	
	@Operation(
		    summary = "Obtener datos del usuario autenticado",
		    description = "Devuelve los datos del usuario autenticado para permitir la edición de su perfil."
		)
	@ApiResponse(responseCode = "200", description = "Datos del usuario obtenidos correctamente")
	@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	@GetMapping("/")
	public ResponseEntity<UsuarioDto> datosDeUsuarioParaEditarDatos() {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    
	    UsuarioDto usuario = usuarioService.findByUsername(username); 
	    if(usuario == null) {
	        return ResponseEntity.status(404).build();
	    }
	    return ResponseEntity.ok(usuario);
	}
	
	@PutMapping("/editar")
	@Operation(summary = "Editar perfil del usuario", description = "Permite al usuario autenticado actualizar sus datos personales, dirección y, opcionalmente, su contraseña.")
	public ResponseEntity<?> editarPerfil(@RequestBody RegistroDto dto) {
	    String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    Usuario usuarioActual = usuarioService.buscarPorUsernameEntidad(username);

	    if (usuarioActual == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario no encontrado"));
	    }

	    // Verificar email duplicado
	    Usuario otro = usuarioService.findByEmail(dto.getEmail());
	    if (otro != null && !otro.getEmail().equals(usuarioActual.getEmail())) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", "El correo electrónico ya está en uso por otro usuario"));
	    }

	    // Actualizar campos
	    usuarioActual.setNombre(dto.getNombre());
	    usuarioActual.setApellidos(dto.getApellidos());
	    usuarioActual.setEmail(dto.getEmail());
	    usuarioActual.setTelefono(dto.getTelefono());
	    usuarioActual.setEstudios(dto.getEstudios());
	    usuarioActual.setExperiencia(dto.getExperiencia());
	    usuarioActual.setCv(dto.getCv());
	    usuarioActual.setFotoPerfil(dto.getFotoPerfil());
	    usuarioActual.setFechaNacimiento(dto.getFechaNacimiento());

        // Actualizar dirección
	    Direccion direccion = usuarioActual.getDireccion();
	    direccion.setCalle(dto.getCalle());
	    direccion.setPais(dto.getPais());
	    direccion.setCodigoPostal(dto.getCodigoPostal());
	    direccion.setProvincia(dto.getProvincia());
	    direccion.setPoblacion(dto.getPoblacion());
	    direccionService.modificarUno(direccion);

	    // Si manda nueva contraseña
	    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	        //usuarioActual.setPassword("{noop}" + dto.getPassword()); // adapta si usas encoder
            usuarioActual.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }

	    Usuario actualizado = usuarioService.modificarUno(usuarioActual);
	    UsuarioDto respuesta = modelMapper.map(actualizado, UsuarioDto.class);

	    return ResponseEntity.ok(respuesta);
	}
}
