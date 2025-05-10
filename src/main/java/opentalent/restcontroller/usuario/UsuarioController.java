package opentalent.restcontroller.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.UsuarioDto;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
@Tag(name = "11 - Usuario - Perfil", description = "Endpoints para gestionar datos del usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService; 
	
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
}
