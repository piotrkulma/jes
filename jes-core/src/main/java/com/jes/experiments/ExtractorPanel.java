package com.jes.experiments;

import com.jes.nesfile.NESFile;
import com.jes.utils.NesFileBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-17.
 */
public class ExtractorPanel extends JPanel {
    public static final int GRAPHIC_SCALE = 4;

    private static Logger LOG = LogManager.getLogger(ExtractorPanel.LOG);
    private NESFile header;
    private VROMExtractor extractor;

    public ExtractorPanel() {
        header = NesFileBuilder.buildNESFile("c:/smb.nes");
        extractor = new VROMExtractor();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawNesBitmaps(g2d);
    }

    private void drawNesBitmaps(Graphics2D g2d) {
        boolean left = true;
        byte[][] vrom = header.getvRomBankData();

        extractor.setBank(vrom);
        extractor.setBankNumber(0);

        List<Tile> tileList = extractor.extract();

        draw(g2d, tileList);
    }

    private void draw(Graphics2D g2d, List<Tile> tileList) {
        int counter = 0;
        for(int i = 0;  i < 8 * 2; i++) {
            counter ++;
            Tile tile = tileList.get(i);

            for (int j = 0; j < 8; j++) {
                Integer[] data = tile.getTile().get(j);

                for(int k = 0; k < 8; k++) {
                    switch (data[k]) {
                        case 0:g2d.setColor(Color.black);break;
                        case 1:g2d.setColor(Color.red);break;
                        case 2:g2d.setColor(Color.green);break;
                        case 3:g2d.setColor(Color.blue);break;
                    }

                    g2d.fillRect((k * GRAPHIC_SCALE), (((counter * 8) + j) * GRAPHIC_SCALE), GRAPHIC_SCALE, GRAPHIC_SCALE);
                }
            }
        }
    }
}