package com.jes.emu6502;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * Nes version of the 6502 CPU
 *
 * Created by Piotr Kulma on 30.11.14.
 */
public class Emulator2A03 extends Emulator6502 {
    public static Logger LOG = LogManager.getLogger(Emulator2A03.class);

    public Emulator2A03() throws Exception{
    }

    @Override
    public void setMemoryCell(int address, byte value) {
        address = getAddress(address);

        super.setMemoryCell(address, value);
    }

    @Override
    public byte getMemoryCell(int address) {
        address = getAddress(address);

        return super.getMemoryCell(address);
    }

    private int getAddress(int address) {
        if(address >= 2048 && address <= 8191) {
            address = getRealAddress(address, 2048, 2048);
        } else if(address >= 8200 && address <= 16383) {
            address = getRealAddress(address, 8, 8200);
        }

        return address;
    }

    private int getRealAddress(int address, int realMemorySize, int mirrorBeginAddress) {
        int realAddress = address;
        while(realAddress >= mirrorBeginAddress) {
            realAddress -= realMemorySize;
        }

        LOG.info(MessageFormat.format("Mirrored memory access {0} -> {1}", address, realAddress));
        return address;
    }
}
