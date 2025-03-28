package opentalent.entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Builder

@Table(name = "Usuario_Oferta")
public class UsuarioOferta implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsuarioOfertaId id; 

    
    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    
    @ManyToOne
    @MapsId("idOferta")
    @JoinColumn(name = "id_oferta")
    private Oferta oferta;

    @Column(name = "fecha_aplicacion")
    private LocalDateTime fechaAplicacion;

    @Enumerated(EnumType.STRING)
    private EstadoAplicacion estado;

    private boolean favorito;
}
