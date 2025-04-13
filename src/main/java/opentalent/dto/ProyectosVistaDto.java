package opentalent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProyectosVistaDto {
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private String foto;
    private String fotoContenido;
    private boolean activo;
    private boolean esFavorito;
    private int plazasDisponibles; 
}
