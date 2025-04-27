package opentalent.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Valoracion;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResennaDetallesEmpresaDto {
    private int idResenna; 
	private String titulo;
    private String comentario;
    private BigDecimal puntuacion;
    private Valoracion valoracion;
    private UsuarioResennaDetallesEmpresaDto usuario;
}
