<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="frm" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Gestión Tareas</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
	crossorigin="anonymous">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-2"></div>
			<div class="col-8 text-center">
				<h1>
					Bienvenido/a ${usuario.nombre } <a href="/salir"
						class="btn btn-outline-primary">Salir</a>
				</h1>
			</div>
		</div>
		<c:if test="${! empty msg }">
			<div class="row">
				<div class="col-2"></div>
				<div class="col-8">
					<div class="alert alert-success">${msg }</div>
				</div>
			</div>
		</c:if>
		<c:if test="${empty tareas.tareas }">
			<div class="row">
				<div class="col-2"></div>
				<div class="col-8">
					<div class="alert alert-info">No hay tareas</div>
				</div>
			</div>
		</c:if>
		<c:if test="${!empty tareas.tareas }">
			<div class="row">
				<div class="col-12">
					<table class="table">
						<thead>
							<tr>
								<th scope="col">#</th>
								<th scope="col">Título</th>
								<th scope="col">Fecha</th>
								<th scope="col">Descripción</th>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="t" items="${tareas.tareas }">
								<tr>
									<th scope="row">${t.id }</th>
									<td>${t.titulo}</td>
									<td><fmt:formatDate value="${t.fecha }" /></td>
									<td>${t.descripcion }</td>
									<td><c:if test="${!t.realizada }">
											<a href="/finalizar?idTarea=${t.id }"
												class="btn btn-outline-primary">Finalizar</a>
										</c:if></td>
									<td><a href="/eliminar?idTarea=${t.id }"
										class="btn btn-outline-danger">Eliminar</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<nav aria-label="Page navigation example">
					<ul class="pagination">
						<c:if test="${tareas.pagina > 1 }">
							<li class="page-item"><a class="page-link"
								href="/ir?pos=${tareas.pagina-1 }">Anterior</a></li>
						</c:if>
						<c:forEach begin="1" end="${tareas.totalPaginas }" var="pos">
						<li class="page-item"><a class="page-link"
							href="/ir?pos=${pos }">${pos }</a></li>
						</c:forEach>
						
						<c:if test="${tareas.pagina <tareas.totalPaginas }">
							<li class="page-item"><a class="page-link"
								href="/ir?pos=${tareas.pagina+1 }">Siguiente</a></li>
						</c:if>
					</ul>
				</nav>
			</div>
		</c:if>
		<div class="row">
			<div class="col-2"></div>
			<div class="col-8">
				<frm:form action="/nuevaTarea" modelAttribute="tarea">
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Título</label>
						<frm:input type="text" class="form-control"
							id="exampleInputEmail1" aria-describedby="emailHelp"
							path="titulo" />
						<frm:errors path="titulo" cssClass="text-danger"></frm:errors>
					</div>
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Fecha</label>
						<frm:input type="date" class="form-control"
							id="exampleInputEmail1" aria-describedby="emailHelp" path="fecha" />
					</div>
					<div class="mb-3">
						<label for="exampleInputEmail1" class="form-label">Descripción</label>
						<frm:textarea type="text" class="form-control"
							id="exampleInputEmail1" aria-describedby="emailHelp"
							path="descripcion" />
					</div>
					<button type="submit" class="btn btn-primary">Guardar</button>
				</frm:form>

			</div>
		</div>
		<c:if test="${!empty error }">
			<div class="row mt-3">
				<div class="col-2"></div>
				<div class="col-8 text-center">
					<div class="alert alert-danger">${error }</div>
				</div>
			</div>
		</c:if>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
		crossorigin="anonymous"></script>
</body>
</html>