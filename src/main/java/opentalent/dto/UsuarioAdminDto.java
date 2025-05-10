package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsuarioAdminDto {

    // Usuario
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private boolean activo;

    // Empresa
    private String nombreEmpresa;

    // Rol
    private String nombreRol;
}
