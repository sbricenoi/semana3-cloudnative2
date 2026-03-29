package com.biblioteca.bff.service;

import com.biblioteca.bff.client.ServerlessClient;
import com.biblioteca.bff.config.ServerlessConfig;
import com.biblioteca.bff.model.Prestamo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PrestamoService {
    private final ServerlessClient client;
    private final ServerlessConfig config;
    private final ObjectMapper objectMapper;

    public PrestamoService(ServerlessClient client, ServerlessConfig config, ObjectMapper objectMapper) {
        this.client = client;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public Prestamo crearPrestamo(Prestamo prestamo) {
        String url = config.getPrestamos().getUrl() + "/prestamos";
        ResponseEntity<Map> response = client.post(url, prestamo, Map.class);
        return extraerData(response.getBody(), Prestamo.class);
    }

    public Prestamo obtenerPrestamo(Long id) {
        String url = config.getPrestamos().getUrl() + "/prestamos/" + id;
        ResponseEntity<Map> response = client.get(url, Map.class);
        return extraerData(response.getBody(), Prestamo.class);
    }

    public List<Prestamo> listarPrestamos() {
        String url = config.getPrestamos().getUrl() + "/prestamos";
        ResponseEntity<Map> response = client.get(url, Map.class);
        return extraerDataList(response.getBody());
    }

    public List<Prestamo> listarPrestamosPorUsuario(Long idUsuario) {
        String url = config.getPrestamos().getUrl() + "/prestamos/usuario/" + idUsuario;
        ResponseEntity<Map> response = client.get(url, Map.class);
        return extraerDataList(response.getBody());
    }

    public Prestamo actualizarPrestamo(Long id, Prestamo prestamo) {
        String url = config.getPrestamos().getUrl() + "/prestamos/" + id;
        ResponseEntity<Map> response = client.put(url, prestamo, Map.class);
        return extraerData(response.getBody(), Prestamo.class);
    }

    public Prestamo devolverLibro(Long id) {
        String url = config.getPrestamos().getUrl() + "/prestamos/" + id + "/devolver";
        ResponseEntity<Map> response = client.put(url, null, Map.class);
        return extraerData(response.getBody(), Prestamo.class);
    }

    public void eliminarPrestamo(Long id) {
        String url = config.getPrestamos().getUrl() + "/prestamos/" + id;
        client.delete(url, Map.class);
    }

    private Prestamo extraerData(Map<String, Object> responseBody, Class<Prestamo> clazz) {
        Object data = responseBody.get("data");
        return objectMapper.convertValue(data, clazz);
    }

    private List<Prestamo> extraerDataList(Map<String, Object> responseBody) {
        Object data = responseBody.get("data");
        return objectMapper.convertValue(data, new TypeReference<List<Prestamo>>() {});
    }
}
