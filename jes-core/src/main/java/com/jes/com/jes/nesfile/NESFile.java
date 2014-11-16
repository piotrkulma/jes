package com.jes.com.jes.nesfile;

import com.jes.NESFileHeader;

/**
 * Created by Piotr Kulma on 2014-11-16.
 *
 * Based on http://fms.komkon.org/EMUL8/NES.html
 */
public class NESFile {
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
    private byte[] data;

    public NESFile() {
    }

    public NESFileHeader getHeader() {
        return header;
    }

    public void setHeader(NESFileHeader header) {
        this.header = header;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
