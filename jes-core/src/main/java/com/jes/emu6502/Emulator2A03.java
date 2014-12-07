package com.jes.emu6502;

import com.jes.nes.MemoryAccessObserver;
import com.jes.utils.EmulationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Nes version of the 6502 CPU
 *
 * Created by Piotr Kulma on 30.11.14.
 */

public class Emulator2A03 extends Emulator6502 {
    public static Logger LOG = LogManager.getLogger(Emulator2A03.class);

    private MemoryAccessObserver memoryObserver;

    public Emulator2A03(MemoryAccessObserver memoryAccessObserver) throws Exception {
        this.memoryObserver = memoryAccessObserver;
        this.memoryObserver.setCpu(this);
    }


    @Override
    public void setMemoryCell(int address, byte value) {
        address = getAddress(address);

        memoryObserver.notifyToPPUMemory(address, value);
        super.setMemoryCell(address, value);
    }

    @Override
    public byte getMemoryCell(int address) {
        address = getAddress(address);

        return super.getMemoryCell(address);
    }

    public void modifyMemoryWithoutObserver(int address, byte value) {
        address = getAddress(address);
        super.setMemoryCell(address, value);
    }

    private int getAddress(int address) {
        if(address >= 2048 && address <= 8191) {
            address = EmulationUtils.getRealAddress(address, 2048, 2048);
        } else if(address >= 8200 && address <= 16383) {
            address = EmulationUtils.getRealAddress(address, 8, 8200);
        }

        return address;
    }
}
