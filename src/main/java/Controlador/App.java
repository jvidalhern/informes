package Controlador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static FXMLLoader loader;

    @Override
    public void start(Stage stage) throws IOException {
        loader = loadFXML("vista_principal");
        scene = (new Scene(loader.load(), 745, 485));
        stage.setScene(scene);
        stage.setOnShown((e) -> {
            GestorInformeController controlador = loader.getController();
            controlador.inicializar();
        });
        stage.setTitle("GENERADOR DE INFORMES DE JUAN JOSÉ VIDAL");
        stage.getIcons().add(new Image("/report/img/icono_aplicacion_blanco.png"));
        stage.show();
    }


    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("Vista/" + fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

}
