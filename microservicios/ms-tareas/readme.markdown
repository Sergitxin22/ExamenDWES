## CREAR MICROSERVICIO - TAREAS
1. [Creación del proyecto](#creación-del-proyecto)
2. [Configuración del proyecto](#configuración-del-proyecto)
3. [Creación del modelo](#creación-del-modelo)
4. [Creación del DTO](#creación-del-dto)
5. [Creación del DAO](#creación-del-dao)
6. [Creación del servicio](#creación-del-servicio)
7. [Creación del controlador](#creación-del-controlador)
8. [Probar la API](#probar-la-api)
9. [Posibles errores](#posibles-errores)
### Creación del proyecto
***
1. File → New → Spring Starter Project
2. [Primera pantalla](/microservicios/images/creacion-proyecto-1.PNG)
3. [Segunda pantalla](/microservicios/images/creacion-proyecto-2.PNG)
### Configuración del proyecto
***
Una vez creado el proyecto vamos a configurar el fichero application.properties ( en src → main → resources) para asignar un nombre a la aplicación, el puerto de escucha y los parámetros de la configuración de SQL Server y del servidor de recursos:

```
spring.application.name=servicio-usuarios
server.port=8005
spring.datasource.url=jdbc:sqlserver://localhost:2000;databaseName=pubs;TrustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=tiger
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.show-sql=true
spring.jpa.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
```
### Creación del modelo
***
Creamos el modelo Usuario.class en el paquete org.zabalburu.usuarios.modelo
```
package org.zabalburu.usuarios.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	private String usuario;
	
	private String nombre;
	
	@JsonIgnore
	@Column(name = "contraseña")
	private String password;
}
```
### Creación del DTO
***
Para incluir la paginación, creamos el DTO UsuarioDTO.class en el paquete org.zabalburu.usuarios.dto
```
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
```
### Creación del DAO
***
Para incluir la paginación, creamos la interfaz UsuariosRepository.java en el paquete org.zabalburu.usuarios.dao\
Este DAO debe implementar la interfaz JpaRepository\
[Imagen](/microservicios/images/crear-repositorios.PNG)
```
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
```
### Creación del servicio
***
Ahora creamos la clase UsuariosService en el paquete org.zabalburu.usuarios.service y le ponemos el decorador @Service, también inyectamos el dao con el decorador @Autowired
```
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
```
### Creación del controlador
***
Ahora creamos la clase UsuariosController en el paquete org.zabalburu.usuarios.controller y le ponemos el decorador @RestController, también inyectamos el servicio con el decorador @Autowired
```
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
```
### Probar la API
***
Ahora podemos probar la API en el puerto que hemos configurado en el archivo application.properties, en este caso el 8005.
Podemos probar los siguientes endpoints:
* GET [http://localhost:8005/usuarios](http://localhost:8005/usuarios) → Esto devuelve todos los usuarios
* GET [http://localhost:8005/usuarios/{idUsuario}](http://localhost:8005/usuarios/1) → Esto devuelve el usuario con el id que le pasamos
* GET [http://localhost:8005/usuarios/{usuario}/{password}](http://localhost:8005/usuarios/1) → Esto devuelve el usuario con el nombre y contraseña que le pasamos
* ### Posibles errores
***
Un posible error es que te salgan los usuarios vacíos, yo lo he conseguido solucionar añadiendo un bean al MsUsuariosApplication:
```
package org.zabalburu.usuarios;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class MsUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsUsuariosApplication.class, args);
	}
	
	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	
	    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	
	    dataSource.setUsername("sa");
	
	    dataSource.setPassword("tiger");
	
	    dataSource.setUrl( "jdbc:sqlserver://localhost:2000;databaseName=Northwind;TrustServerCertificate=True;");
	
	    return dataSource;
	
	 }

}
```
Y luego borrándolo.
