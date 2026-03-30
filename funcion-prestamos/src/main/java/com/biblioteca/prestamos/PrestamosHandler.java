package com.biblioteca.prestamos;

import com.biblioteca.prestamos.dao.PrestamoDAO;
import com.biblioteca.prestamos.model.ApiResponse;
import com.biblioteca.prestamos.model.Prestamo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;

import static spark.Spark.*;

public class PrestamosHandler {
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    private static final PrestamoDAO prestamoDAO = new PrestamoDAO();

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8082"));
        port(port);

        configurarCORS();
        configurarRutas();

        System.out.println("Función Préstamos iniciada en puerto " + port);
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
        path("/prestamos", () -> {
            post("", PrestamosHandler::crearPrestamo);
            get("", PrestamosHandler::listarPrestamos);
            get("/:id", PrestamosHandler::obtenerPrestamo);
            put("/:id", PrestamosHandler::actualizarPrestamo);
            put("/:id/devolver", PrestamosHandler::devolverLibro);
            delete("/:id", PrestamosHandler::eliminarPrestamo);
        });

        get("/prestamos/usuario/:idUsuario", PrestamosHandler::listarPrestamosPorUsuario);

        get("/health", (req, res) -> {
            res.type("application/json");
            return gson.toJson(ApiResponse.success(null, "Servicio de préstamos operativo"));
        });
    }

    private static String crearPrestamo(Request req, Response res) {
        try {
            Prestamo prestamo = gson.fromJson(req.body(), Prestamo.class);
            
            if (!validarPrestamo(prestamo)) {
                res.status(400);
                return gson.toJson(ApiResponse.error("Datos de préstamo incompletos"));
            }

            Prestamo prestamoCreado = prestamoDAO.crear(prestamo);
            res.status(201);
            return gson.toJson(ApiResponse.success(prestamoCreado, "Préstamo registrado exitosamente"));
        } catch (SQLException e) {
            if (e.getMessage().contains("no disponible")) {
                res.status(400);
                return gson.toJson(ApiResponse.error(e.getMessage()));
            }
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al crear préstamo: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al crear préstamo: " + e.getMessage()));
        }
    }

    private static String obtenerPrestamo(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            Prestamo prestamo = prestamoDAO.obtenerPorId(id);
            
            if (prestamo == null) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Préstamo no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(prestamo, "Préstamo obtenido exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de préstamo inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al obtener préstamo: " + e.getMessage()));
        }
    }

    private static String listarPrestamos(Request req, Response res) {
        try {
            List<Prestamo> prestamos = prestamoDAO.listarTodos();
            return gson.toJson(ApiResponse.success(prestamos, "Préstamos obtenidos exitosamente"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al listar préstamos: " + e.getMessage()));
        }
    }

    private static String listarPrestamosPorUsuario(Request req, Response res) {
        try {
            Long idUsuario = Long.parseLong(req.params(":idUsuario"));
            List<Prestamo> prestamos = prestamoDAO.listarPorUsuario(idUsuario);
            return gson.toJson(ApiResponse.success(prestamos, "Préstamos del usuario obtenidos exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de usuario inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al listar préstamos: " + e.getMessage()));
        }
    }

    private static String actualizarPrestamo(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            Prestamo prestamo = gson.fromJson(req.body(), Prestamo.class);
            
            if (!validarPrestamo(prestamo)) {
                res.status(400);
                return gson.toJson(ApiResponse.error("Datos de préstamo incompletos"));
            }

            Prestamo prestamoActualizado = prestamoDAO.actualizar(id, prestamo);
            
            if (prestamoActualizado == null) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Préstamo no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(prestamoActualizado, "Préstamo actualizado exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de préstamo inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al actualizar préstamo: " + e.getMessage()));
        }
    }

    private static String devolverLibro(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            Prestamo prestamo = prestamoDAO.devolverLibro(id);
            
            if (prestamo == null) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Préstamo no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(prestamo, "Libro devuelto exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de préstamo inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al devolver libro: " + e.getMessage()));
        }
    }

    private static String eliminarPrestamo(Request req, Response res) {
        try {
            Long id = Long.parseLong(req.params(":id"));
            boolean eliminado = prestamoDAO.eliminar(id);
            
            if (!eliminado) {
                res.status(404);
                return gson.toJson(ApiResponse.error("Préstamo no encontrado"));
            }
            
            return gson.toJson(ApiResponse.success(null, "Préstamo eliminado exitosamente"));
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson(ApiResponse.error("ID de préstamo inválido"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(ApiResponse.error("Error al eliminar préstamo: " + e.getMessage()));
        }
    }

    private static boolean validarPrestamo(Prestamo prestamo) {
        return prestamo != null &&
               prestamo.getIdUsuario() != null &&
               prestamo.getIdLibro() != null &&
               prestamo.getFechaDevolucionEsperada() != null;
    }
}
