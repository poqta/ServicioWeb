
package ni.edu.uam.servicioweb.service;

import ni.edu.uam.servicioweb.model.Customer;
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

public class CustomerService {

    public List<Customer> obtenerClientes()
            throws IOException, InterruptedException {

        String url = System.getenv("SUPABASE_URL");
        String apiKey = System.getenv("SUPABASE_API_KEY");

        if (url == null || url.isBlank()
                || apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Faltan variables de entorno de Supabase"
            );
        }

        // Cliente HTTP para consultar el servicio web
        HttpClient cliente = HttpClient.newHttpClient();

        // Petición GET a la API de Supabase
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", apiKey)
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpResponse<String> response = cliente.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        // Verificar la respuesta de la API
        if (response.statusCode() != 200) {
            throw new IOException(
                    "Error al consultar clientes. Código HTTP: "
                            + response.statusCode()
            );
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode datos = mapper.readTree(response.body());

        if (!datos.isArray()) {
            throw new IOException(
                    "La respuesta no contiene una lista de clientes"
            );
        }

        List<Customer> clientes = new ArrayList<>();

        // Convertir cada registro JSON en un objeto Customer
        for (JsonNode dato : datos) {

            Customer customer = new Customer(
                    dato.path("id").asInt(),
                    dato.path("first_name").asText(),
                    dato.path("last_name").asText(),
                    dato.path("email").asText(),
                    dato.path("phone").asText(),
                    dato.path("date_of_birth").asText(),
                    dato.path("country").asText(),
                    dato.path("city").asText(),
                    dato.path("address").asText(),
                    dato.path("annual_income").asDouble(),
                    dato.path("credit_score").asInt(),
                    dato.path("has_unpaid_debt").asBoolean(),
                    dato.path("unpaid_debt_amount").asDouble(),
                    dato.path("status").asText(),
                    dato.path("created_at").asText(),
                    dato.path("updated_at").asText()
            );

            clientes.add(customer);
        }

        return clientes;
    }
}
