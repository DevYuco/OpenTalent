package opentalent.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import opentalent.dto.LoginRequest;
import opentalent.dto.UsuarioDto;
import opentalent.entidades.Usuario;
import opentalent.jwt.JwtTokenUtil;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil; 
	
	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
}
