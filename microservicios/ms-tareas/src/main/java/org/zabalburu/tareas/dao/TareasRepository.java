package org.zabalburu.tareas.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zabalburu.tareas.modelo.Tarea;

public interface TareasRepository extends JpaRepository<Tarea, Integer> {

	Page<Tarea> findByIdUsuarioOrderByFechaDesc(Pageable pg, Integer idUsuario);

	@Query("Select t From Tarea t where t.idUsuario=:idusuario and not t.realizada Order By t.fecha desc")
	Page<Tarea> getTareasPendientes(Pageable pg, @Param(value = "idusuario") Integer idUsuario);
	
	Optional<Tarea> findByIdUsuarioAndId(Integer idUsuario, Integer idTarea);
}
