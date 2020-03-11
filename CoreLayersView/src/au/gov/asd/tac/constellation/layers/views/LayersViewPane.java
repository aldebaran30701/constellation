/*
 * Copyright 2010-2019 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.layers.views;

import au.gov.asd.tac.constellation.layers.LayersViewController;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


/**
 *
 * TODO: Note the code initially in this file is for proof of concept and trial only. 
 * Full scale implementation will require refactoring and neatening of UI elements.
 * 
 * @author aldebaran30701
 */
public class LayersViewPane extends GridPane {
    private TextArea queryInput = new TextArea();
    public TextArea viewLayersInput = new TextArea();
    private SplitPane layersSplitPane = new SplitPane();
    private Button executeButton = new Button("Execute");
    private HBox layerExecution = new HBox();
    
    public LayersViewPane(final LayersViewController controller) {
        


        layerExecution.setAlignment(Pos.CENTER_RIGHT);
        layerExecution.setSpacing(15);
        GridPane.setHalignment(layerExecution, HPos.RIGHT);
        add(layerExecution, 0, 1);
        
        // event handler for execute click
        executeButton.setOnAction((ActionEvent event) -> {
            controller.execute();
        });
        layerExecution.getChildren().add(executeButton);
        
        // adding textAreas to the splitpane
        layersSplitPane.getItems().addAll(queryInput, viewLayersInput);
        
        // code found in attribute calculator pane - unsure of exact need
        GridPane.setHalignment(layersSplitPane, HPos.LEFT);
        GridPane.setValignment(layersSplitPane, VPos.TOP);
        add(layersSplitPane, 0, 0);
    }
}
