package opentalent.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistroEmpresaDto {
    // Datos personales del responsable
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String username; 

    // Dirección
    private String pais;
    private String provincia;
    private String poblacion;
    private String calle;
    private String codigoPostal;

    // Imagen
    private String fotoPerfil;

    // Datos de empresa
    private String cif;
    
}
