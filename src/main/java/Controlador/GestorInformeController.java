package Controlador;

import Modelo.AccesoDatos;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JasperFillManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import net.sf.jasperreports.view.JasperViewer;
import java.util.HashMap;
import java.awt.Desktop;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import net.sf.jasperreports.engine.export.JRXlsExporter;

public class GestorInformeController {

    @FXML
    private ComboBox cbAlumno;

    @FXML
    private ComboBox cbModulo;

    @FXML
    private Button btnGenerarInforme;

    ObservableList<String> oListAlumno;
    ObservableList<String> oListModulo;
    ArrayList<String> alumnos = new ArrayList<>();
    ArrayList<String> modulos = new ArrayList<>();

    @FXML
    private ToggleGroup rbInformes;
    @FXML
    private RadioButton rbInformeAlumnado;
    @FXML
    private RadioButton rbInformeModulo;
    @FXML
    private RadioButton rbActaCurso;
    @FXML
    private RadioButton rbInformeNotasFinales;
    @FXML
    private CheckBox chInformePDF;
    @FXML
    private CheckBox chInformeXLS;
    @FXML
    private Button btnExportar;

    /**
     * Inicializa el controlador configurando los datos y elementos de la
     * interfaz.
     *
     * Este método carga la lista de alumnos y módulos en sus respectivos
     * ComboBoxes, y configura los RadioButtons necesarios para la interfaz de
     * usuario.
     */
    public void inicializar() {
        cargarAlumnos();
        cargarModulos();
        configurarRadioButtons();
    }

    /**
     * Carga los nombres y apellidos de los alumnos en el ComboBox
     * correspondiente.
     *
     * Este método obtiene la lista de alumnos desde la base de datos utilizando
     * el método `buscarAlumnos` de la clase `AccesoDatos`, agrega los apellidos
     * y nombres de los alumnos en el formato "Apellidos, Nombre" a una lista
     * observable y configura el ComboBox `cbAlumno` con estos nombres y
     * apellidos.
     */
    private void cargarAlumnos() {
        AccesoDatos.buscarAlumnos().forEach(alumno -> alumnos.add(alumno.getApellidos() + ", " + alumno.getNombre()));
        oListAlumno = FXCollections.observableArrayList(alumnos);
        cbAlumno.setItems(oListAlumno);
    }

    /**
     * Carga los nombres de los módulos en el ComboBox correspondiente.
     *
     * Este método obtiene la lista de módulos desde la base de datos utilizando
     * el método `buscarModulos` de la clase `AccesoDatos`, agrega los nombres
     * de los módulos a una lista observable y configura el ComboBox `cbModulo`
     * con estos nombres.
     */
    private void cargarModulos() {
        AccesoDatos.buscarModulos().forEach(modulo -> modulos.add(modulo.getNombreModulo()));
        oListModulo = FXCollections.observableArrayList(modulos);
        cbModulo.setItems(oListModulo);
    }

    /**
     * Configura los RadioButtons para manejar la selección de informes.
     *
     * Este método agrega un listener al evento de cambio de selección de los
     * RadioButtons en el grupo `rbInformes`. Dependiendo del RadioButton
     * seleccionado, se habilitan o deshabilitan los ComboBoxes `cbAlumno` y
     * `cbModulo` según las necesidades del informe seleccionado.
     */
    private void configurarRadioButtons() {
        rbInformes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (rbInformes.getSelectedToggle() != null) {
                RadioButton selectedRadioButton = (RadioButton) rbInformes.getSelectedToggle();
                if (selectedRadioButton == rbInformeAlumnado) {
                    cbAlumno.setDisable(true);
                    cbModulo.setDisable(true);
                } else if (selectedRadioButton == rbInformeModulo) {
                    cbAlumno.setDisable(false);
                    cbModulo.setDisable(false);
                } else if (selectedRadioButton == rbActaCurso) {
                    cbAlumno.setDisable(true);
                    cbModulo.setDisable(true);
                } else if (selectedRadioButton == rbInformeNotasFinales) {
                    cbAlumno.setDisable(false);
                    cbModulo.setDisable(true);
                }
            }
        });
    }

    /**
     * Maneja el evento de clic en el botón para generar informes.
     *
     * Este método verifica qué RadioButton está seleccionado en el grupo
     * `rbInformes`. Dependiendo del RadioButton seleccionado, se genera un
     * informe con la plantilla correspondiente, utilizando los parámetros
     * necesarios obtenidos mediante el método `obtenerParametros`. Si se
     * selecciona el informe de alumnado, se genera un informe de alumnos. Si se
     * selecciona el informe de módulo, se genera un informe de notas del grupo.
     * Si se selecciona el acta de curso, se genera un informe de notas finales.
     * Si se selecciona el informe de notas finales, se genera un informe
     * detallado para un alumno específico. El informe se genera solo si se
     * proporcionan todos los parámetros necesarios.
     *
     * @param event El evento de acción que desencadena el método.
     */
    @FXML
    private void btnOnGenerarInforme(ActionEvent event) {
        if (rbInformes.getSelectedToggle() != null) {
            RadioButton selectedRadioButton = (RadioButton) rbInformes.getSelectedToggle();
            if (selectedRadioButton == rbInformeAlumnado) {
                generarInforme("alumnos.jasper", new HashMap<>());
            } else if (selectedRadioButton == rbInformeModulo) {
                if (obtenerParametros("ALUMNO", "MODULO") != null) {
                    generarInforme("notasGrupo.jasper", obtenerParametros("ALUMNO", "MODULO"));
                }
            } else if (selectedRadioButton == rbActaCurso) {
                generarInforme("notasFinales.jasper", new HashMap<>());
            } else if (selectedRadioButton == rbInformeNotasFinales) {
                if (obtenerParametros("ALUMNO") != null) {
                    generarInforme("informe.jasper", obtenerParametros("ALUMNO"));
                }
            }
        }
    }

    /**
     * Maneja el evento de clic en el botón para exportar informes.
     *
     * Este método verifica si se ha seleccionado al menos uno de los formatos
     * de exportación (PDF o XLS). Luego, verifica qué RadioButton está
     * seleccionado en el grupo `rbInformes`. Dependiendo del RadioButton
     * seleccionado y de los formatos de exportación elegidos, se exporta el
     * informe correspondiente. Si se selecciona el informe de alumnado y al
     * menos un formato de exportación, se exporta un informe de alumnos en el
     * formato seleccionado. Si se selecciona el informe de módulo y al menos un
     * formato de exportación, y se proporcionan los parámetros necesarios, se
     * exporta un informe de notas del grupo en el formato seleccionado. Si se
     * selecciona el acta de curso y al menos un formato de exportación, se
     * exporta un informe de notas finales en el formato seleccionado. Si se
     * selecciona el informe de notas finales y al menos un formato de
     * exportación, y se proporciona el parámetro necesario, se exporta un
     * informe detallado para un alumno específico en el formato seleccionado.
     * Si no se selecciona ningún formato de exportación, se muestra una alerta.
     *
     * @param event El evento de acción que desencadena el método.
     */
    @FXML
    private void btnOnExportar(ActionEvent event) {
        if (chInformePDF.isSelected() || chInformeXLS.isSelected()) {
            if (rbInformes.getSelectedToggle() != null) {
                RadioButton selectedRadioButton = (RadioButton) rbInformes.getSelectedToggle();
                if (selectedRadioButton == rbInformeAlumnado) {
                    exportarInforme("alumnos.jasper", "InformeAlumnado", new HashMap<>());
                } else if (selectedRadioButton == rbInformeModulo) {
                    if (obtenerParametros("ALUMNO", "MODULO") != null) {
                        exportarInforme("notasGrupo.jasper", "InformeModulo_" + obtenerValorComboBox(cbAlumno) + "_" + obtenerValorComboBox(cbModulo), obtenerParametros("ALUMNO", "MODULO"));
                    }
                } else if (selectedRadioButton == rbActaCurso) {
                    exportarInforme("notasFinales.jasper", "ActaCurso", new HashMap<>());
                } else if (selectedRadioButton == rbInformeNotasFinales) {
                    if (obtenerParametros("ALUMNO") != null) {
                        exportarInforme("informe.jasper", "InformeNotas", obtenerParametros("ALUMNO"));
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ALERTA");
            alert.setContentText("Seleccione un formato para exportar");
            alert.show();
        }
    }

    /**
     * Genera y muestra un informe JasperReports.
     *
     * Este método rellena un informe JasperReports utilizando la plantilla y
     * los parámetros proporcionados, y luego muestra el informe en un visor de
     * informes.
     *
     * @param templateName El nombre de la plantilla JasperReports (sin la ruta
     * completa).
     * @param parameters Un mapa de parámetros a pasar al informe JasperReports
     * para su relleno.
     */
    private void generarInforme(String templateName, Map<String, Object> parameters) {
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport("src/main/resources/report/" + templateName, parameters, AccesoDatos.getConnection());
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(GestorInformeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exporta un informe basado en una plantilla JasperReports a uno o más
     * formatos.
     *
     * Este método rellena un informe JasperReports usando la plantilla y los
     * parámetros proporcionados, luego exporta el informe a PDF y/o Excel según
     * las opciones seleccionadas por el usuario.
     *
     * @param templateName El nombre de la plantilla JasperReports (sin la ruta
     * completa).
     * @param outputFileName El nombre del archivo de salida (sin la extensión)
     * donde se guardarán los informes exportados.
     * @param parameters Un mapa de parámetros a pasar al informe JasperReports
     * para su relleno.
     */
    private void exportarInforme(String templateName, String outputFileName, Map<String, Object> parameters) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport("src/main/resources/report/" + templateName, parameters, AccesoDatos.getConnection());
            if (chInformePDF.isSelected()) {
                exportToPDF(jasperPrint, outputFileName);
            }
            if (chInformeXLS.isSelected()) {
                exportToXLS(jasperPrint, outputFileName);
            }
        } catch (JRException | IOException ex) {
            Logger.getLogger(GestorInformeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene un mapa de parámetros basado en las claves proporcionadas.
     *
     * Este método crea y retorna un mapa de parámetros donde las claves son los
     * nombres de los parámetros y los valores son los elementos seleccionados
     * de los ComboBoxes correspondientes. Si una clave es "ALUMNO", el método
     * verifica que haya una selección en el ComboBox de alumnos (`cbAlumno`).
     * Si no hay una selección, se muestra una alerta y el método retorna null.
     * Similarmente, si la clave es "MODULO", se verifica el ComboBox de módulos
     * (`cbModulo`).
     *
     * @param keys Las claves para los parámetros a obtener (pueden ser "ALUMNO"
     * y/o "MODULO").
     * @return Un mapa de parámetros con las claves proporcionadas y sus valores
     * correspondientes, o null si alguna selección requerida no está presente.
     */
    private Map<String, Object> obtenerParametros(String... keys) {
        Map<String, Object> parameters = new HashMap<>();
        for (String key : keys) {
            if ("ALUMNO".equals(key)) {
                if (cbAlumno.getSelectionModel().getSelectedIndex() != -1) {
                    parameters.put("ALUMNO", obtenerValorComboBox(cbAlumno));
                } else {
                    mostrarAlerta("Por favor, seleccione un alumno.");
                    return null;
                }
            } else if ("MODULO".equals(key)) {
                if (cbModulo.getSelectionModel().getSelectedIndex() != -1) {
                    parameters.put("MODULO", cbModulo.getSelectionModel().getSelectedItem().toString());
                } else {
                    mostrarAlerta("Por favor, seleccione un módulo.");
                    return null;
                }
            }
        }
        return parameters;
    }

    /**
     * Obtiene el valor del ComboBox después de la coma.
     *
     * Este método devuelve el valor del ComboBox seleccionado después de la
     * coma. Si no se ha seleccionado ningún elemento en el ComboBox, devuelve
     * una cadena vacía.
     *
     * @param comboBox El ComboBox del que se desea obtener el valor.
     * @return El valor del ComboBox después de la coma, o una cadena vacía si
     * no hay selección.
     */
    private String obtenerValorComboBox(ComboBox<String> comboBox) {
        String selectedItem = comboBox.getSelectionModel().getSelectedItem();        
        return selectedItem.substring(selectedItem.indexOf(",") + 2);
    }

    /**
     * Método para mostrar un aviso al usuario
     *
     * @param mensaje que se muestra
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AVISO");
        alert.setContentText(mensaje);
        alert.show();
    }

    /**
     * Método para exportar el informe a PDF. Este método toma un objeto
     * JasperPrint y un nombre de archivo de salida, y genera un archivo PDF en
     * la ubicación especificada. También intenta abrir el archivo PDF generado
     * utilizando la aplicación predeterminada del sistema.
     *
     * @param jasperPrint El objeto JasperPrint que contiene el diseño del
     * informe y los datos a exportar.
     * @param outputFileName El nombre del archivo de salida (sin la extensión)
     * donde se guardará el PDF.
     * @throws JRException Si ocurre un error durante la exportación del informe
     * a PDF.
     * @throws IOException Si ocurre un error durante la creación del archivo o
     * al intentar abrir el archivo PDF.
     */
    public void exportToPDF(JasperPrint jasperPrint, String outputFileName) throws JRException, IOException {

        File outputFile = new File(outputFileName + ".pdf");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            exporter.exportReport();
        }

        try {
            Desktop.getDesktop().open(outputFile);
        } catch (IOException ex) {
        }
    }

    /**
     * Método para exportar el informe a Excel.
     *
     * Este método toma un objeto JasperPrint y un nombre de archivo de salida,
     * genera un archivo Excel (.xls) en la ubicación especificada y luego
     * intenta abrir el archivo Excel generado utilizando la aplicación
     * predeterminada del sistema.
     *
     * @param jasperPrint El objeto JasperPrint que contiene el diseño del
     * informe y los datos a exportar.
     * @param outputFileName El nombre del archivo de salida (sin la extensión)
     * donde se guardará el archivo Excel.
     * @throws JRException Si ocurre un error durante la exportación del informe
     * a Excel.
     * @throws IOException Si ocurre un error durante la creación del archivo o
     * al intentar abrir el archivo Excel.
     */
    public void exportToXLS(JasperPrint jasperPrint, String outputFileName) throws JRException, IOException {

        File outputFile = new File(outputFileName + ".xls");

        // Crear el exportador de Excel
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE, outputFile);

        // Exportar el JasperPrint a un archivo Excel
        exporter.exportReport();

        try {
            Desktop.getDesktop().open(outputFile);
        } catch (IOException ex) {
        }
    }
}
