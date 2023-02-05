## CREAR MICROSERVICIO
1. [Creación del proyecto](#creación-del-proyecto)
2. [Configuración del proyecto](#configuración-del-proyecto)
3. [Installation](#installation)
4. [Collaboration](#collaboration)
5. [FAQs](#faqs)
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
Para incluir la paginación, creamos el DTO UsuarioDTO.class en el paquete org.zabalburu.usuarios.dto\
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
Para incluir la paginación, creamos el DAO UsuariosRepository.class en el paquete org.zabalburu.usuarios.dao\
Este DAO debe implementar la interfaz JpaRepository<T, ID>: [Imagen](/microservicios/images/crear-repositorio.PNG)
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
To answer this question we use an unordered list:
* First point
* Second Point
* Third point
3. **Third question in bold**
Answer of the third question with *italic words*.
4. **Fourth question in bold**
| Headline 1 in the tablehead | Headline 2 in the tablehead | Headline 3 in the tablehead |
|:--------------|:-------------:|--------------:|
| text-align left | text-align center | text-align right |
### Screenshot
![Image text]([https://www.united-internet.de/fileadmin/user_upload/Brands/Downloads/Logo_IONOS_by.jpg](https://zabalburu.neolms.com/files/4360843/springBoot3(2).PNG?lmsauth=5348b0aafa6e73e7d17b329851881005c362fea1))
## Technologies
***
A list of technologies used within the project:
* [Technologie name](https://example.com): Version 12.3 
* [Technologie name](https://example.com): Version 2.34
* [Library name](https://example.com): Version 1234
## Installation
***
A little intro about the installation. 
```
$ git clone https://example.com
$ cd ../path/to/the/file
$ npm install
$ npm start
```
Side information: To use the application in a special environment use ```lorem ipsum``` to start
## Collaboration
***
Give instructions on how to collaborate with your project.
> Maybe you want to write a quote in this part. 
> It should go over several rows?
> This is how you do it.
## FAQs
***
A list of frequently asked questions
1. **This is a question in bold**
Answer of the first question with _italic words_. 
2. __Second question in bold__ 
To answer this question we use an unordered list:
* First point
* Second Point
* Third point
3. **Third question in bold**
Answer of the third question with *italic words*.
4. **Fourth question in bold**
| Headline 1 in the tablehead | Headline 2 in the tablehead | Headline 3 in the tablehead |
|:--------------|:-------------:|--------------:|
| text-align left | text-align center | text-align right |
