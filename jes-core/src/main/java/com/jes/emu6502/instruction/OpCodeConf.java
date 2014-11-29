package com.jes.emu6502.instruction;

import com.jes.emu6502.addressing.AddressingMode;

/**
 * Created by Piotr Kulma on 29.11.14.
 */
public class OpCodeConf {
    private String opCode;
    private Mnemonic mnemonic;
    private AddressingMode addressingMode;
    private int bytes;
    private String cycles;

    public OpCodeConf(String op, Mnemonic m, AddressingMode a, int b, String c) {
        this.opCode = op;
        this.mnemonic = m;
        this.addressingMode = a;
        this.bytes = b;
        this.cycles = c;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(Mnemonic mnemonic) {
        this.mnemonic = mnemonic;
    }

    public AddressingMode getAddressingMode() {
        return addressingMode;
    }

    public void setAddressingMode(AddressingMode addressingMode) {
        this.addressingMode = addressingMode;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getCycles() {
        return cycles;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }
}
