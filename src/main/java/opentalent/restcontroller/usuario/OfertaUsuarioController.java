package opentalent.restcontroller.usuario;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import opentalent.dto.CambiarFavoritoDto;
import opentalent.dto.OfertaDetallesVistaDto;
import opentalent.dto.OfertaTodasDto;
import opentalent.entidades.Empresa;
import opentalent.entidades.Oferta;
import opentalent.entidades.UsuarioOferta;
import opentalent.service.OfertaService;
import opentalent.service.UsuarioOfertaService;


@RestController
@RequestMapping("/usuario/ofertas")
@CrossOrigin(origins = "*")
@Tag(name = "Usuario - Ofertas", description = "Endpoints para gestionar ofertas desde el perfil usuario")
public class OfertaUsuarioController {
	
    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private UsuarioOfertaService usuarioOfertaService;
	
	@Operation(summary = "Detalles de oferta", description = "Devuelve los datos de una oferta específica")
    @GetMapping("/{id}")
    public ResponseEntity<OfertaDetallesVistaDto> detallesOferta(@PathVariable Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();   
    	
    	Oferta oferta = ofertaService.buscarUno(id);
    	
    	//Verificar oferta
    	if (oferta == null) {
    		return ResponseEntity.notFound().build();
    	}
    	
    	Empresa empresa = oferta.getEmpresa();

        // Obtener info de la tabla intermedia UsuarioOferta
        UsuarioOferta usuarioOferta = usuarioOfertaService.findByUsernameAndIdOferta(username, id);
        //Calcular vacantes
        int aceptados = usuarioOfertaService.contarAceptadosPorOferta(id);
        int vacantesDisponibles = oferta.getNumeroPlazas() - aceptados;
        

        OfertaDetallesVistaDto dto = OfertaDetallesVistaDto.builder()
            .idOferta(oferta.getIdOferta())
            .titulo(oferta.getTitulo())
            .descripcion(oferta.getDescripcion())
            .modalidad(oferta.getModalidad() != null ? oferta.getModalidad().name() : null)
            .imagenOferta(oferta.getFotoContenido())
            .nombreEmpresa(empresa.getNombreEmpresa())
            .fotoEmpresa(empresa.getFoto())
            .direccionEmpresa(empresa.getDireccion())
            .estadoAplicacion(usuarioOferta != null ? usuarioOferta.getEstado().name() : "NO_APLICADO")
            .esFavorita(usuarioOferta != null && usuarioOferta.isFavorito())
            .vacantesDisponibles(vacantesDisponibles)
            .build();

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar ofertas activas", description = "Devuelve todas las ofertas activas con info resumida y favoritas del usuario")
    @GetMapping("/")
    public ResponseEntity<List<OfertaTodasDto>> todasOfertas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Oferta> ofertas = ofertaService.buscarOfertasActivas();

        List<OfertaTodasDto> dtoList = ofertas.stream()
        	    .map(oferta -> {
        	        boolean favorita = usuarioOfertaService.esFavorita(username, oferta.getIdOferta());

        	        return OfertaTodasDto.builder()
        	            .idOfeta(oferta.getIdOferta())
        	            .fotoContenido(oferta.getFotoContenido())
        	            .titulo(oferta.getTitulo())
        	            .descripcion(oferta.getDescripcion())
        	            .foto(oferta.getEmpresa() != null ? oferta.getEmpresa().getFoto() : null)
        	            .direccion(oferta.getEmpresa() != null ? oferta.getEmpresa().getDireccion() : null)
        	            .esFavorita(favorita)
        	            .build();
        	    })
        	    .toList();

        return ResponseEntity.ok(dtoList);
    }
    
    @Operation(summary = "Ofertas favoritas", description = "Obtiene las ofertas activas marcadas como favoritas por el usuario")
    @GetMapping("/favoritas")
    public ResponseEntity<List<OfertaTodasDto>> ofertasFavoritosActivasPorUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Oferta> ofertas = usuarioOfertaService.buscarOfertasFavsActivasPorUsername(username);

        List<OfertaTodasDto> dtoList = ofertas.stream()
        	    .map(oferta -> {
        	        boolean favorita = usuarioOfertaService.esFavorita(username, oferta.getIdOferta());

        	        return OfertaTodasDto.builder()
        	            .idOfeta(oferta.getIdOferta())
        	            .fotoContenido(oferta.getFotoContenido())
        	            .titulo(oferta.getTitulo())
        	            .descripcion(oferta.getDescripcion())
        	            .foto(oferta.getEmpresa() != null ? oferta.getEmpresa().getFoto() : null)
        	            .direccion(oferta.getEmpresa() != null ? oferta.getEmpresa().getDireccion() : null)
        	            .esFavorita(favorita)
        	            .build();
        	    })
        	    .toList();

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Cambiar estado de favorito", description = "Modifica el valor booleano de una oferta marcada como favorita para un usuario")
    @PostMapping("/favoritas/cambiar")
    public ResponseEntity<Integer> cambiarEstadoFavoritoUsername(@RequestBody CambiarFavoritoDto dto) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	return ResponseEntity.ok(usuarioOfertaService.cambiarEstadoFavorito(dto.isEstado(), username, dto.getIdOferta()));
    }
}
