package com.biblioteca.bff.controller;

import com.biblioteca.bff.model.ApiResponse;
import com.biblioteca.bff.model.Usuario;
import com.biblioteca.bff.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Usuario>> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(usuarioCreado, "Usuario creado exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerUsuario(id);
        return ResponseEntity.ok(ApiResponse.success(usuario, "Usuario obtenido exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Usuario>>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(ApiResponse.success(usuarios, "Usuarios obtenidos exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(ApiResponse.success(usuarioActualizado, "Usuario actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuario eliminado exitosamente"));
    }
}
