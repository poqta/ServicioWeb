
package ni.edu.uam.servicioweb.controller;

import ni.edu.uam.servicioweb.model.Customer;
import ni.edu.uam.servicioweb.service.CustomerService;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class CustomerController {

    @FXML
    private Button btnConsultar;
    @FXML
    private Label lblEstado;
    @FXML
    private TableView<Customer> tblClientes;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colNombre;
    @FXML
    private TableColumn<Customer, String> colApellido;
    @FXML
    private TableColumn<Customer, String> colCorreo;
    @FXML
    private TableColumn<Customer, String> colTelefono;
    @FXML
    private TableColumn<Customer, String> colPais;
    @FXML
    private TableColumn<Customer, String> colCiudad;
    @FXML
    private TableColumn<Customer, Double> colIngreso;
    @FXML
    private TableColumn<Customer, Integer> colScore;
    @FXML
    private TableColumn<Customer, String> colDeuda;
    @FXML
    private TableColumn<Customer, String> colEstadoCliente;

    // El controller no llama a la API directamente: se la pide al service
    private final CustomerService servicio = new CustomerService();

    // JavaFX lo ejecuta una sola vez, cuando el FXML termina de cargarse
    @FXML
    public void initialize() {
        // Cada texto debe coincidir con un getter de Customer
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("city"));
        colIngreso.setCellValueFactory(new PropertyValueFactory<>("annualIncome"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("creditScore"));
        colDeuda.setCellValueFactory(new PropertyValueFactory<>("deuda"));
        colEstadoCliente.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void consultarClientes() {
        btnConsultar.setDisable(true);
        lblEstado.setText("Consultando...");

        // La llamada a la API va en otro hilo para que la ventana no se congele
        Task<List<Customer>> tarea = new Task<>() {
            @Override
            protected List<Customer> call() throws Exception {
                return servicio.obtenerClientes();
            }
        };

        // Si la consulta salió bien se llena la tabla
        tarea.setOnSucceeded(e -> {
            List<Customer> clientes = tarea.getValue();
            tblClientes.setItems(FXCollections.observableArrayList(clientes));
            lblEstado.setText("Se cargaron " + clientes.size() + " clientes");
            btnConsultar.setDisable(false);
        });

        // Si falló, se muestra un mensaje
        tarea.setOnFailed(e -> {
            Throwable error = tarea.getException();
            error.printStackTrace();
            lblEstado.setText("Error al consultar la API");
            btnConsultar.setDisable(false);

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se pudo consultar la API");
            alerta.setContentText(String.valueOf(error.getMessage()));
            alerta.showAndWait();
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }
}
