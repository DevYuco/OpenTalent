package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmpresaAdminDto {

    private String cif;
    private String nombreEmpresa;
    private String email;
    private boolean activo;
    private boolean destacado;
    private String nombreSector;
}

