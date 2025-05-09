package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResennaDetallesEmpresaDto {
	private int idUsuario;
	private String nombre;
    private String apellidos;
    private String fotoPerfil;
}
