package opentalent.entidades;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "idSector")
@Builder
@Table(name = "Sectores")
public class Sector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int idSector;

    private String nombre;
    private String descripcion;
}
