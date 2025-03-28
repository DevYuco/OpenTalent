package opentalent.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(of = "idOferta")
@Builder
@Table(name = "Ofertas")
public class Oferta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferta")
    private int idOferta;

    private String titulo;
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_oferta")
    private TipoOferta tipoOferta;

    @Enumerated(EnumType.STRING)
    private EstadoOferta estado;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @Column(name = "numero_plazas")
    private int numeroPlazas;

    @Column(name = "foto_contenido")
    private String fotoContenido;

    @ManyToOne
    @JoinColumn(name = "cif")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_sector")
    private Sector sector;
}
