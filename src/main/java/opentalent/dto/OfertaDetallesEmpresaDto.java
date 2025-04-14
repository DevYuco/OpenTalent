package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaDetallesEmpresaDto {
    private String idOferta; 
	private String titulo;
    private String descripcion;
    private String foto;
}