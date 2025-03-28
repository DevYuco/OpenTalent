package opentalent.entidades;

import java.io.Serializable;

import jakarta.persistence.*;

import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Builder
@Table(name = "Usuario_Proyecto")
public class UsuarioProyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsuarioProyectoId id; 
    
    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idProyecto")
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @Enumerated(EnumType.STRING)
    private EstadoParticipacion estado;

    private boolean favorito;
}
