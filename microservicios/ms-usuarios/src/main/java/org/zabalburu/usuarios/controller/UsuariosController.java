package org.zabalburu.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zabalburu.usuarios.dto.UsuarioDTO;
import org.zabalburu.usuarios.modelo.Usuario;
import org.zabalburu.usuarios.service.UsuariosService;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private UsuariosService servicio;
	
	@GetMapping("")
	public UsuarioDTO getUsuarios(@RequestParam(defaultValue = "1") Integer pagina) {
		return servicio.getUsuarios(pagina);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getUsuario(@PathVariable Integer id){
		Usuario u = servicio.getUsuario(id);
		if (u == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(u);
		}
	}
	
	@GetMapping("/{usuario}/{password}")
	public ResponseEntity<Usuario> getUsuario(@PathVariable String usuario, @PathVariable String password){
		Usuario u = servicio.getUsuario(usuario, password);
		if (u == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(u);
		}
	}
}