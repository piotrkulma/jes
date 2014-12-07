package com.jes.emu2C02;

import com.jes.nes.MemoryAccessObserver;
import com.jes.utils.EmulationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * NES PPU Enumator (Ricoh 2C02 Picture Processing Unit)
 *
 * Created by Piotr Kulma on 05.12.14.
 */

public class Emulator2C02 {
    public static Logger LOG = LogManager.getLogger(Emulator2C02.class);

    public static final int SPRITE_DMA_REGISTER_ADDRESS         = 0x4014;


    public static final int MEMORY_SIZE                         = 0x10000;
    public static final int SPRITE_RAM_SIZE                     = 256;

    private byte[] memoryMap;
    private byte[] spriteRam;

    private MemoryAccessObserver memoryObserver;

    public Emulator2C02(MemoryAccessObserver memoryAccessObserver) {
        initializeSpriteRam();
        initializeMemoryMap();

        this.memoryObserver = memoryAccessObserver;
        this.memoryObserver.setPpu(this);
    }

    public void setSpriteRamMemoryCell(int address, byte value) {
        spriteRam[address] = value;
    }

    public byte getSpriteRamMemoryCell(int address) {
        return spriteRam[address];
    }

    public void setMemoryCell(int address, byte value) {
        address = getAddress(address);

        memoryObserver.notifyToCPUMemory(address, value);
        memoryMap[address] = value;
    }

    public byte getMemoryCell(int address) {
        address = getAddress(address);

        return memoryMap[address];
    }

    public void modifyMemoryWithoutObserver(int address, byte value) {
        address = getAddress(address);
        memoryMap[address] = value;
    }

    private void initializeMemoryMap() {
        memoryMap = new byte[MEMORY_SIZE];

        for(int i=0; i<MEMORY_SIZE; i++) {
            memoryMap[i] = 0;
        }
    }

    private void initializeSpriteRam() {
        spriteRam = new byte[SPRITE_RAM_SIZE];

        for(int i=0; i<SPRITE_RAM_SIZE; i++) {
            spriteRam[i] = 0;
        }
    }

    private int getAddress(int address) {
        if(address >= 12288 && address <= 16127) {
            address = EmulationUtils.getRealAddress(address, 3840, 12288);
        } else if(address >= 16160 && address <= 16383) {
            address = EmulationUtils.getRealAddress(address, 32, 16160);
        } else if(address >= 16384 && address <= 65535) {
            address = EmulationUtils.getRealAddress(address, 16384, 16384);
        }

        return address;
    }
}
