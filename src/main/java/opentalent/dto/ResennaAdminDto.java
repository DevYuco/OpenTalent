package opentalent.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResennaAdminDto {

    // Resenna
    private int idResenna;
    private String titulo;
    private String comentario;
    private BigDecimal puntuacion;

    // Usuario
    private String nombreUsuario;

    // Proyecto
    private String nombreProyecto;
}
