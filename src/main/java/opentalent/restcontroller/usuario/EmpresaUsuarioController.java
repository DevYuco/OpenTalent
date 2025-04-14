package opentalent.restcontroller.usuario;


import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
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
import opentalent.dto.EmpresaDto;
import opentalent.dto.OfertaDetallesEmpresaDto;
import opentalent.dto.ResennaDetallesEmpresaDto;
import opentalent.dto.ResennaDto;
import opentalent.entidades.Empresa;
import opentalent.entidades.Oferta;
import opentalent.entidades.Resenna;
import opentalent.entidades.Usuario;
import opentalent.entidades.Valoracion;
import opentalent.service.EmpresaService;
import opentalent.service.OfertaService;
import opentalent.service.ResennaService;
import opentalent.service.UsuarioOfertaService;
import opentalent.service.UsuarioService;

@RestController
@RequestMapping("/usuario/empresas")
@CrossOrigin(origins = "*")
@Tag(name = "Usuario - Empresas", description = "Endpoints para visualizar empresas desde el perfil usuario")
public class EmpresaUsuarioController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private UsuarioOfertaService usuarioOfertaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResennaService resennaService;

	@Operation(summary = "Detalles de empresa", description = "Obtiene los datos de una empresa a partir de su CIF")
    @GetMapping("/{cif}")
    public ResponseEntity<EmpresaDto> detallesEmpresa(@PathVariable String cif) {
        Empresa empresa = empresaService.buscarUno(cif);
        
        if (empresa == null) {
        	return ResponseEntity.notFound().build();
        }
        
        // Mapear los datos básicos de la empresa
        EmpresaDto empresaDto = modelMapper.map(empresa, EmpresaDto.class);
        // Obtener ofertas activas asociadas a la empresa
        List<Oferta> ofertas = ofertaService.findActivasByEmpresaCif(cif);
        // Obtener reseñas de la empresa
        List<Resenna> resennas = resennaService.findByEmpresaCif(cif);
        //Calcular inscritos a las ofertas de una empresa
        int inscritos = usuarioOfertaService.contarInscritosPorEmpresa(cif);
        // Mapear solo los campos necesarios de cada oferta
        List<OfertaDetallesEmpresaDto> ofertasDto = ofertas.stream()
            .map(o -> modelMapper.map(o, OfertaDetallesEmpresaDto.class))
            .toList();
        // Mapear dtos necesarios de las resennas
        List<ResennaDetallesEmpresaDto> resennasDto = resennas.stream()
        	    .map(r -> modelMapper.map(r,ResennaDetallesEmpresaDto.class))
        	    .toList();
        //insertar ofertas y resennas
        empresaDto.setOfertas(ofertasDto);
        empresaDto.setResennas(resennasDto);
        empresaDto.setNumeroInscritos(inscritos);
        
        return ResponseEntity.ok(empresaDto);
    }
	
    @Operation(summary = "Añadir reseña a empresa", description = "Permite a un usuario publicar una reseña hacia una empresa con valoración y comentario")
    @PostMapping("/reseñas")
    public ResponseEntity<Boolean> añadirReseña(@RequestBody ResennaDto resennaDto) {
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	Usuario usuario = usuarioService.buscarPorUsernameEntidad(username);
        Empresa empresa = empresaService.buscarUno(resennaDto.getCif());
        
        Valoracion valoracionEnum;
        
        if (usuario == null || empresa == null) {
        	return ResponseEntity.notFound().build();
        }
        
        BigDecimal puntuacion = resennaDto.getPuntuacion();
        
        if (puntuacion == null) {
        	return ResponseEntity.badRequest().body(false);
        }
       
        if (puntuacion.compareTo(BigDecimal.valueOf(3)) < 0) {
        	valoracionEnum = Valoracion.NEGATIVA;
        }else if (puntuacion.compareTo(BigDecimal.valueOf(3)) == 0) {
        	valoracionEnum = Valoracion.NEUTRAL;
        }else {
        	valoracionEnum = Valoracion.POSITIVA;
        }

        Resenna resenna = Resenna.builder()
        	    .titulo(resennaDto.getTitulo())
        	    .comentario(resennaDto.getComentario())
        	    .valoracion(valoracionEnum)
        	    .puntuacion(puntuacion)
        	    .usuario(usuario)
        	    .empresa(empresa)
        	    .build(); 
        
        resennaService.insertUno(resenna);
        
        return ResponseEntity.ok(true);
    }
}
