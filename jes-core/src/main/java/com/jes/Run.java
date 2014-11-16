package com.jes;

import com.jes.com.jes.nesfile.NESFile;
import com.jes.utils.NesFileBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */
public class Run {
    public static final String TEST_ROM_PATH = "c:/smb.nes";
    private static Logger LOG = LogManager.getLogger(Run.LOG);

    public static void main(String... args) throws Exception {
        NESFile header = NesFileBuilder.buildNESFile(TEST_ROM_PATH);
    }
}
