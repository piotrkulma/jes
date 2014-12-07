package com.jes.nes;

import com.jes.emu2C02.Emulator2C02;
import com.jes.emu6502.Emulator2A03;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Piotr Kulma on 05.12.14.
 */
public class MemoryAccessObserver {
    public static Logger LOG = LogManager.getLogger(MemoryAccessObserver.class);

    public static final int PPU_CONTROL_REGISTER_1_ADDRESS      = 0x2000;
    public static final int PPU_CONTROL_REGISTER_2_ADDRESS      = 0x2001;
    public static final int PPU_STATUS_REGISTER_ADDRESS         = 0x2002;
    public static final int SPR_RAM_ADDRESS_REGISTER_ADDRESS    = 0x2003;
    public static final int SPR_RAM_IO_REGISTER_ADDRESS         = 0x2004;
    public static final int VRAM_ADDRESS_REGISTER_1_ADDRESS     = 0x2005;
    public static final int VRAM_ADDRESS_REGISTER_2_ADDRESS     = 0x2006;
    public static final int VRAM_IO_REGISTER_ADDRESS            = 0x2007;
    public static final int SPRITE_DMA_REGISTER_ADDRESS         = 0x4014;

    private Emulator2A03 cpu;
    private Emulator2C02 ppu;

    public MemoryAccessObserver() {
        cpu = null;
        ppu = null;
    }

    public void setPpu(Emulator2C02 ppu) {
        this.ppu = ppu;
    }

    public void setCpu(Emulator2A03 cpu) {
        this.cpu = cpu;
    }

    public void notifyToCPUMemory(int address, byte value) {
        if (isIORegister(address)) {
            cpu.modifyMemoryWithoutObserver(address, value);
            LOG.info("PPU -> CPU IO Register WRITE");
        }

        if(isDMATransfer(address)) {
            LOG.info("PPU -> CPU DMA TRANSFER");
        }

    }

    public void notifyToPPUMemory(int address, byte value) {
        if (isIORegister(address)) {
            ppu.modifyMemoryWithoutObserver(address, value);
            LOG.info("CPU -> PPU IO Register WRITE");
        }

        if(isDMATransfer(address)) {
            LOG.info("CPU -> PPU DMA TRANSFER");
        }
    }

    private boolean isIORegister(int address) {
        if(address >= PPU_CONTROL_REGISTER_1_ADDRESS && address <= VRAM_IO_REGISTER_ADDRESS) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDMATransfer(int address) {
        if(address == SPRITE_DMA_REGISTER_ADDRESS) {
            return true;
        } else {
            return false;
        }
    }
}
