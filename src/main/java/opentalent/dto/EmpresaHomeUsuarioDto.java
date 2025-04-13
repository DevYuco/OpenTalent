package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaHomeUsuarioDto {
	private String cif;
	private String nombreEmpresa;
	private String foto;
	private String fotoContenido;
	private String descripcion; 
}
