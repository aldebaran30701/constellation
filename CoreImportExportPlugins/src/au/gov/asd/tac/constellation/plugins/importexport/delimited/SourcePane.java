/*
 * Copyright 2010-2020 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.plugins.importexport.delimited;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.manager.GraphManager;
import au.gov.asd.tac.constellation.graph.schema.SchemaFactory;
import au.gov.asd.tac.constellation.graph.schema.SchemaFactoryUtilities;
import au.gov.asd.tac.constellation.plugins.gui.PluginParametersPane;
import au.gov.asd.tac.constellation.plugins.importexport.GraphDestination;
import au.gov.asd.tac.constellation.plugins.importexport.ImportDestination;
import au.gov.asd.tac.constellation.plugins.importexport.SchemaDestination;
import au.gov.asd.tac.constellation.plugins.importexport.delimited.parser.ImportFileParser;
import au.gov.asd.tac.constellation.plugins.importexport.delimited.parser.InputSource;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.utilities.color.ConstellationColor;
import au.gov.asd.tac.constellation.utilities.icon.UserInterfaceIconProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * The SourcePane provides the UI necessary to allow the user to specify where
 * the imported data should come from. This is typically done by selecting a
 * file.
 *
 * @author sirius
 */
public class SourcePane extends GridPane {

    private static File DEFAULT_DIRECTORY = new File(System.getProperty("user.home"));
    private static final Logger LOGGER = Logger.getLogger(SourcePane.class.getName());

    private final ComboBox<ImportDestination<?>> graphComboBox;
    private final ComboBox<ImportFileParser> importFileParserComboBox;
    private final CheckBox schemaCheckBox;
    private final Pane parametersPane = new Pane();
    private final ListView<File> fileListView = new ListView<>();
    private final ImportController importController;

    public SourcePane(final ImportController importController) {
        this.importController = importController;

        setMinHeight(USE_PREF_SIZE);
        setMinWidth(0);
        setPadding(new Insets(5));
        setHgap(10);
        setVgap(10);

        ColumnConstraints column0Constraints = new ColumnConstraints();
        column0Constraints.setHgrow(Priority.NEVER);

        ColumnConstraints column1Constraints = new ColumnConstraints();
        column1Constraints.setHgrow(Priority.ALWAYS);
        column1Constraints.setMinWidth(0);

        ColumnConstraints column2Constraints = new ColumnConstraints();
        column2Constraints.setHgrow(Priority.NEVER);

        getColumnConstraints().addAll(column0Constraints, column1Constraints, column2Constraints);

        final Label fileLabel = new Label("Files:");
        GridPane.setConstraints(fileLabel, 0, 0, 1, 1, HPos.LEFT, VPos.TOP);

        fileListView.setMinHeight(0);
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fileListView.setMaxWidth(Double.MAX_VALUE);
        fileListView.setMinWidth(0);

        final ScrollPane fileScrollPane = new ScrollPane();
        fileScrollPane.setMaxHeight(100);
        fileScrollPane.setMaxWidth(Double.MAX_VALUE);
        fileScrollPane.setPrefHeight(100);
        fileScrollPane.setFitToWidth(true);
        fileScrollPane.setMinWidth(0);
        fileScrollPane.setContent(fileListView);
        fileScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        fileScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        GridPane.setConstraints(fileScrollPane, 1, 0);

        final VBox fileButtonBox = new VBox();
        fileButtonBox.setFillWidth(true);
        fileButtonBox.setSpacing(10);
        GridPane.setConstraints(fileButtonBox, 2, 0);

        final Button fileAddButton = new Button("", new ImageView(UserInterfaceIconProvider.ADD_ALTERNATE.buildImage(40, ConstellationColor.EMERALD.getJavaColor())));
        final Button fileRemoveButton = new Button("", new ImageView(UserInterfaceIconProvider.REMOVE_ALTERNATE.buildImage(40, ConstellationColor.CHERRY.getJavaColor())));
        fileRemoveButton.setDisable(true);

        fileButtonBox.getChildren().addAll(fileAddButton, fileRemoveButton);

        getChildren().addAll(fileLabel, fileScrollPane, fileButtonBox);

        fileListView.setOnMouseClicked((MouseEvent t) -> {
            fileRemoveButton.setDisable(fileListView.getSelectionModel().getSelectedItems().isEmpty());
        });

        fileListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends File> c) -> {
            ObservableList<File> allFiles = fileListView.getItems();
            ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
            importController.setFiles(allFiles, selectedFiles.isEmpty() ? null : selectedFiles.get(0));
        });

        fileAddButton.setOnAction((ActionEvent t) -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(DEFAULT_DIRECTORY);

            final ImportFileParser parser = SourcePane.this.importFileParserComboBox.getSelectionModel().getSelectedItem();
            if (parser != null) {
                final ExtensionFilter extensionFilter = parser.getExtensionFilter();
                if (extensionFilter != null) {
                    fileChooser.getExtensionFilters().add(extensionFilter);
                    fileChooser.setSelectedExtensionFilter(extensionFilter);
                }
            }
            fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.*"));

            final List<File> newFiles = fileChooser.showOpenMultipleDialog(SourcePane.this.getScene().getWindow());

            if (newFiles != null) {
                if (!newFiles.isEmpty()) {
                    DEFAULT_DIRECTORY = newFiles.get(0).getParentFile();
                    SourcePane.this.importFileParserComboBox.setDisable(true);
                }
                final ObservableList<File> files = FXCollections.observableArrayList(fileListView.getItems());
                final StringBuilder sb = new StringBuilder();
                sb.append("The following files could not be parsed and have been excluded from import set:");
                boolean foundInvalidFile = false;
                for (final File file : newFiles) {
                    // Iterate over files and attempt to parse/preview, if a failure is detected don't add the file to the
                    // set of files to import.
                    try {
                        parser.preview(new InputSource(file), null, 100);
                        files.add(file);
                    } catch (IOException ex) {
                        foundInvalidFile = true;
                        LOGGER.log(Level.INFO, "Unable to parse the file {0}, excluding from import set.", new Object[]{file.toString()});
                        sb.append("\n    " + file.toString());
                    }
                }
                // If at least one file was found to be invalid then raise a dialog to indicate to user of import failures.
                if (foundInvalidFile) {
                    importController.displayAlert("Invalid file(s) found", sb.toString(), Alert.AlertType.WARNING);
                }
                fileListView.setItems(files);
                if (!newFiles.isEmpty()) {
                    fileListView.getSelectionModel().select(newFiles.get(0));
                    fileListView.requestFocus();
                }
                final ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
                importController.setFiles(files, selectedFiles.isEmpty() ? null : selectedFiles.get(0));
            }
        });

        fileRemoveButton.setOnAction((ActionEvent t) -> {
            final ObservableList<File> selectedFiles = fileListView.getSelectionModel().getSelectedItems();
            final ObservableList<File> allFiles = fileListView.getItems();
            final ObservableList<File> files = FXCollections.observableArrayList(allFiles);
            files.removeAll(selectedFiles);
            fileListView.setItems(files);
            importController.setFiles(files, null);
            if (files.isEmpty()) {
                SourcePane.this.importFileParserComboBox.setDisable(false);
            }
        });

        final Label destinationLabel = new Label("Destination:");
        graphComboBox = new ComboBox<>();
        updateDestinationGraphCombo();

        // IMPORT FILE PARSER
        final Label importFileParserLabel = new Label("Import File Parser:");

        final ObservableList<ImportFileParser> parsers = FXCollections.observableArrayList();
        parsers.addAll(ImportFileParser.getParsers().values());
        importFileParserComboBox = new ComboBox<>();
        importFileParserComboBox.setItems(parsers);
        importFileParserComboBox.getSelectionModel().selectFirst();
        importFileParserComboBox.setOnAction((final ActionEvent t) -> {
            importController.setImportFileParser(importFileParserComboBox.getSelectionModel().getSelectedItem());
        });

        //SCHEMA
        final Label schemaLabel = new Label("Initialise With Schema:");

        schemaCheckBox = new CheckBox();
        schemaCheckBox.setSelected(importController.isSchemaInitialised());
        schemaCheckBox.setOnAction((final ActionEvent event) -> {
            importController.setSchemaInitialised(schemaCheckBox.isSelected());
        });

        final ToolBar optionsBox = new ToolBar();
        optionsBox.setMinWidth(0);
        GridPane.setConstraints(optionsBox, 0, 1, 3, 1);
        optionsBox.getItems().addAll(destinationLabel, graphComboBox, importFileParserLabel, importFileParserComboBox, schemaLabel, schemaCheckBox);
        getChildren().add(optionsBox);
    }

    public void setParameters(final PluginParameters parameters) {
        parametersPane.getChildren().clear();
        if (parameters != null) {
            final PluginParametersPane pluginParametersPane = PluginParametersPane.buildPane(parameters, null);
            parametersPane.getChildren().add(pluginParametersPane);
        }
    }

    /**
     * Allow a file to be removed from fileListView. This would be triggered by
     * code in InputController if the file was found to be missing or invalid -
     * these checks are triggered when a new file is selected in the
     * fileListView.
     *
     * @param file The file to remove.
     */
    public void removeFile(File file) {
        final ObservableList<File> files = fileListView.getItems();
        files.remove(file);
        fileListView.setItems(files);
    }

    /**
     * The import controller has been modified: update the GUI to match.
     *
     * @param importController The ImportController.
     */
    void update(final ImportController importController) {
        graphComboBox.getSelectionModel().select(importController.getDestination());
        importFileParserComboBox.getSelectionModel().select(importController.getImportFileParser());
        schemaCheckBox.setSelected(importController.isSchemaInitialised());
    }

    /**
     * Return the selected destination
     *
     * @return ImportDestination destination
     */
    public final ImportDestination<?> getDestination() {
        return graphComboBox.getSelectionModel().getSelectedItem();
    }

    /**
     * Helper method to update the destination graph combo box. Also used for
     * value loading when this pane is being initialized.
     */
    public void updateDestinationGraphCombo() {
        // previousDestinationObject is the previously selected item in the combobox
        final ImportDestination<?> previousDestinationObject = graphComboBox.getSelectionModel().getSelectedItem();
        final ObservableList<ImportDestination<?>> destinations = FXCollections.observableArrayList();

        final Map<String, Graph> graphs = GraphManager.getDefault().getAllGraphs();
        final Graph activeGraph = GraphManager.getDefault().getActiveGraph();
        ImportDestination<?> defaultDestination = null;
        for (final Graph graph : graphs.values()) {
            final GraphDestination destination = new GraphDestination(graph);
            destinations.add(destination);
            if (graph == activeGraph) {
                defaultDestination = destination;
            }
        }

        final Map<String, SchemaFactory> schemaFactories = SchemaFactoryUtilities.getSchemaFactories();
        for (final SchemaFactory schemaFactory : schemaFactories.values()) {
            final SchemaDestination destination = new SchemaDestination(schemaFactory);
            destinations.add(destination);
            if (defaultDestination == null) {
                defaultDestination = destination;
            }
        }
        // resets the combo box when set correctly.
        graphComboBox.setItems(destinations);
        graphComboBox.setOnAction((final ActionEvent t) -> {
            importController.setDestination(graphComboBox.getSelectionModel().getSelectedItem());
        });
        // Select null triggers the combobox to update to the correct value for
        // some unknown reason. Removal will mean that the combobox will
        // not keep it's state when a graph event occurs
        // ClearSelection() did not work to fix this.
        graphComboBox.getSelectionModel().select(null);
        importController.setDestination(previousDestinationObject != null ? previousDestinationObject : defaultDestination);
    }
}
