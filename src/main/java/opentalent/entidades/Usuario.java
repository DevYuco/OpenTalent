package opentalent.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "idUsuario")
@Builder
@Table(name = "Usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    private String nombre;
    private String apellidos;
    private String email;
    private String estudios;
    private String experiencia;
    
    private String cv;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    private String telefono;
    private String username;
    private String password;

    @Column(name = "fecha_alta")
    private LocalDate fechaAlta;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "cif")
    private Empresa empresa;
}
