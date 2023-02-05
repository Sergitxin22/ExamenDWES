package org.zabalburu.actividad15.modelo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tarea {

	@EqualsAndHashCode.Include
	private Integer id;
	
	private Usuario usuario;
	
	
	@NotBlank(message = "Debe especificarse el título de la tarea")
	private String titulo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;
	
	private String descripcion;
	
	private Boolean realizada;
	
}
