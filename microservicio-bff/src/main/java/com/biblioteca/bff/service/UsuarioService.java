package com.biblioteca.bff.service;

import com.biblioteca.bff.client.ServerlessClient;
import com.biblioteca.bff.config.ServerlessConfig;
import com.biblioteca.bff.model.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {
    private final ServerlessClient client;
    private final ServerlessConfig config;
    private final ObjectMapper objectMapper;

    public UsuarioService(ServerlessClient client, ServerlessConfig config, ObjectMapper objectMapper) {
        this.client = client;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    public Usuario crearUsuario(Usuario usuario) {
        String url = config.getUsuarios().getUrl() + "/usuarios";
        ResponseEntity<Map> response = client.post(url, usuario, Map.class);
        return extraerData(response.getBody(), Usuario.class);
    }

    public Usuario obtenerUsuario(Long id) {
        String url = config.getUsuarios().getUrl() + "/usuarios/" + id;
        ResponseEntity<Map> response = client.get(url, Map.class);
        return extraerData(response.getBody(), Usuario.class);
    }

    public List<Usuario> listarUsuarios() {
        String url = config.getUsuarios().getUrl() + "/usuarios";
        ResponseEntity<Map> response = client.get(url, Map.class);
        return extraerDataList(response.getBody());
    }

    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        String url = config.getUsuarios().getUrl() + "/usuarios/" + id;
        ResponseEntity<Map> response = client.put(url, usuario, Map.class);
        return extraerData(response.getBody(), Usuario.class);
    }

    public void eliminarUsuario(Long id) {
        String url = config.getUsuarios().getUrl() + "/usuarios/" + id;
        client.delete(url, Map.class);
    }

    private Usuario extraerData(Map<String, Object> responseBody, Class<Usuario> clazz) {
        Object data = responseBody.get("data");
        return objectMapper.convertValue(data, clazz);
    }

    private List<Usuario> extraerDataList(Map<String, Object> responseBody) {
        Object data = responseBody.get("data");
        return objectMapper.convertValue(data, new TypeReference<List<Usuario>>() {});
    }
}
