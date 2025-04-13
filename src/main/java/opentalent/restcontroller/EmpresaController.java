package opentalent.restcontroller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/empresa")
@CrossOrigin(origins = "*")
public class EmpresaController {
	
	
	
    @GetMapping("/verify")
    @PreAuthorize("hasRole('EMPRESA')")
    public ResponseEntity<String> verificarEmpresa() {
        return ResponseEntity.ok("✅ Acceso verificado: eres una EMPRESA");
    }
    
   
    
    
}
