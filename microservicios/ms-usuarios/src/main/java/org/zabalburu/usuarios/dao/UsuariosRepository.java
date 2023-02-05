package org.zabalburu.usuarios.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zabalburu.usuarios.modelo.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {
	Page<Usuario> findByOrderByNombre(Pageable pg);
	
	@Query("Select u From Usuario u Order By u.nombre")
	Page<Usuario> getUsuarios(Pageable pg);
	
	Optional<Usuario> findByUsuarioAndPassword(String usuario, String password);
	
	@Query("Select u From Usuario u Where lower(u.usuario)=lower(:usuario) and u.password=:password")
	Usuario getUsuario(@Param(value = "usuario") String usuario, @Param(value = "password") String password);
	
	@Query(value = "Select * From Usuario Where usuario=:usuario and contraseña=:password",
			nativeQuery = true)
	Usuario getUsuarioNative(@Param(value = "usuario") String usuario, @Param(value = "password") String password);	
}
