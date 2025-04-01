package opentalent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import opentalent.entidades.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer>{
	 Optional<Rol> findByNombre(String name);
}
