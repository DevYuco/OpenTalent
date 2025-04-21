package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Direccion;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
	private int idUsuario; 
	private String nombre;
    private String apellidos;
    private String email;
    private String estudios;
    private String experiencia;
    private String cv;
    private String fotoPerfil;
    private String telefono;
    private String username;
    private String fechaAlta;
    private String fechaNacimiento;
    private boolean activo;
    private String empresa;
    private Direccion direccion;
    private String rol;
}
