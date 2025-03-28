package opentalent.entidades;


import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@EqualsAndHashCode(of = "cif")
@Builder
@Table(name = "Empresas")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String cif;

    @Column(name = "nombre_empresa")
    private String nombreEmpresa;

    private boolean activo;

    private String email;

    private String foto;

    @Column(name = "foto_contenido")
    private String fotoContenido;

    private boolean destacado;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;
    
    @ManyToMany
	@JoinTable(
		name="empresa_sector"
		, joinColumns={
			@JoinColumn(name="cif")
			}
		, inverseJoinColumns={
			@JoinColumn(name="id_sector")
			}
		)
    private List<Sector> sectores; 
}
