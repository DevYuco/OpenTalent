package opentalent.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "idResenna")
@Builder
@Table(name = "Resennas")
public class Resenna implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resenna")
    private int idResenna;

    private String titulo;
    private String comentario;

    @Enumerated(EnumType.STRING)
    private Valoracion valoracion;

    private BigDecimal puntuacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cif")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;
}
