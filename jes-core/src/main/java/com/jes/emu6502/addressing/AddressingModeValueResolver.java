package com.jes.emu6502.addressing;

import com.jes.emu6502.Emulator6502;
import com.jes.emu6502.addressing.AddressingMode;
import com.jes.utils.BinaryMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * Created by Piotr Kulma on 2014-11-23.
 */

public class AddressingModeValueResolver {
    private Logger LOG = LogManager.getLogger(AddressingModeValueResolver.class);

    private Emulator6502 cpu;

    public AddressingModeValueResolver(Emulator6502 emulator) {
        this.cpu = emulator;
    }

    public int[] resolveValue(AddressingMode mode, byte... attributes) {
        switch(mode) {
            case IMM: return immediate(attributes[0]);
            case AB: return absolute(attributes);
            case ABX: return absoluteX(attributes);
            case ABY: return absoluteY(attributes);
            case INX: return indirectX(attributes[0]);
            case INY: return indirectY(attributes[0]);
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
    public int[] relative(byte offset) {
        int value = (cpu.pc + offset);

        return new int[]{ value };
    }

    /**
     * Operates only on accumulator
     *
     * @return
     */
    public int[] accumulator() {
        int value = cpu.accum;

        return new int[]{ value, value };
    }


    /**
     * Implied instructions has no attributes
     *
     * @return nothing
     */
    public int[] implied() {

        return new int[] {};
    }

    /**
     * Absolute addressing specifies the memory location
     * explicitly in the two bytes following the opcode.
     *
     * Consider situation, eg:
     * params[0] = $32 - lsb
     * params[1] = $40 - msb
     * value -> memory[$4032]
     *
     * @param params
     * @return
     */
    private int[] absolute(byte[] params) {
        byte lsbAddress = params[0];
        byte msbAddress = params[1];

        int address = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);
        int value = cpu.getMemoryCell(address);

        return new int[] { value, address };
    }

    /**
     * Adding the content of the X register to an absolute
     * address, eg:
     *
     * param[0] -> $aa
     * param[1] => $bb
     * X -> $92
     * value -> memory[$bbaa + X]
     *
     * @param params
     * @return
     */
    public int[] absoluteX(byte[] params) {
        byte lsbAddress = params[0];
        byte msbAddress = params[1];
        int longAddress = cpu.getMemoryCell(BinaryMath.combineTwoBytes(msbAddress, lsbAddress));

        int destAddress = longAddress + cpu.regX;
        int value = cpu.getMemoryCell(destAddress);

        return new int[] { value, destAddress };
    }

    /**
     * Same as absoluteX but uses Y register instead.
     *
     * @param params
     * @return
     */
    public int[] absoluteY(byte[] params) {
        byte lsbAddress = params[0];
        byte msbAddress = params[1];
        int longAddress = cpu.getMemoryCell(BinaryMath.combineTwoBytes(msbAddress, lsbAddress));

        int destAddress = longAddress + cpu.regY;
        int value = cpu.getMemoryCell(destAddress);

        return new int[] { value, destAddress };
    }

    /**
     * Consider situation, eg:
     * param[0] -> $F0
     * param[1] -> $F1
     *
     * memory[$F0] -> $01 - lsb
     * memory[$F1] -> $cc - msb
     *
     * memory[$cc01] -> $xx
     * memory[$cc01 + 1] -> $yy
     *
     * value = memory[$yyxx]
     *
     * @param param adresses
     * @return
     */
    private int[] indirect(byte[] param) {
        byte lsbAddress = cpu.getMemoryCell(param[0]);
        byte msbAddress = cpu.getMemoryCell(param[1]);

        int longAddress = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);

        byte lsb = cpu.getMemoryCell(longAddress);
        byte msb = cpu.getMemoryCell(longAddress + 1);

        int destAddress = BinaryMath.combineTwoBytes(msb, lsb);
        int value = cpu.getMemoryCell(destAddress);

        return new int[] { value, destAddress };
    }

    /**
     * Consider situation, eg:
     * X -> $10
     * param -> $5
     *
     * memory[$15] -> $20
     * memory[$15 + 1] -> $44
     *
     * value -> memory[$4420]
     *
     * @param param
     * @return
     */

    private int[] indirectX(byte param) {
        int address = (byte)(param + cpu.regX);
        byte lsbAddress = cpu.getMemoryCell(address);
        byte msbAddress = cpu.getMemoryCell(address + 1);

        int destAddress = BinaryMath.combineTwoBytes(msbAddress, lsbAddress);

        int value = cpu.getMemoryCell(destAddress);

        return new int[] { value, destAddress };
    }

    /**
     * WTF addressing mode :)
     * Most fucked up addresing
     *
     * param -> $5
     * memory[$5] -> $bb
     * memory[$5 + 1] -> $aa
     *
     * value -> memory[$aabb + Y]
     *
     * @param param
     * @return
     */
    private int[] indirectY(byte param) {
        int address = cpu.getMemoryCell(param);
        byte lsbAddress = cpu.getMemoryCell(address);
        byte msbAddress = cpu.getMemoryCell(address + 1);

        int longAddress = BinaryMath.combineTwoBytes(msbAddress, lsbAddress) + cpu.regY;
        int value = cpu.getMemoryCell(longAddress);


        return new int[] { value, longAddress };
    }

    /**
     * The value is given in val.
     *
     * @param val operation value
     */
    private int[] immediate(byte val) {
        return new int[] { val };
    }

    /**
     * Capable of addressing the first 256 bytes of the CPU memory map.
     * Operation is the value stored in zeroPageAddress in memory map.
     *
     * @param zeroPageAddress address in zero page memory map
     */
    private int[] zeroPage(byte zeroPageAddress) {
        byte val = cpu.getMemoryCell(zeroPageAddress);

        return new int[] { val, zeroPageAddress };
    }

    /**
     * Consider situation, eg:
     * X -> $04
     * attr -> $20
     * (x + attr) -> $24
     * value -> memory[$24]
     *
     * @param attr operation parameter
     */
    private int[] zeroPageX(byte attr) {
        byte address = (byte)(attr + cpu.regX);

        int val = cpu.getMemoryCell(address);

        return new int[] { val, address };
    }

    /**
     * Same as zeroPageX but uses Y register
     *
     * @param attr operation attribute
     */
    private int[] zeroPageY(byte attr) {
        byte address = (byte)(attr + cpu.regY);

        int val = cpu.getMemoryCell(address);

        return new int[] { val, address };
    }
}
