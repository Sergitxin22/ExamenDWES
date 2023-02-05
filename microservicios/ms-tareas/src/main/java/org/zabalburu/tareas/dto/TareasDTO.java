package org.zabalburu.tareas.dto;

import java.util.List;

import org.zabalburu.tareas.modelo.Tarea;

import lombok.Data;

@Data
public class TareasDTO {
	private Integer pagina;
	private Integer totalPaginas;
	private Integer tareasPorPagina;
	private List<Tarea> tareas;
}
