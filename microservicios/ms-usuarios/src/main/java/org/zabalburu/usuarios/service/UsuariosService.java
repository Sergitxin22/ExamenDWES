package org.zabalburu.usuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zabalburu.usuarios.dao.UsuariosRepository;
import org.zabalburu.usuarios.dto.UsuarioDTO;
import org.zabalburu.usuarios.modelo.Usuario;

@Service
public class UsuariosService {
	@Autowired
	private UsuariosRepository dao;
	
	public UsuarioDTO getUsuarios(Integer pagina) {
		Pageable pg = PageRequest.of(pagina-1, 2);
		Page<Usuario> page = dao.findAll(pg);
		UsuarioDTO userDTO = new UsuarioDTO();
		userDTO.setPagina(pagina);
		userDTO.setTotalPaginas(page.getTotalPages());
		userDTO.setUsuarios(page.getContent());
		userDTO.setUsuariosPorPagina(2);
		return userDTO;
	}
	
	public Usuario getUsuario(Integer id) {
		return dao.findById(id).orElse(null);
	}
	
	public Usuario getUsuario(String usuario, String password) {
		//return dao.findByUsuarioAndPassword(usuario, password).orElse(null);
		Usuario u = null;
		
		try {
			u = dao.getUsuario(usuario, password);
		} catch (Exception ex) {}
		
		return u;
	}

}