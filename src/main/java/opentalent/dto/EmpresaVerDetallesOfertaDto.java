package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Direccion;
import opentalent.entidades.Modalidad;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaVerDetallesOfertaDto {
	//Datos de oferta
    private int idOferta; 
	private String titulo;
    private String descripcion;
    private String foto;
    private int vacantesDisponibles;
    private Modalidad modalidad; 
    //datos de la empresa
    private String fotoEmpresa; 
    private String nombreEmpresa; 
    private Direccion direccion; 
    
    
}
