package com.jes.emu6502.instruction;

import com.jes.emu6502.Emulator6502;
import com.jes.emu6502.instruction.Mnemonic;

import static com.jes.utils.BinaryMath.bcd;
import static com.jes.utils.BinaryMath.*;

/**
 * Created by Piotr Kulma on 2014-11-23.
 */
public class InstructionExecutor {
    public static final int _7 = 0;
    private Emulator6502 cpu;

    public InstructionExecutor(Emulator6502 emulator) {
        this.cpu = emulator;
    }

    public void executeOperation(Mnemonic mne, byte value) {
        switch(mne) {
            case ADC: adc(value); break;
            case AND: and(value); break;
        }
    }

    private void adc(byte m) {
        byte t = (byte)(cpu.acm + m + cpu.sr.C);
        cpu.sr.V = (getBinaryArray(cpu.acm)[_7] != getBinaryArray(t)[_7]) ? 1 : 0;
        cpu.sr.N = getBinaryArray(t)[_7];
        cpu.sr.Z = (t == 0) ? 1: 0;
        if(cpu.sr.D == 1) {
            t = (byte)(bcd(cpu.acm) + bcd(m) + cpu.sr.C);
            cpu.sr.C = (t > 255) ? 1 : 0;
        }
        cpu.acm = (byte)(t & 0xFF);
    }

    private void and(byte m) {
        cpu.acm = (byte)(cpu.acm & m);
        cpu.sr.N = getBinaryArray(cpu.acm)[_7];
        cpu.sr.Z = (cpu.acm == 0) ? 1 : 0;
    }
}
