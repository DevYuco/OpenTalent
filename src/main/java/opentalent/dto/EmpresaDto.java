package opentalent.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Direccion;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDto {
	private String cif;
    private String nombreEmpresa;
    private boolean activo;
    private String email;
    private String foto;
    private String fotoContenido;
    private boolean destacado;
    private Direccion direccion; 
    private List<SectorNombreDto> sectores; 
}
