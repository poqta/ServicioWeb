package ni.edu.uam.servicioweb.service;
import ni.edu.uam.servicioweb.model.Usuario;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
public class UsuarioService {

    // lo que va luego de la pleca son endpoint, representa un metodo de consulta dentro de mi aplicacion
    private static final String URL = "https://jsonplaceholder.typicode.com/users";

    public List<Usuario> obtenerUsuarios() throws IOException, InterruptedException {
        // Comunicarse con el servicio web para obtener la lista de usuarios
        HttpClient cliente = HttpClient.newHttpClient();

        // Es la peticion que se va a realizar al servicio web, en este caso es un GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = cliente.send(
                request, HttpResponse.BodyHandlers.ofString()
        );

        // Verificar el codigo de respuesta (Debe ser 200 para éxito)
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());

        // Validar que el nodo principal sea un arreglo JSON
        if (!rootNode.isArray()) {
            throw new RuntimeException("Failed: La respuesta no es un arreglo JSON válido.");
        }

        List<Usuario> usuarios = new ArrayList<>();

        // recorrer el arreglo JSON para mapearlo a objetos Usuario
        for (JsonNode datos : rootNode) {
            Usuario usuario = new Usuario(
                    datos.get("id").asInt(),
                    datos.get("name").asText(),
                    datos.get("username").asText(),
                    datos.get("email").asText(),
                    datos.get("phone").asText(),
                    datos.get("address").get("city").asText(),
                    datos.get("company").get("name").asText()
            );
            usuarios.add(usuario);
        }
        return usuarios;
    }
}