package com.biblioteca.bff.controller;

import com.biblioteca.bff.model.ApiResponse;
import com.biblioteca.bff.model.Prestamo;
import com.biblioteca.bff.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Prestamo>> crearPrestamo(@Valid @RequestBody Prestamo prestamo) {
        Prestamo prestamoCreado = prestamoService.crearPrestamo(prestamo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(prestamoCreado, "Préstamo registrado exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Prestamo>> obtenerPrestamo(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.obtenerPrestamo(id);
        return ResponseEntity.ok(ApiResponse.success(prestamo, "Préstamo obtenido exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Prestamo>>> listarPrestamos() {
        List<Prestamo> prestamos = prestamoService.listarPrestamos();
        return ResponseEntity.ok(ApiResponse.success(prestamos, "Préstamos obtenidos exitosamente"));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<Prestamo>>> listarPrestamosPorUsuario(@PathVariable Long idUsuario) {
        List<Prestamo> prestamos = prestamoService.listarPrestamosPorUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.success(prestamos, "Préstamos del usuario obtenidos exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Prestamo>> actualizarPrestamo(
            @PathVariable Long id,
            @Valid @RequestBody Prestamo prestamo) {
        Prestamo prestamoActualizado = prestamoService.actualizarPrestamo(id, prestamo);
        return ResponseEntity.ok(ApiResponse.success(prestamoActualizado, "Préstamo actualizado exitosamente"));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<ApiResponse<Prestamo>> devolverLibro(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.devolverLibro(id);
        return ResponseEntity.ok(ApiResponse.success(prestamo, "Libro devuelto exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPrestamo(@PathVariable Long id) {
        prestamoService.eliminarPrestamo(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Préstamo eliminado exitosamente"));
    }
}
