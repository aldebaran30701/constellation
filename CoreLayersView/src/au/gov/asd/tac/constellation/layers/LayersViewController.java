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
package au.gov.asd.tac.constellation.layers;

import au.gov.asd.tac.constellation.graph.StoreGraph;
import au.gov.asd.tac.constellation.layers.views.LayersViewPane;
import au.gov.asd.tac.constellation.layers.views.LayersViewTopComponent;

/**
 *
 * Controls interaction of UI to layers and filtering of nodes.
 * TODO: Currently has codeblocks from Attribute calculator and its execute functionality. 
 * 
 * @author aldebaran30701
 */
public class LayersViewController {
    //public static final List<String> ATTRIBUTE_TYPES = Arrays.asList(StringAttributeDescription.ATTRIBUTE_NAME, IntegerAttributeDescription.ATTRIBUTE_NAME, BooleanAttributeDescription.ATTRIBUTE_NAME, FloatAttributeDescription.ATTRIBUTE_NAME, ColorAttributeDescription.ATTRIBUTE_NAME, IconAttributeDescription.ATTRIBUTE_NAME, ZonedDateTimeAttributeDescription.ATTRIBUTE_NAME, TimeAttributeDescription.ATTRIBUTE_NAME, DateAttributeDescription.ATTRIBUTE_NAME, LocalDateTimeAttributeDescription.ATTRIBUTE_NAME, VertexTypeAttributeDescription.ATTRIBUTE_NAME, TransactionTypeAttributeDescription.ATTRIBUTE_NAME);

    private final LayersViewTopComponent parent;

    public LayersViewController(final LayersViewTopComponent parent) {
        this.parent = parent;
    }

    public void execute() {
        // used currently for selecting the layers to view and queries to run
        final LayersViewPane layersViewPane = parent.getContent();
        if (layersViewPane != null) {
            
            String[] layerEntries = layersViewPane.viewLayersInput.getText().split(",");
            
            int tempMask = 0;
            for(String layerNo : layerEntries) {
                if(Integer.parseInt(layerNo) > 0 && Integer.parseInt(layerNo) < Integer.MAX_VALUE){
                    // when parsed - maybe check is not necessary
                    System.err.println("parsed: " + Integer.parseInt(layerNo));
                    tempMask |= (1 << Integer.parseInt(layerNo));
                }
            }
            // set value crudely
            StoreGraph.currentVisibleMask = tempMask;
        }
        
        
        
        
        
        /**
        final AttributeCalculatorPane attributeCalculatorPane = parent.getContent();
        if (attributeCalculatorPane != null) {
            final String script = attributeCalculatorPane.getScript();
            if (!script.trim().isEmpty()) {
                final GraphElementType elementType = attributeCalculatorPane.getElementType();
                final String attribute = attributeCalculatorPane.getAttribute();
                final String attributeType = attributeCalculatorPane.getAttributeType();
                final boolean selectedOnly = attributeCalculatorPane.isSelectedOnly();
                final boolean completeWithSchema = attributeCalculatorPane.isCompleteWithSchema();

                final AttributeCalculatorPlugin plugin = new AttributeCalculatorPlugin(elementType, attribute, attributeType, "text/python", script, selectedOnly, completeWithSchema);
                PluginExecution.withPlugin(plugin).executeLater(parent.getCurrentGraph());
            } else {
                StatusDisplayer.getDefault().setStatusText("Not executing empty script.");
            }
        }
        * */
    }
}
