package opentalent.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResennaDto {
	private String titulo;
    private String comentario;
    private BigDecimal puntuacion;
    private String username; 
    private String cif; 
}
