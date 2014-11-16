package com.jes.utils;

import com.jes.com.jes.nesfile.NESFile;
import com.jes.com.jes.nesfile.NESFileHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */
public class NesFileBuilder {
    private static Logger LOG = LogManager.getLogger(NesFileBuilder.LOG);

    public NesFileBuilder() {
    }

    public static NESFile loadNesFile(String path) {
        return null;
    }

    public static NESFileHeader loadNesFileHeader(String path) {
        NESFileHeader header = new NESFileHeader();
        LOG.info(MessageFormat.format("LOADING header from file ''{0}''", path));

        try {
            byte[] signature = new byte[4];
            FileInputStream fis = new FileInputStream(new File(path));

            fis.read(signature, 0, 4);
            header.setFileSignature(new String(signature));
            LOG.info("file signature: " + header.getFileSignature());

            header.setRomBanks(fis.read());
            LOG.info("number of 16kB ROM banks: : " + header.getRomBanks());

            header.setVromBanks(fis.read());
            LOG.info("number of 16kB VROM banks: : " + header.getVromBanks());

            int byte6 = fis.read();
            LOG.info("conf byte 6: " + byte6);

            int byte7 = fis.read();
            LOG.info("conf byte 7: " + byte7);

            header.setRamBanks(fis.read());
            LOG.info("number of 8kB RAM banks: " + header.getRamBanks());

            fis.close();
        } catch (FileNotFoundException fnfe) {
            LOG.error("", fnfe);
        } catch (IOException e) {
            LOG.error("", e);
        }
        return null;
    }
}
