package com.jes.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 05.12.14.
 */
public class EmulationUtils {
    public static Logger LOG = LogManager.getLogger(EmulationUtils.class);

    public static int getRealAddress(int address, int realMemorySize, int mirrorBeginAddress) {
        int realAddress = address;
        while(realAddress >= mirrorBeginAddress) {
            realAddress -= realMemorySize;
        }

        LOG.info(MessageFormat.format("Mirrored memory access {0} -> {1}", address, realAddress));
        return address;
    }
}
