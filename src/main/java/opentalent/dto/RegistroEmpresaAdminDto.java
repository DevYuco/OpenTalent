package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegistroEmpresaAdminDto {

    private String cif;
    private String nombreEmpresa;
    private String email;
    private String descripcion;
    private String foto;
    private String fotoContenido;
    private boolean destacado;

    // Dirección
    private String calle;
    private String poblacion;
    private String provincia;
    private String pais;
    private String codigoPostal;

    // Sector
    private String nombreSector;

}
