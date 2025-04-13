package opentalent.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoDetallesVistaDto {
	private int idProyecto;
	//Usuario
	private String nombreUsuario; 
	private String apellidos; 
	private String fotoUsuario; 
	
	//Proyecto
	private LocalDate fechaInicio;
	private String titulo; 
	private String foto;
	private String fotoContenido;
	private int plazasRestantes; 
	private boolean esFavorito; 
	private String descripcion; 
	//otros datos
	private String estadoAplicacion; 
	
	//Participantes
	private List<UsuarioVistaDetalleProyectoDto> participantes; 
}
