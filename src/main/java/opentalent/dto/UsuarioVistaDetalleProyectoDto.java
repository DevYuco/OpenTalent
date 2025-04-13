package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioVistaDetalleProyectoDto {
	private int idUsuario;
	private String nombre;
    private String apellidos;
    private String fotoPerfil;
}
