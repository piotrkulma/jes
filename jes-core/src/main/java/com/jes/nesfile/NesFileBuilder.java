package com.jes.nesfile;

import com.jes.utils.BinaryMath;
import com.jes.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */
public class NesFileBuilder {
    private static Logger LOG = LogManager.getLogger(NesFileBuilder.class);

    public static NESFile buildNESFile(String path) {
        NESFile file = new NESFile();
        NESFileHeader header = new NESFileHeader();
        file.setHeader(header);

        LOG.info(MessageFormat.format("LOADING DATA FROM FILE ''{0}''", path));

        try {
            FileInputStream fis = new FileInputStream(new File(path));

            LOG.info("#### FETCHING FILE HEADER");
            fetchHeader(file, fis);

            LOG.info("#### FETCHING FILE DATA");
            fetchData(file, fis);

            fis.close();
        } catch (FileNotFoundException fnfe) {
            LOG.error("", fnfe);
        } catch (IOException e) {
            LOG.error("", e);
        }

        LOG.info("DATA FROM FILE LOADED");
        return file;
    }

    private static void fetchData(NESFile file, FileInputStream fis) throws IOException{
        if(file.isTrainer()) {
            LOG.info("TRAINER FOUND");
            LOG.info(MessageFormat.format("AVAILABLE DATA: {0}", fis.available()));

            byte[] trainer = new byte[NESFile.SIZE_TRAINER];
            fis.read(trainer, 0, NESFile.SIZE_TRAINER);

            file.setTrainer(trainer);
        }

        LOG.info(MessageFormat.format("AVAILABLE DATA: {0}", fis.available()));
        byte[][] romBank = new byte[file.getHeader().getRomBanks()][NESFile.SIZE_ROM_BANK];

        LOG.info("LOADING 16kB ROM BANKS");
        for( int i=0; i<file.getHeader().getRomBanks(); i++) {
            fis.read(romBank[i], 0, NESFile.SIZE_ROM_BANK);
        }

        file.setRomBankData(romBank);

        LOG.info(MessageFormat.format("AVAILABLE DATA: {0}", fis.available()));
        byte[][] vRomBank = new byte[file.getHeader().getVromBanks()][NESFile.SIZE_VROM_BANK];

        LOG.info("LOADING 8kB VROM BANKS");
        for( int i=0; i<file.getHeader().getVromBanks(); i++) {
            fis.read(vRomBank[i], 0, NESFile.SIZE_VROM_BANK);
        }

        file.setvRomBankData(vRomBank);

    }

    private static void fetchHeader(NESFile file, FileInputStream fis) throws IOException{
        byte[] signature = new byte[NESFile.SIZE_SIGNATURE];
        byte[] reserved = new byte[NESFile.SIZE_RESERVED_BYTE_10_TO_15];

        NESFileHeader header = file.getHeader();

        fis.read(signature, 0, NESFile.SIZE_SIGNATURE);
        header.setFileSignature(new String(signature));
        LOG.info("FILE SIGNATURE: " + header.getFileSignature());

        header.setRomBanks(fis.read());
        LOG.info("NUMBER 16kB ROM BANKS: : " + header.getRomBanks());

        header.setVromBanks(fis.read());
        LOG.info("NUMBER 8kB VROM BANKS: : " + header.getVromBanks());

        header.setConfByte6(BinaryMath.getBinaryArray(fis.read(), 8));
        LOG.info("CONF BYTE 6: " + CommonUtils.byteArrayToString(header.getConfByte6()));

        header.setConfByte7(BinaryMath.getBinaryArray(fis.read(), 8));
        LOG.info("CONF BYTE 7: " + CommonUtils.byteArrayToString(header.getConfByte7()));

        header.setRamBanks(fis.read());
        LOG.info("NUMBER OF 8kB RAM BANKS: " + header.getRamBanks());

        header.setConfByte9(BinaryMath.getBinaryArray(fis.read(), 8));
        LOG.info("CONF BYTE 9: " + CommonUtils.byteArrayToString(header.getConfByte9()));

        fis.read(reserved, 0, NESFile.SIZE_RESERVED_BYTE_10_TO_15);
        header.setReserved(reserved);
        LOG.info("RESERVED: " + CommonUtils.byteArrayToString(header.getReserved()));
    }
}
