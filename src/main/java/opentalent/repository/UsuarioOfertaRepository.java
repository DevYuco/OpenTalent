package opentalent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import opentalent.entidades.UsuarioOferta;
import opentalent.entidades.UsuarioOfertaId;

public interface UsuarioOfertaRepository extends JpaRepository<UsuarioOferta, UsuarioOfertaId>{

}
