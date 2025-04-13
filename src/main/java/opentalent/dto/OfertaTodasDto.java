package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Direccion;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfertaTodasDto {
	//Datos de oferta
	private String fotoContenido; 
	private String titulo; 
	private String descripcion; 
	
	//datos de empresa
	private String foto;
	private Direccion direccion; 
	
	//Otros datos
	private boolean esFavorita; 

}
