package org.zabalburu.usuarios.dto;

import java.util.List;

import org.zabalburu.usuarios.modelo.Usuario;

import lombok.Data;

@Data
public class UsuarioDTO {
	private Integer pagina;
	private Integer totalPaginas;
	private Integer usuariosPorPagina;
	private List<Usuario> usuarios;
	
}
