package opentalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import opentalent.entidades.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

}
