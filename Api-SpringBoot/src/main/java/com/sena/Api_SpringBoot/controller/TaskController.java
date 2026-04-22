package com.sena.Api_SpringBoot.controller;

import com.sena.Api_SpringBoot.entity.Task;
import com.sena.Api_SpringBoot.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin("*")
@Tag(name = "Tasks", description = "API para gestionar tareas")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Verificar que la API está funcionando", description = "Retorna un mensaje de bienvenida")
    @ApiResponse(responseCode = "200", description = "API funcionando correctamente")
    public Map<String, String> home() {
        return Map.of("message", "API Spring Boot funcionando");
    }

    @GetMapping("/tasks")
    @Operation(summary = "Obtener todas las tareas", description = "Retorna una lista con todas las tareas almacenadas")
    @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida exitosamente")
    public List<Task> getAll() {
        return repository.findAll();
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Obtener tarea por ID", description = "Retorna una tarea específica por su identificador")
    @ApiResponse(responseCode = "200", description = "Tarea obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public Task getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping("/tasks")
    @Operation(summary = "Crear una nueva tarea", description = "Crea y almacena una nueva tarea en la base de datos")
    @ApiResponse(responseCode = "200", description = "Tarea creada exitosamente")
    public Task create(@RequestBody Task task) {
        return repository.save(task);
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Actualizar una tarea", description = "Actualiza los datos de una tarea existente")
    @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        Task existing = repository.findById(id).orElseThrow();
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setCompleted(task.getCompleted());
        return repository.save(existing);
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Eliminar una tarea", description = "Elimina una tarea de la base de datos por su ID")
    @ApiResponse(responseCode = "200", description = "Tarea eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    public Map<String, String> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return Map.of("message", "Tarea eliminada");
    }
}
