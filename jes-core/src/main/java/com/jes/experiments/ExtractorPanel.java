package com.jes.experiments;

import com.jes.Configuration;
import com.jes.nes.Nes;
import com.jes.nesfile.NESFile;
import com.jes.utils.CommonUtils;
import com.jes.nesfile.NesFileBuilder;
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

    private static Logger LOG = LogManager.getLogger(ExtractorPanel.class);
    private NESFile nesFile;
    private Nes nes;
    private VROMExtractor extractor;

    public ExtractorPanel() {
        nesFile = NesFileBuilder.buildNESFile(Configuration.TEST_ROM_PATH);
        //nes = new Nes(nesFile);
        extractor = new VROMExtractor();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawNesBitmaps(g2d);
    }

    private void drawNesBitmaps(Graphics2D g2d) {
/*        byte[] vrom = CommonUtils.getPartialArray(nes.getPpuMemoryMap(),
                Nes.PPU_PATTERN_TABLES_BEGIN_ADDR,
                Nes.PPU_PATTERN_TABLES_END_ADDR);

        extractor.setBank(vrom);

        List<Tile> tileList = extractor.extract();

        draw(g2d, tileList);*/
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