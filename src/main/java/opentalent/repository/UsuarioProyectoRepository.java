package opentalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import opentalent.entidades.UsuarioProyecto;
import opentalent.entidades.UsuarioProyectoId;

public interface UsuarioProyectoRepository extends JpaRepository<UsuarioProyecto, UsuarioProyectoId>{

}
