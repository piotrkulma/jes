package com.jes.com.jes.nesfile;

/**
 * Created by Piotr Kulma on 2014-11-16.
 *
 * Based on http://fms.komkon.org/EMUL8/NES.html
 */
public class NESFileHeader {
    /**
     * Byte 0-3 - String "NES^Z" used to recognize .NES files
     */
    private String fileSignature;

    /**
     * Byte 4 - Number of 16kB ROM banks.
     */
    private int romBanks;

    /**
     * Byte 5 - Number of 8kB VROM banks.
     */
    private int vromBanks;

    /**
     * Byte 6 -
     * bit 0     1 for vertical mirroring, 0 for horizontal mirroring.
     * bit 1     1 for battery-backed RAM at $6000-$7FFF.
     * bit 2     1 for a 512-byte trainer at $7000-$71FF.
     * bit 3     1 for a four-screen VRAM layout.
     * bit 4-7   Four lower bits of ROM Mapper Type.
     */
    private int[] confByte6;

    /**
     * Byte 7 -
     * bit 0     1 for VS-System cartridges.
     * bit 1-3   Reserved, must be zeroes!
     * bit 4-7   Four higher bits of ROM Mapper Type.
     */
    private int[] confByte7;

    /**
     * Byte 8 - Number of 8kB RAM banks. For compatibility with the
     * previous versions of the .NES format, assume 1x8kB RAM page
     * when this byte is zero.
     */
    private int ramBanks;

    /**
     * Byte 9 -
     * bit 0     1 for PAL cartridges, otherwise assume NTSC.
     * bit 1-7   Reserved, must be zeroes!
     */
    private int[] confByte9;

    /**
     * Bytes 10-15 - Reserved, must be zeroes!
     */
    private byte[] reserved;

    public NESFileHeader() {
    }

    public String getFileSignature() {
        return fileSignature;
    }

    public void setFileSignature(String fileSignature) {
        this.fileSignature = fileSignature;
    }

    public int getRomBanks() {
        return romBanks;
    }

    public void setRomBanks(int romBanks) {
        this.romBanks = romBanks;
    }

    public int getVromBanks() {
        return vromBanks;
    }

    public void setVromBanks(int vromBanks) {
        this.vromBanks = vromBanks;
    }

    public int[] getConfByte6() {
        return confByte6;
    }

    public void setConfByte6(int[] confByte6) {
        this.confByte6 = confByte6;
    }

    public int[] getConfByte7() {
        return confByte7;
    }

    public void setConfByte7(int[] confByte7) {
        this.confByte7 = confByte7;
    }

    public int getRamBanks() {
        return ramBanks;
    }

    public void setRamBanks(int ramBanks) {
        this.ramBanks = ramBanks;
    }

    public int[] getConfByte9() {
        return confByte9;
    }

    public void setConfByte9(int[] confByte9) {
        this.confByte9 = confByte9;
    }

    public byte[] getReserved() {
        return reserved;
    }

    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }
}
