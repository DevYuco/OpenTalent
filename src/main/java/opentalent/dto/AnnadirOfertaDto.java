package opentalent.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import opentalent.entidades.Modalidad;
import opentalent.entidades.TipoOferta;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnadirOfertaDto {
    private String titulo;
    private String descripcion;
    private String nombreSector;
    private String fotoContenido;
    private int numeroPlazas;
    private TipoOferta tipoOferta;  // Enum: PRACTICAS, EMPLEO
    private Modalidad modalidad; // Enum: PRESENCIAL, HIBRIDO, REMOTO
    private LocalDate fechaFin;
}
