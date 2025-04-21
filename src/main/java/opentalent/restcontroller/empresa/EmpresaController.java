package opentalent.restcontroller.empresa;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.RegistroDto;
import opentalent.dto.RegistroEmpresaDto;
import opentalent.entidades.Direccion;
import opentalent.entidades.Empresa;
import opentalent.entidades.Usuario;
import opentalent.service.DireccionService;
import opentalent.service.EmpresaService;
import opentalent.service.OfertaService;
import opentalent.service.SectorService;
import opentalent.service.UsuarioOfertaService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/empresa")
@CrossOrigin(origins = "*")
@Tag(name = "Empresa - Empresa", description = "Endpoints para visualizar todo lo relacionado con la empresa")
public class EmpresaController {
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private DireccionService direccionService;
	
	@Autowired
	private SectorService sectorService;
	
	@Autowired
	private OfertaService ofertaService;

	@Autowired
	private UsuarioOfertaService usuarioOfertaService;
	
	
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
	    usuario.setPassword("{noop}" + dto.getPassword()); // Puedes encriptarlo si lo deseas
	    usuario.setFotoPerfil(dto.getFotoPerfil());
	    usuarioService.modificarUno(usuario);

	    return ResponseEntity.ok(Map.of("mensaje","Empresa editada correctamente."));
	}
}
