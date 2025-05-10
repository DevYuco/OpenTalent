package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OfertaAdminDto {

    // Oferta
    private int idOferta;
    private String titulo;
    private String descripcion;
    private String modalidad;
    private String tipoOferta;
    private String estado;
    private int numeroPlazas;

    // Empresa
    private String nombreEmpresa;

    // Sector
    private String nombreSector;
}
