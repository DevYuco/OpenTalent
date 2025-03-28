package opentalent.entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioOfertaId implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name ="id_usuario") 
	private int idUsuario; 
	
	@Column(name ="id_oferta")
	private int idOferta; 
}
