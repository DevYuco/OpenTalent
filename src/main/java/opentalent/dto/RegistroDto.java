package opentalent.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistroDto {
    private String nombre;
    private String apellidos;
    private String email;
    private String estudios;
    private String experiencia;
    private String cv;
    private String fotoPerfil;
    private String telefono;
    private String username;
    private String password;
    private LocalDate fechaNacimiento;
    private String calle;
    private String pais;
    private String codigoPostal;
    private String provincia;
    private String poblacion;
}
