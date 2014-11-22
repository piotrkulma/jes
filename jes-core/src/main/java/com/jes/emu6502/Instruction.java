package com.jes.emu6502;

/**
 * Created by Piotr Kulma on 2014-11-22.
 */
public class Instruction {
    private String code;
    private byte[] parameters;

    public Instruction(String code, int paramNo) {
        this.code = code;
        this.parameters = new byte[paramNo];
    }

    public String getCode() {
        return code;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public void setParameter(int index, byte parameter) {
        this.parameters[index] = parameter;
    }
}
