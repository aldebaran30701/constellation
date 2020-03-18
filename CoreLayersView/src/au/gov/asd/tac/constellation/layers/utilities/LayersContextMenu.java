/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.gov.asd.tac.constellation.layers.utilities;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.graph.manager.GraphManager;
import au.gov.asd.tac.constellation.graph.visual.contextmenu.ContextMenuProvider;
import au.gov.asd.tac.constellation.pluginframework.PluginExecution;
import au.gov.asd.tac.constellation.visual.graphics3d.Vector3f;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import org.openide.util.lookup.ServiceProvider;

/**
 * Add, delete selections to layers.
 * 
 * @author sol695510
 */

@ServiceProvider(service = ContextMenuProvider.class, position = 1100)
public class LayersContextMenu implements ContextMenuProvider {

    private static final String LAYERS_MENU = "Layers";
    private static final String ADD_REMOVE_LAYER = "Add/Remove Selection from Layer";
    
    @Override
    public List<String> getMenuPath(GraphElementType elementType) {
        return Arrays.asList(LAYERS_MENU);
    }

    @Override
    public List<String> getItems(GraphReadMethods graph, GraphElementType elementType, int elementId) {
        if (elementType == GraphElementType.VERTEX || elementType == GraphElementType.TRANSACTION) {
            return Arrays.asList(ADD_REMOVE_LAYER);
        } else {
            return null;
        }
    }

    @Override
    public void selectItem(String item, Graph graph, GraphElementType elementType, int elementId, Vector3f unprojected) {
        switch (item) {
            case ADD_REMOVE_LAYER:
                // add
                PluginExecution.withPlugin(new UpdateElementBitmaskPlugin()).executeLater(GraphManager.getDefault().getActiveGraph());
                JOptionPane.showMessageDialog(null, "Adding/Removing from Layers...");                
                break;
            default:
                break;
        }
    }
}