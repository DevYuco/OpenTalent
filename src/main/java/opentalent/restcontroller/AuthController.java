package opentalent.restcontroller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.LoginRequest;
import opentalent.dto.RegistroDto;
import opentalent.dto.UsuarioDto;
import opentalent.entidades.Direccion;
import opentalent.entidades.Rol;
import opentalent.entidades.Usuario;
import opentalent.jwt.JwtTokenUtil;
import opentalent.service.DireccionService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Login - Registro", description = "Endpoints para hacer login y registro")
public class AuthController {
	
    @Autowired
    private ModelMapper modelMapper;
    
	@Autowired
	private JwtTokenUtil jwtTokenUtil; 
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DireccionService direccionService; 
	
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();

        Usuario usuarioEntidad = usuarioService.buscarPorUsernameEntidad(username);

        if (usuarioEntidad != null && passwordEncoder.matches(rawPassword, usuarioEntidad.getPassword())) {

            UsuarioDto usuarioDto = usuarioService.findByUsername(username); // para devolver solo lo necesario

            String accessToken = jwtTokenUtil.generateAccessToken(username, usuarioDto.getRol());
            String refreshToken = jwtTokenUtil.generateRefreshToken(username, usuarioDto.getRol());

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("user", usuarioDto);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid username or password");
    }
 
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        if (jwtTokenUtil.validateToken(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            String rol = jwtTokenUtil.getRoleFromToken(refreshToken);

            String newAccessToken = jwtTokenUtil.generateAccessToken(username, rol);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntity.ok(tokens);
        }

        return ResponseEntity.status(403).body("Invalid refresh token");
    }
    
    @PostMapping("/signup")
    @Operation(summary = "Registro de nuevo usuario", description = "Permite a un nuevo usuario registrarse en la plataforma como USUARIO")
    public ResponseEntity<?> registro(@RequestBody RegistroDto dto){
    	 // 1. Verificar si ya existe username
    	Usuario comprobarUsuario = usuarioService.buscarPorUsernameEntidad(dto.getUsername()); 
        if(comprobarUsuario != null) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ya registrado");
        }
        
        // 2.Montamos la dirreccion 
        Direccion direccion = modelMapper.map(dto,Direccion.class); 
        direccion = direccionService.insertUno(direccion); 
        
        // 3. Añadimos el rol usuario
        Rol rolUsuario = new Rol();
        rolUsuario.setIdRol(2); // si 2 = USUARIO
        
        // 4. Crear usuario completo
        Usuario nuevoUsuario = Usuario.builder()
            .nombre(dto.getNombre())
            .apellidos(dto.getApellidos())
            .email(dto.getEmail())
            .estudios(dto.getEstudios())
            .experiencia(dto.getExperiencia())
            .cv(dto.getCv())
            .fotoPerfil(dto.getFotoPerfil())
            .telefono(dto.getTelefono())
            .username(dto.getUsername())
            .password("{noop}" + dto.getPassword()) //ENCRIPTAR CONTRASEÑA
            .fechaAlta(LocalDate.now())
            .fechaNacimiento(dto.getFechaNacimiento())
            .activo(true)
            .direccion(direccion)
            .empresa(null)
            .rol(rolUsuario)
            .build();
        
        Usuario guardado = usuarioService.insertUno(nuevoUsuario); 
        UsuarioDto dtoRespuesta = modelMapper.map(guardado, UsuarioDto.class); 
        
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoRespuesta);
        
    }
}
