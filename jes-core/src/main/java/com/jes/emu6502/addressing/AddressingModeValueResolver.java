package com.jes.emu6502.addressing;

import com.jes.emu6502.Emulator6502;
import com.jes.emu6502.addressing.AddressingMode;
import com.jes.utils.BinaryMath;

import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 2014-11-23.
 */
public class AddressingModeValueResolver {
    private Emulator6502 cpu;

    public AddressingModeValueResolver(Emulator6502 emulator) {
        this.cpu = emulator;
    }

    public int resolveValue(AddressingMode mode, byte... attributes) {
        switch(mode) {
            case IMM: return immediate(attributes[0]);
            case AB: return absolute(attributes);
            case ABX: return absoluteX(attributes[0]);
            case ABY: return absoluteY(attributes[0]);
            case INX: break;
            case INY: break;
            case ZP: return zeroPage(attributes[0]);
            case ZPX: return zeroPageX(attributes[0]);
            case ZPY: return zeroPageY(attributes[0]);
            case ACC: return accumulator();
            case REL: return relative(attributes[0]);
            case IMP: return implied();
            case IND: return indirect(attributes);
        }

        throw new RuntimeException(MessageFormat.format("Could not resolve addresing mode ''{0}''", mode.name()));
    }

    /**
     * Relative addressing on the 6502 is only used for branch
     * operations. The byte after the opcode is the branch offset.
     * If the branch is taken, the new address will the the current
     * PC plus the offset.
     *
     * @param offset
     * @return
     */
    public int relative(byte offset) {
        int value = (cpu.pc + offset);

        return value;
    }

    /**
     * Operates only on accumulator
     *
     * @return
     */
    public int accumulator() {
        int value = cpu.acm;

        return value;
    }


    /**
     * Implied instructions has no attributes
     *
     * @return nothing
     */
    public int implied() {
        return 0;
    }

    /**
     * Absolute addressing specifies the memory location
     * explicitly in the two bytes following the opcode.
     *
     * Consider situation, eg:
     * params[0] = $32 - lsb
     * params[1] = $40 - msb
     * value -> $4032
     *
     * @param params
     * @return
     */
    private int absolute(byte[] params) {
        byte lsbAddress = params[0];
        byte msbAddress = params[1];

        int value = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);

        return value;
    }

    /**
     * Adding the content of the X register to an absolute
     * address, eg:
     *
     * param -> $2000
     * X -> $92
     * value - > $2092
     *
     * @param param
     * @return
     */
    public int absoluteX(byte param) {
        int value = (byte) (param + cpu.regX);
        return value;
    }

    /**
     * Same as absoluteX but uses Y register instead.
     *
     * @param param
     * @return
     */
    public int absoluteY(byte param) {
        int value = (byte) (param + cpu.regX);
        return value;
    }

    /**
     * Consider situation, eg:
     * param[0] -> $F0
     * param[1] -> $F1
     *
     * memory[$F0] -> $01 - lsb
     * memory[$F1] -> $cc - msb
     *
     * value = $cc01
     *
     * @param param adresses
     * @return
     */
    private int indirect(byte[] param) {
        byte lsbAddress = cpu.getMemoryCell(param[0]);
        byte msbAddress = cpu.getMemoryCell(param[1]);

        int value = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);

        return value;
    }

    /**
     * The value is given in val.
     *
     * @param val operation value
     */
    private int immediate(byte val) {
        return val;
    }

    /**
     * Capable of addressing the first 256 bytes of the CPU memory map.
     * Operation is the value stored in zeroPageAddress in memory map.
     *
     * @param zeroPageAddress address in zero page memory map
     */
    private int zeroPage(byte zeroPageAddress) {
        byte val = cpu.getMemoryCell(zeroPageAddress);

        return val;
    }

    /**
     * Consider situation, eg:
     * X -> $04
     * attr -> $20
     * (x + attr) -> $24
     * memory($24) -> $74 - lsb
     * memory($24 + 1) -> $20 - mbs
     * address -> $2074
     * value to operate with -> memory($2074)
     *
     * @param attr operation parameter
     */
    private int zeroPageX(byte attr) {
        byte sum = (byte)(attr + cpu.regX);

        byte lsbAddress = cpu.getMemoryCell(sum);
        byte msbAddress = cpu.getMemoryCell(sum + 1);

        int address = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);
        byte val = cpu.getMemoryCell(address);

        return val;
    }

    /**
     * Consider situation, eg:
     * attr -> $86
     * Y -> $10
     * memory($86) -> $28 - lsb
     * memory($86 +1) -> $40 - msb
     * lsb + Y = $28 + $10 = $38 - lsb
     * value -> memory( $(msb lsb) ) -> memory($4038)
     *
     * @param attr operation attribute
     */
    private int zeroPageY(byte attr) {
        byte lsbAddress = (byte)(cpu.getMemoryCell(attr) + cpu.regY);
        byte msbAddress = cpu.getMemoryCell(attr + 1);

        int address = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);
        byte val = cpu.getMemoryCell(address);

        return val;
    }
}
