package opentalent.restcontroller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.EmpresasTodasDto;
import opentalent.dto.LoginRequest;
import opentalent.dto.RegistroDto;
import opentalent.dto.RegistroEmpresaDto;
import opentalent.dto.UsuarioDto;
import opentalent.entidades.Direccion;
import opentalent.entidades.Empresa;
import opentalent.entidades.Rol;
import opentalent.entidades.Usuario;
import opentalent.jwt.JwtTokenUtil;
import opentalent.service.DireccionService;
import opentalent.service.EmpresaService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "01 - Auth", description = "Endpoints para hacer login y registro")
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
	
	@Autowired
	private EmpresaService empresaService; 
	
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

        return ResponseEntity.status(401).body(Map.of("mensaje","Invalid username or password"));
    }
 
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje","Refresh token is required"));
        }

        if (jwtTokenUtil.validateToken(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            String rol = jwtTokenUtil.getRoleFromToken(refreshToken);

            String newAccessToken = jwtTokenUtil.generateAccessToken(username, rol);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntity.ok(tokens);
        }

        return ResponseEntity.status(403).body(Map.of("mensaje","Invalid refresh token"));
    }
    
    @PostMapping("/signup")
    @Operation(summary = "Registro de nuevo usuario", description = "Permite a un nuevo usuario registrarse en la plataforma como USUARIO")
    public ResponseEntity<?> registro(@RequestBody RegistroDto dto){
    	 // 1. Verificar si ya existe username
    	Usuario comprobarUsuario = usuarioService.buscarPorUsernameEntidad(dto.getUsername()); 
        if(comprobarUsuario != null) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje","Usuario ya registrado"));
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
          //.password(passwordEncoder.encode(dto.getPassword()))
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
    @Operation(
    	    summary = "Registro de usuario con empresa",
    	    description = "Permite registrar un nuevo usuario y asociarlo a una empresa existente en el sistema mediante su CIF. Verifica que el email no esté duplicado y que la empresa no esté ya asignada."
    	)
    @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente")
    @ApiResponse(responseCode = "404", description = "Empresa con el CIF indicado no encontrada")
    @ApiResponse(responseCode = "409", description = "El usuario ya existe o la empresa ya está asignada a otro usuario")
    @PostMapping("/registro/empresa")
    public ResponseEntity<?> registrarEmpresa(@RequestBody RegistroEmpresaDto dto) {

        // 1. Comprobar si el usuario ya existe
        Usuario existente = usuarioService.buscarPorUsernameEntidad(dto.getEmail());
        if (existente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje","El usuario ya existe"));
        }

        // 2. Buscar la empresa por CIF
        Empresa empresa = empresaService.buscarUno(dto.getCif());
        if (empresa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje","La empresa con ese CIF no existe"));
        }

        // 3. Verificar que esa empresa no está ya asignada a otro usuario
        List<Usuario> usuarios = usuarioService.buscarTodos();
        boolean empresaAsignada = usuarios.stream().anyMatch(u -> 
            u.getEmpresa() != null && u.getEmpresa().getCif().equals(dto.getCif())
        );

        if (empresaAsignada) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje","La empresa ya está asignada a otro usuario"));
        }

        // 4. Construir dirección
        Direccion direccion = Direccion.builder()
                .calle(dto.getCalle())
                .pais(dto.getPais())
                .codigoPostal(dto.getCodigoPostal())
                .provincia(dto.getProvincia())
                .poblacion(dto.getPoblacion())
                .build();
        direccionService.insertUno(direccion);

        // 5. Obtener el rol EMPRESA
        Rol rol = new Rol();
        rol.setIdRol(3); // si 3 = EMPRESA

        // 6. Crear usuario empresa
        Usuario nuevoUsuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellidos(dto.getApellido())
                .email(dto.getEmail())
                .username(dto.getUsername())
                //.password(passwordEncoder.encode(dto.getPassword()))
                .password("{noop}" + dto.getPassword())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .fotoPerfil(dto.getFotoPerfil())
                .activo(true)
                .fechaAlta(LocalDate.now())
                .direccion(direccion)
                .empresa(empresa)
                .rol(rol)
                .build();

        usuarioService.insertUno(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje","Empresa registrada correctamente con usuario asignado."));
    }
    
    @Operation(
    	    summary = "Listar todas las empresas",
    	    description = "Devuelve una lista con el nombre y CIF de todas las empresas registradas en la plataforma."
    	)
    @ApiResponse(responseCode = "200", description = "Empresas listadas correctamente")
    @GetMapping("/empresas")
    public ResponseEntity<List<EmpresasTodasDto>> todasEmpresas() {
        List<EmpresasTodasDto> empresasDto = empresaService.buscarTodos()
            .stream()
            .map(e -> {
            	EmpresasTodasDto dto = modelMapper.map(e, EmpresasTodasDto.class); 
            	return dto; 
            })
            .toList();

        return ResponseEntity.ok(empresasDto);
    }
}
