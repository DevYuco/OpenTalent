package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Direccion;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OfertaDetallesVistaDto {
    //Datos de oferta
	private int idOferta; 
	private String titulo;
    private String descripcion;
    private String modalidad;
    private String imagenOferta;
    //Detalles empresa
    private String nombreEmpresa;
    private String fotoEmpresa;
    private Direccion direccionEmpresa;
    
    //Datos adicionales
    private String estadoAplicacion;
    private boolean esFavorita;
    private int vacantesDisponibles;

    
}
