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
package au.gov.asd.tac.constellation.layers.utilities;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.pluginframework.PluginException;
import au.gov.asd.tac.constellation.pluginframework.PluginInteraction;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameters;
import au.gov.asd.tac.constellation.pluginframework.templates.SimpleEditPlugin;

/**
 *
 * @author aldebaran30701
 */
public final class UpdateElementBitmaskPlugin extends SimpleEditPlugin {

    public UpdateElementBitmaskPlugin() {
    }

    @Override
    public void edit(final GraphWriteMethods graph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        int graphBitMask = VisualConcept.GraphAttribute.SELECTEDFILTERMASK.get(graph);

        // VERTEX
        int vxSelectedAttr = VisualConcept.VertexAttribute.SELECTED.get(graph);
        int vxBitmaskAttributeId = VisualConcept.VertexAttribute.FILTERMASK.get(graph);
        
        if (vxSelectedAttr != Graph.NOT_FOUND) {
            final int vxCount = graph.getVertexCount();
            for (int position = 0; position < vxCount; position++) {
                final int vxId = graph.getVertex(position);
                
                // if selected, set a new bitmask
                if(graph.getBooleanValue(vxSelectedAttr, vxId)){
                    
                    graph.setIntValue(vxBitmaskAttributeId, vxId, graphBitMask == 1 ? 0b1 : 0b0101);
                    int bitMask = graph.getIntValue(vxBitmaskAttributeId, vxId);
                    // display prompt and ask for input?
                    System.err.println("setting for vertex : " + Integer.toBinaryString(bitMask));
                }
            }
        }
        
        // TRANSACTIONS
        int txSelected = VisualConcept.TransactionAttribute.SELECTED.get(graph);
        int txBitmaskAttributeId = VisualConcept.TransactionAttribute.FILTERMASK.get(graph);
        
        if (txSelected != Graph.NOT_FOUND) {
            final int txCount = graph.getTransactionCount();
            for (int position = 0; position < txCount; position++) {
                final int txId = graph.getTransaction(position);

                // if selected, set a new bitmask
                if(graph.getBooleanValue(txSelected, txId)){
                    
                    graph.setIntValue(txBitmaskAttributeId, txId, graphBitMask == 1 ? 0b1 : 0b0101);
                    int bitMask = graph.getIntValue(txBitmaskAttributeId, txId);
                    System.err.println("setting for transaction: " + Integer.toBinaryString(bitMask));
                    // display prompt and ask for input
                }
            }
        }
        
        //int bitmaskAttributeId = VisualConcept.GraphAttribute.SELECTEDFILTERMASK.ensure(graph);
        //graph.setIntValue(bitmaskAttributeId, 0, bitmask);
    }

    @Override
    protected boolean isSignificant() {
        return true;
    }

    @Override
    public String getName() {
        return "Layers View: Update Graph Bitmask";
    }
}
