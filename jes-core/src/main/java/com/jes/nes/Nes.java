package com.jes.nes;

import com.jes.emu2C02.Emulator2C02;
import com.jes.emu6502.Emulator2A03;
import com.jes.nesfile.NESFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 2014-11-20.
 */
public class Nes {
    public static Logger LOG = LogManager.getLogger(Nes.class);

    public static final int PPU_PATTERN_TABLES_BEGIN_ADDR   = 0x0000;
    public static final int PPU_PATTERN_TABLES_END_ADDR     = 0x2000;

    public static final int CPU_ROM_UPPER_BANK_START_ADDR   = 0xC000;
    public static final int CPU_ROM_UPPER_BANK_END_ADDR     = 0x10000;

    public static final int CPU_ROM_LOWER_BANK_START_ADDR   = 0x8000;
    public static final int CPU_ROM_LOWER_BANK_END_ADDR     = 0xC000;

    private MemoryAccessObserver memory;
    private Emulator2A03 cpu;
    private Emulator2C02 ppu;

    public Nes(NESFile nesFile) throws Exception {
        initialize();

        loadData(nesFile);
    }

    public void runNesEmulation() throws Exception {
        cpu.runEmulation();
    }

    private void initialize() throws Exception {
        memory = new MemoryAccessObserver();

        cpu = new Emulator2A03(memory);
        ppu = new Emulator2C02(memory);
    }

    public Emulator2A03 getCpu() {
        return cpu;
    }

    public Emulator2C02 getPpu() {
        return ppu;
    }

    private void loadData(NESFile nesFile) {
        int romBanks = nesFile.getHeader().getRomBanks();
        //int ramBanks = nesFile.getHeader().getRamBanks();

        byte[][] romBank = nesFile.getRomBankData();
        byte[][] vRomBank = nesFile.getvRomBankData();

        copyROMIntoCPUMemory(romBank, romBanks);

        copyROMIntoPPUMemory(vRomBank);
    }

    private void copyROMIntoPPUMemory(byte[][] romBank) {
        int index = 0;

        for(int i=PPU_PATTERN_TABLES_BEGIN_ADDR; i<PPU_PATTERN_TABLES_END_ADDR; i++) {
            ppu.setMemoryCell(i, romBank[0][index++]);
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
