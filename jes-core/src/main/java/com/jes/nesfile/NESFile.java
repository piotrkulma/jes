package com.jes.nesfile;

import com.jes.nesfile.NESFileHeader;

/**
 * Created by Piotr Kulma on 2014-11-16.
 *
 * Based on http://fms.komkon.org/EMUL8/NES.html
 */
public class NESFile {
    public static final int CONF_BYTE_6_VERTICAL_MIRRORING              = 0;
    public static final int CONF_BYTE_6_BATTERY_BACKED                  = 1;
    public static final int CONF_BYTE_6_TRAINER                         = 2;
    public static final int CONF_BYTE_6_FOUR_SCREEN                     = 3;
    public static final int CONF_BYTE_6_LOWER_BITS_OF_ROM_MT_BEGIN      = 4;

    public static final int CONF_BYTE_7_VS_SYSTEM_CART                  = 0;
    public static final int CONF_BYTE_7_RESERVED_BEGIN                  = 1;
    public static final int CONF_BYTE_7_HIGHER_BITS_OF_ROM_MT_BEGIN     = 4;

    public static final int CONF_BYTE_9_PAL                             = 0;
    public static final int CONF_BYTE_9_RESERVED_BEGIN                  = 1;

    public static final int SIZE_RESERVED_BYTE_10_TO_15                 = 6;
    public static final int SIZE_SIGNATURE                              = 4;
    public static final int SIZE_ROM_BANK                               = 16 * 1024;
    public static final int SIZE_VROM_BANK                              = 8 * 1024;
    public static final int SIZE_TRAINER                                = 512;

    /**
     * Bytes 0-15
     */
    private NESFileHeader header;

    /**
     * Bytes 16-...
     * ROM banks, in ascending order. If a trainer is present, its
     * 512 bytes precede the ROM bank contents.
     * ...-EOF  VROM banks, in ascending order.
     */
    private byte[] trainer;

    private byte[][] romBankData;
    private byte[][] vRomBankData;

    public NESFile() {
    }

    public NESFile(NESFileHeader header) {
        this.header = header;
    }

    public NESFileHeader getHeader() {
        return header;
    }

    public void setHeader(NESFileHeader header) {
        this.header = header;
    }

    public static int getConfByte6VerticalMirroring() {
        return CONF_BYTE_6_VERTICAL_MIRRORING;
    }

    public byte[][] getvRomBankData() {
        return vRomBankData;
    }

    public void setvRomBankData(byte[][] vRomBankData) {
        this.vRomBankData = vRomBankData;
    }

    public byte[][] getRomBankData() {
        return romBankData;
    }

    public void setRomBankData(byte[][] romBankData) {
        this.romBankData = romBankData;
    }

    public byte[] getTrainer() {
        return trainer;
    }

    public void setTrainer(byte[] trainer) {
        this.trainer = trainer;
    }

    public boolean isTrainer() {
        return (getHeader().getConfByte6()[CONF_BYTE_6_TRAINER] == 1);
    }
}
