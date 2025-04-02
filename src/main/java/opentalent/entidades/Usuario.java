package opentalent.entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;

import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "idUsuario")
@Builder
@Table(name = "Usuarios")
public class Usuario implements Serializable, UserDetails {

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
    
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private Rol rol;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 return List.of(() -> "ROLE_" + rol.getNombre()); // Spring espera "ROLE_" prefix
	}


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo; // o simplemente `return true;`
    }

}
