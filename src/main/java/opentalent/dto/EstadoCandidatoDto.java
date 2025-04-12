package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.EstadoAplicacion;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstadoCandidatoDto {
	private Integer idUsuario; 
	private Integer idProyecto; 
	private EstadoAplicacion estado; 
}
