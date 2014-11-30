package com.jes.emu6502.instruction;

import com.jes.emu6502.addressing.AddressingMode;
import com.jes.utils.BinaryMath;

/**
 * Created by Piotr Kulma on 2014-11-22.
 */
public class Instruction {
    private Mnemonic mnemonic;
    private AddressingMode addressingMode;
    private int parametersNumber;
    private byte[] parameters;

    public Instruction(OpCodeConf conf) {
        this.mnemonic = conf.getMnemonic();
        this.addressingMode = conf.getAddressingMode();
        this.parametersNumber = conf.getBytes() - 1;
        this.parameters = new byte[parametersNumber];
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

    public int getParametersNumber() {
        return parametersNumber;
    }

    public void setParametersNumber(int parametersNumber) {
        this.parametersNumber = parametersNumber;
    }

    @Override
    public String toString() {
        StringBuffer stringBuff = new StringBuffer("");

        stringBuff.append("Instruction [ ");
        stringBuff.append(" mnemonic: ");
        stringBuff.append(mnemonic.name());
        stringBuff.append(", addressingMode: ");
        stringBuff.append(addressingMode.name());
        stringBuff.append(", parametersNumber: ");
        stringBuff.append(parametersNumber);
        stringBuff.append(", parameters: [");
        for(int i=0; i<parameters.length; i++) {
            stringBuff.append(BinaryMath.byteToIntCorrection(parameters[i]));
            stringBuff.append(" ");
        }
        stringBuff.append("]");
        stringBuff.append(" ]");
        return stringBuff.toString();
    }
}
