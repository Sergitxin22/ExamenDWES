package org.zabalburu.actividad15.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

	@EqualsAndHashCode.Include
	private Integer id;
	
	private String nombre;
	
	private String rol;
	
	private String sexo;
		
}
