package com.jes.nes;

import com.jes.emu6502.Emulator6502;
import com.jes.nesfile.NESFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 2014-11-20.
 */
public class Nes {
    public static Logger LOG = LogManager.getLogger(Nes.class);

    public static final int PPU_MEMORY_SIZE = 0x10000;

    public static final int PPU_PATTERN_TABLES_BEGIN_ADDR = 0x0000;
    public static final int PPU_PATTERN_TABLES_END_ADDR = 0x2000;

    public static final int CPU_ROM_UPPER_BANK_START_ADDR = 0xC000;
    public static final int CPU_ROM_UPPER_BANK_END_ADDR = 0x10000;

    public static final int CPU_ROM_LOWER_BANK_START_ADDR = 0x8000;
    public static final int CPU_ROM_LOWER_BANK_END_ADDR = 0xC000;

    private byte[] ppuMemoryMap;

    private Emulator6502 cpu;

    public Nes(NESFile nesFile) {
        initialize();

        loadData(nesFile);
    }

    public void runNesEmulation() {
        cpu.runEmulation();
    }

    public byte[] getPpuMemoryMap() {
        return ppuMemoryMap;
    }

    //TODO do usunięcia, bo to wyrywanie flaków
    public Emulator6502 getCpu() {
        return cpu;
    }

    private void initialize() {
        ppuMemoryMap = new byte[PPU_MEMORY_SIZE];
        cpu = new Emulator6502();
    }

    private void loadData(NESFile nesFile) {
        int romBanks = nesFile.getHeader().getRomBanks();
        int ramBanks = nesFile.getHeader().getRamBanks();

        byte[][] romBank = nesFile.getRomBankData();
        byte[][] vRomBank = nesFile.getvRomBankData();

        copyROMIntoCPUMemory(romBank, romBanks);

        copyVROMIntoCPUMemory(vRomBank, ramBanks);
    }

    private void copyVROMIntoCPUMemory(byte[][] romBank, int romBanks) {
        int index = 0;

        for(int i=PPU_PATTERN_TABLES_BEGIN_ADDR; i<PPU_PATTERN_TABLES_END_ADDR; i++) {
            ppuMemoryMap[i] = romBank[0][index++];
        }
        LOG.info(MessageFormat.format("Loaded Pattern Tables Bank ({0}-{1})",
                PPU_PATTERN_TABLES_BEGIN_ADDR,
                PPU_PATTERN_TABLES_END_ADDR));
    }

    private void copyROMIntoCPUMemory(byte[][] romBank, int romBanks) {
        if(romBanks == 1) {
            /**
             * When only one ROM bank is present load it twice into both
             * Upper and Lower PRG_ROM CPU memory
             */
            int index = 0;
            for(int i=CPU_ROM_LOWER_BANK_START_ADDR; i<CPU_ROM_LOWER_BANK_END_ADDR; i++) {
                cpu.setMemoryCell(i, romBank[0][index++]);
            }
            LOG.info(MessageFormat.format("Loaded lower PRG-ROM Bank ({0}-{1})",
                    CPU_ROM_LOWER_BANK_START_ADDR,
                    CPU_ROM_LOWER_BANK_END_ADDR));

            index = 0;
            for(int i=CPU_ROM_UPPER_BANK_START_ADDR; i<CPU_ROM_UPPER_BANK_END_ADDR; i++) {
                cpu.setMemoryCell(i, romBank[0][index++]);
            }
            LOG.info(MessageFormat.format("Loaded upper PRG-ROM Bank ({0}-{1})",
                    CPU_ROM_UPPER_BANK_START_ADDR,
                    CPU_ROM_UPPER_BANK_END_ADDR));
        } else {
            int index = 0;
            for(int i=CPU_ROM_LOWER_BANK_START_ADDR; i<CPU_ROM_LOWER_BANK_END_ADDR; i++) {
                cpu.setMemoryCell(i, romBank[0][index++]);
            }
            LOG.info(MessageFormat.format("Loaded lower PRG-ROM Bank ({0}-{1})",
                    CPU_ROM_LOWER_BANK_START_ADDR,
                    CPU_ROM_LOWER_BANK_END_ADDR));

            index = 0;
            for(int i=CPU_ROM_UPPER_BANK_START_ADDR; i<CPU_ROM_UPPER_BANK_END_ADDR; i++) {
                cpu.setMemoryCell(i, romBank[1][index++]);
            }
            LOG.info(MessageFormat.format("Loaded upper PRG-ROM Bank ({0}-{1})",
                    CPU_ROM_UPPER_BANK_START_ADDR,
                    CPU_ROM_UPPER_BANK_END_ADDR));
        }
    }
}
