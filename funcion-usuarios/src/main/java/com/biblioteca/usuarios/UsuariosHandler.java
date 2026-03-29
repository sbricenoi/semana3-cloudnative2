package com.biblioteca.usuarios;

import com.biblioteca.usuarios.dao.UsuarioDAO;
import com.biblioteca.usuarios.model.ApiResponse;
import com.biblioteca.usuarios.model.Usuario;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

public class UsuariosHandler {
    private static final Gson gson = new Gson();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8081"));
        port(port);

        configurarCORS();
        configurarRutas();

        System.out.println("Función Usuarios iniciada en puerto " + port);
    }

    private static void configurarCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Content-Type", "application/json");
        });
    }

    private static void configurarRutas() {
        path("/usuarios", () -> {
            post("", UsuariosHandler::crearUsuario);
            get("", UsuariosHandler::listarUsuarios);
            get("/:id", UsuariosHandler::obtenerUsuario);
            put("/:id", UsuariosHandler::actualizarUsuario);
            delete("/:id", UsuariosHandler::eliminarUsuario);
        });

        get("/health", (req, res) -> {
            res.type("application/json");
            return gson.toJson(ApiResponse.success(null, "Servicio de usuarios operativo"));
        });
    }

    private static String crearUsuario(Request req, Response res) {
        try {
            Usuario usuario = gson.fromJson(req.body(), Usuario.class);
            
            if (!validarUsuario(usuario)) {
                res.status(400);
                return gson.toJson(ApiResponse.error("Datos de usuario incompletos"));
            }

            Usuario usuarioCreado = usuarioDAO.crear(usuario);
            res.status(201);
            return gson.toJson(ApiResponse.success(usuarioCreado, "Usuario creado exitosamente"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al crear usuario: " + e.getMessage()));
        }
    }

    private static String obtenerUsuario(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            Usuario usuario = usuarioDAO.obtenerPorId(id);
            
            if (usuario == null) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Usuario no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(usuario, "Usuario obtenido exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de usuario inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al obtener usuario: " + e.getMessage()));
        }
    }

    private static String listarUsuarios(Request req, Response res) {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            return gson.toJson(ApiResponse.success(usuarios, "Usuarios obtenidos exitosamente"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al listar usuarios: " + e.getMessage()));
        }
    }

    private static String actualizarUsuario(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            Usuario usuario = gson.fromJson(req.body(), Usuario.class);
            
            if (!validarUsuario(usuario)) {
                res.status(400);
                return gson.toJson(ApiResponse.error("Datos de usuario incompletos"));
            }

            Usuario usuarioActualizado = usuarioDAO.actualizar(id, usuario);
            
            if (usuarioActualizado == null) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Usuario no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(usuarioActualizado, "Usuario actualizado exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de usuario inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al actualizar usuario: " + e.getMessage()));
        }
    }

    private static String eliminarUsuario(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            boolean eliminado = usuarioDAO.eliminar(id);
            
            if (!eliminado) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Usuario no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(null, "Usuario eliminado exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de usuario inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al eliminar usuario: " + e.getMessage()));
        }
    }

    private static boolean validarUsuario(Usuario usuario) {
        return usuario != null &&
               usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty() &&
               usuario.getApellido() != null && !usuario.getApellido().trim().isEmpty() &&
               usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty() &&
               usuario.getRut() != null && !usuario.getRut().trim().isEmpty();
    }
}
