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
package au.gov.asd.tac.constellation.visual.opengl.utilities.glyphs;

import au.gov.asd.tac.constellation.visual.opengl.utilities.glyphs.FontInfo.ParsedFontInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * An application for viewing fonts rendered as glyphs in OpenGL.
 *
 * @author algol
 */
public class GlyphsFrame extends JFrame {

    private final GlyphManagerBI glyphManager;
    private final JFrame imageFrame;
    private final Random random = new Random();

    // For copying to the clipboard.
    //
    private String copyText;

    public GlyphsFrame(final FontInfo[] fontsInfo, final String[] text) {

        initComponents();

        // Create a frame to display the highest page of the texture buffer in.
        // Make our texture size smaller so we can look at it sensibly.
        //
        final int textureBufferSize = GlyphManagerBI.DEFAULT_TEXTURE_BUFFER_SIZE / 4;
        imageFrame = new JFrame("Texture buffer (highest page)");
        imageFrame.getContentPane().setBackground(Color.DARK_GRAY);
        imageFrame.getContentPane().setPreferredSize(new Dimension(textureBufferSize, textureBufferSize));
        imageFrame.getContentPane().setLayout(new BorderLayout());
//        imageFrame.getContentPane().setBackground(Color.BLACK);
//        imageFrame.setPreferredSize(new Dimension(TEXTURE_BUFFER_SIZE*2, TEXTURE_BUFFER_SIZE*2));
        imageFrame.getContentPane().add(new JLabel(), BorderLayout.CENTER);
//        imageFrame.getContentPane().getComponent(0).setPreferredSize(new Dimension(TEXTURE_BUFFER_SIZE, TEXTURE_BUFFER_SIZE));
        imageFrame.pack();
        imageFrame.setVisible(true);
        imageFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        textLines.setModel(new DefaultComboBoxModel<>(text));
        glyphManager = new GlyphManagerBI(fontsInfo, textureBufferSize, BufferedImage.TYPE_INT_ARGB);

        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String[] availablefonts = Arrays.stream(ge.getAvailableFontFamilyNames(Locale.US))
                .filter(f -> !f.startsWith(Font.DIALOG))
                .sorted()
                .toArray(String[]::new);
        fontNameSp.setModel(new DefaultComboBoxModel<>(availablefonts));
        if (fontsInfo.length > 0) {
            fontNameSp.getModel().setSelectedItem(fontsInfo[0].fontName);
            cbBold.getModel().setSelected(fontsInfo[0].fontStyle == Font.BOLD);
        }

        fontSizeSp.setValue(GlyphManagerBI.DEFAULT_FONT_SIZE);
        cbActionPerformed();

        final BufferedImage img = glyphManager.getImage();
        imageLabel.setIcon(new ImageIcon(glyphManager.getImage()));
        glyphPanel.setPreferredSize(new Dimension(img.getWidth() + 1, img.getHeight() + 1));

        final String line = getLine();
        glyphManager.renderTextAsLigatures(line, null);

        showTextureBuffer();
    }

    private void showTextureBuffer() {
        final JLabel label = (JLabel) imageFrame.getContentPane().getComponent(0);
        label.setIcon(new ImageIcon(glyphManager.getTextureBuffer()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textLines = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbRuns = new javax.swing.JCheckBox();
        cbIGlyphs = new javax.swing.JCheckBox();
        cbCGlyphs = new javax.swing.JCheckBox();
        fontSizeSp = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        fontNameSp = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbBold = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        glyphPanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        textField = new javax.swing.JTextField();
        addTextButton = new javax.swing.JButton();
        cbZalgo = new javax.swing.JCheckBox();
        copyTextButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Glyph rendering example");
        setLocationByPlatform(true);
        setPreferredSize(new java.awt.Dimension(1200, 400));

        textLines.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        textLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textLinesActionPerformed(evt);
            }
        });

        jLabel1.setText("Glyph Outlines:");

        jLabel2.setText("Text:");

        cbRuns.setText("Font runs");
        cbRuns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRunsActionPerformed(evt);
            }
        });

        cbIGlyphs.setText("Individual glyphs");
        cbIGlyphs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbIGlyphsActionPerformed(evt);
            }
        });

        cbCGlyphs.setSelected(true);
        cbCGlyphs.setText("Combined glyphs");
        cbCGlyphs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCGlyphsActionPerformed(evt);
            }
        });

        fontSizeSp.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        fontSizeSp.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fontSizeSpStateChanged(evt);
            }
        });

        jLabel3.setText("Size:");

        fontNameSp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fontNameSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fontNameSpActionPerformed(evt);
            }
        });

        jLabel4.setText("First font:");

        cbBold.setText("Bold");
        cbBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBoldActionPerformed(evt);
            }
        });

        glyphPanel.setBackground(new java.awt.Color(0, 0, 0));
        glyphPanel.setPreferredSize(new java.awt.Dimension(1500, 220));
        glyphPanel.setLayout(new java.awt.BorderLayout());
        glyphPanel.add(imageLabel, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(glyphPanel);

        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldActionPerformed(evt);
            }
        });

        addTextButton.setText("Add text");
        addTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTextButtonActionPerformed(evt);
            }
        });

        cbZalgo.setText("Zalgo");

        copyTextButton.setText("Copy text");
        copyTextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyTextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbRuns)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbIGlyphs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbCGlyphs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(copyTextButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fontNameSp, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbBold)
                        .addGap(62, 62, 62)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fontSizeSp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbZalgo))
                    .addComponent(textLines, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTextButton)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textLines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTextButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fontSizeSp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(fontNameSp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbBold)
                    .addComponent(cbZalgo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbRuns)
                    .addComponent(cbIGlyphs)
                    .addComponent(cbCGlyphs)
                    .addComponent(copyTextButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbRunsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRunsActionPerformed
        cbActionPerformed();
    }//GEN-LAST:event_cbRunsActionPerformed

    private void textLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textLinesActionPerformed
        final String line = getLine();
        glyphManager.renderTextAsLigatures(line, null);
        repaint();
        showTextureBuffer();
    }//GEN-LAST:event_textLinesActionPerformed

    private void cbIGlyphsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbIGlyphsActionPerformed
        cbActionPerformed();
    }//GEN-LAST:event_cbIGlyphsActionPerformed

    private void cbCGlyphsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCGlyphsActionPerformed
        cbActionPerformed();
    }//GEN-LAST:event_cbCGlyphsActionPerformed

    private void fontSizeSpStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fontSizeSpStateChanged
        fontActionPerformed();
    }//GEN-LAST:event_fontSizeSpStateChanged

    private void fontNameSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fontNameSpActionPerformed
        fontActionPerformed();
    }//GEN-LAST:event_fontNameSpActionPerformed

    private void cbBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBoldActionPerformed
        fontActionPerformed();
    }//GEN-LAST:event_cbBoldActionPerformed

    private void addTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTextButtonActionPerformed
        textActionPerformed();
    }//GEN-LAST:event_addTextButtonActionPerformed

    private void textFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldActionPerformed
        textActionPerformed();
    }//GEN-LAST:event_textFieldActionPerformed

    private void copyTextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyTextButtonActionPerformed
        final StringSelection stringSelection = new StringSelection(copyText);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_copyTextButtonActionPerformed

    @SuppressWarnings("unchecked")
    private void textActionPerformed() {
        final String text = textField.getText().trim();
        final DefaultComboBoxModel<String> model = (DefaultComboBoxModel) textLines.getModel();
        model.addElement(text);
        model.setSelectedItem(text);
        repaint();
    }

    private void cbActionPerformed() {
        final boolean drawRuns = cbRuns.isSelected();
        final boolean drawIndividual = cbIGlyphs.isSelected();
        final boolean drawCombined = cbCGlyphs.isSelected();
        glyphManager.setBoundaries(drawRuns, drawIndividual, drawCombined);
        final String line = getLine();
        glyphManager.renderTextAsLigatures(line, null);
        repaint();
    }

    private void fontActionPerformed() {
        final FontInfo[] fontsInfo = glyphManager.getFonts();
        final String fontName = (String) fontNameSp.getSelectedItem();
        final int fontStyle = cbBold.isSelected() ? Font.BOLD : Font.PLAIN;
        final int fontSize = ((SpinnerNumberModel) fontSizeSp.getModel()).getNumber().intValue();
        final FontInfo fi = fontsInfo[0];
        fontsInfo[0] = new FontInfo(fontName, fontStyle, fontSize, fi.mustHave, fi.mustNotHave);

        glyphManager.setFonts(fontsInfo);
        glyphManager.createBackgroundGlyph(0.5f);

        showTextureBuffer();
        final String line = getLine();
        glyphManager.renderTextAsLigatures(line, null);

        repaint();
    }

    private String getLine() {
        String line = (String) textLines.getModel().getSelectedItem();
        final boolean isZalgo = cbZalgo.isSelected();
        if (isZalgo) {
            final List<Integer> codepoints = new ArrayList<>();
            final int length = line.length();
            for (int offset = 0; offset < length;) {
                final int codepoint = line.codePointAt(offset);
                final int cc = Character.charCount(codepoint);

                codepoints.add(codepoint);
                random.ints(random.nextInt(6), 0, 7 * 16).forEach(r -> codepoints.add(0x300 + r));

                offset += cc;
            }
            final int[] cpi = codepoints.stream().mapToInt(i -> i).toArray();
            line = new String(cpi, 0, cpi.length);
        }

        copyText = line;

        return line;
    }

    private static String[] loadText(final String fnam, final boolean raw) throws IOException {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(GlyphsFrame.class.getResourceAsStream(fnam), StandardCharsets.UTF_8))) {
            final List<String> ls = in.lines().filter(line -> raw || (line.length() > 0 && !line.startsWith("#"))).collect(Collectors.toList());
            final String[] text = ls.toArray(new String[ls.size()]);
            return text;
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        final String[] fontNames = loadText("fonts.txt", true);
        final String[] text = loadText("text.txt", false);

        final ParsedFontInfo pfi = FontInfo.parseFontInfo(fontNames, GlyphManagerBI.DEFAULT_FONT_SIZE);

        if (!pfi.messages.isEmpty()) {
            System.out.printf("ParsedFontInfo message: %s\n", pfi.getMessages());
        }

        try {
            /* Set the system look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //</editor-fold>
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GlyphsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        EventQueue.invokeLater(() -> {
            new GlyphsFrame(pfi.fontsInfo, text).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTextButton;
    private javax.swing.JCheckBox cbBold;
    private javax.swing.JCheckBox cbCGlyphs;
    private javax.swing.JCheckBox cbIGlyphs;
    private javax.swing.JCheckBox cbRuns;
    private javax.swing.JCheckBox cbZalgo;
    private javax.swing.JButton copyTextButton;
    private javax.swing.JComboBox<String> fontNameSp;
    private javax.swing.JSpinner fontSizeSp;
    private javax.swing.JPanel glyphPanel;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField textField;
    private javax.swing.JComboBox<String> textLines;
    // End of variables declaration//GEN-END:variables
}
