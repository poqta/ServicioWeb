
package ni.edu.uam.servicioweb.controller;

import ni.edu.uam.servicioweb.model.Usuario;
import ni.edu.uam.servicioweb.service.UsuarioService;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UsuariosController {

    @FXML
    private Button btnConsultar;

    @FXML
    private Label lblEstado;

    @FXML
    private TableView<Usuario> tblUsuarios;

    @FXML
    private TableColumn<Usuario, Integer> colId;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colUsername;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colTelefono;

    @FXML
    private TableColumn<Usuario, String> colCiudad;

    @FXML
    private TableColumn<Usuario, String> colEmpresa;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        colUsername.setCellValueFactory(
                new PropertyValueFactory<>("username"));

        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));

        colTelefono.setCellValueFactory(
                new PropertyValueFactory<>("telefono"));

        colCiudad.setCellValueFactory(
                new PropertyValueFactory<>("ciudad"));

        colEmpresa.setCellValueFactory(
                new PropertyValueFactory<>("empresa"));
    }

    @FXML
    private void consultarUsuarios() {

        btnConsultar.setDisable(true);
        lblEstado.setText("Consultando...");

        Task<List<Usuario>> tarea = new Task<>() {

            @Override
            protected List<Usuario> call() throws Exception {
                return usuarioService.obtenerUsuarios();
            }
        };

        tarea.setOnSucceeded(e -> {

            List<Usuario> usuarios = tarea.getValue();

            tblUsuarios.setItems(
                    FXCollections.observableArrayList(usuarios));

            lblEstado.setText(
                    "Se cargaron " + usuarios.size() + " usuarios");

            btnConsultar.setDisable(false);
        });

        tarea.setOnFailed(e -> {

            lblEstado.setText("Error al consultar la API");
            btnConsultar.setDisable(false);

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se pudo consultar la API");
            alerta.setContentText(
                    String.valueOf(tarea.getException().getMessage()));

            alerta.showAndWait();
        });

        Thread hilo = new Thread(tarea);
        hilo.setDaemon(true);
        hilo.start();
    }
}
