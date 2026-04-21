package com.sena.Api_SpringBoot.controller;

import com.sena.Api_SpringBoot.entity.Task;
import com.sena.Api_SpringBoot.repository.TaskRepository;
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
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Map<String, String> home() {
        return Map.of("message", "API Spring Boot funcionando");
    }

    @GetMapping("/tasks")
    public List<Task> getAll() {
        return repository.findAll();
    }

    @GetMapping("/tasks/{id}")
    public Task getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping("/tasks")
    public Task create(@RequestBody Task task) {
        return repository.save(task);
    }

    @PutMapping("/tasks/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        Task existing = repository.findById(id).orElseThrow();
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setCompleted(task.getCompleted());
        return repository.save(existing);
    }

    @DeleteMapping("/tasks/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return Map.of("message", "Tarea eliminada");
    }
}
