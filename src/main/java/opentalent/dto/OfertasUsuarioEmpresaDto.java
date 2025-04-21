package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfertasUsuarioEmpresaDto {
    private int idOferta; 
	private String titulo;
    private String descripcion;
    private String foto;
    private int vacantesDisponibles;
}
