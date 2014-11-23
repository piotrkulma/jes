package com.jes.emu6502.instruction;

import com.jes.emu6502.addressing.AddressingMode;

/**
 * Created by Piotr Kulma on 2014-11-22.
 */
public class Instruction {
    private Mnemonic mnemonic;
    private AddressingMode addressingMode;
    private byte[] parameters;

    public Instruction(Mnemonic mnemonic, AddressingMode add, int paramNo) {
        this.mnemonic = mnemonic;
        this.parameters = new byte[paramNo];
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

    public byte[] getParameters() {
        return parameters;
    }

    public void setParameter(int index, byte value) {
        this.parameters[index] = value;
    }
}
