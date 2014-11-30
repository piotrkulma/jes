package com.jes.emu6502.instruction;

import com.jes.emu6502.Emulator6502;
import com.jes.utils.BinaryMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jes.utils.BinaryMath.bdc;
import static com.jes.utils.BinaryMath.getBinaryArray;

/**
 * Created by Piotr Kulma on 29.11.14.
 *
 * Based on http://homepage.ntlworld.com/cyborgsystems/CS_Main/6502/6502.htm#FLG-D
 */
public class InstructionImplementation {
    private Logger LOG = LogManager.getLogger(InstructionExecutor.class);

    public static final int VALUE_SET           = 1;
    public static final int VALUE_CLEAR         = 0;

    public static final int SR_INDEX_B          = 3;

    /**
     * zero index in java arrays notation is 7th bit in
     * 6502 notation order
     */
    public static final int _7 = 0;
    public static final int _6 = 1;
    public static final int _0 = 7;

    private Emulator6502 cpu;

    public InstructionImplementation(Emulator6502 cpu) {
        this.cpu = cpu;
    }

    @InstructionImpl
    public void adc(int m) {
        int t = (cpu.accum + m + cpu.sr.C);
        cpu.sr.V = (getBinaryArray(cpu.accum)[_7] != getBinaryArray(t)[_7]) ? 1 : 0;
        cpu.sr.N = getBinaryArray(t)[_7];
        cpu.sr.Z = (t == 0) ? 1: 0;
        if(cpu.sr.D == 1) {
            t = (bdc(cpu.accum) + bdc(m) + cpu.sr.C);
            cpu.sr.C = (t>99) ? 1:0;
        } else {
            cpu.sr.C = (t > 255) ? 1 : 0;
        }

        cpu.accum = (t & 0xFF);
    }

    @InstructionImpl
    public void and(int m) {
        cpu.accum = (cpu.accum & m);
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = (cpu.accum == 0) ? 1 : 0;
    }

    @InstructionImpl
    public void asl(int b, int address) {
        cpu.sr.C = getBinaryArray(b)[_7];
        b = (b << 1);
        cpu.sr.N = getBinaryArray(b)[_7];
        cpu.sr.Z = (b==0) ? 1 : 0;

        cpu.setMemoryCell(address, (byte)b);
    }

    @InstructionImpl
    public void bcc(int b) {
        if(cpu.sr.C == VALUE_CLEAR) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void bcs(int b) {
        if(cpu.sr.C == VALUE_SET) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void beq(int b) {
        if(cpu.sr.Z == VALUE_SET) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void bit(int b) {
        int t = (cpu.accum & b);
        cpu.sr.N = getBinaryArray(t)[_7];
        cpu.sr.V = getBinaryArray(t)[_6];
        cpu.sr.Z = (t==0) ? 1 : 0;
    }

    @InstructionImpl
    public void bmi(int b) {
        if(cpu.sr.N == VALUE_SET) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void bne(int b) {
        if(cpu.sr.Z == VALUE_CLEAR) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void bpl(int b) {
        if(cpu.sr.N == VALUE_CLEAR) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void brk() {
        cpu.pc = cpu.pc + 1;

        //odłuż na stos najbardziej znaczącą część licznika rozkazów
        cpu.setMemoryCell(cpu.sp, BinaryMath.high(cpu.pc));
        cpu.sp = (cpu.sp - 1);

        //odłuż na stos najmniej znaczącą część licznika rozkazów
        cpu.setMemoryCell(cpu.sp, BinaryMath.low(cpu.pc));
        cpu.sp = (cpu.sp - 1);

        //odłuż na stos bit statusu ustaw flagę B na stosie
        //nie zmieniając jej w rejestrze procesora
        int[] sr = cpu.getStatusRegisterArray();
        sr[SR_INDEX_B] = VALUE_SET;

        cpu.setMemoryCell(cpu.sp, (byte)BinaryMath.binaryToDecimal(sr));
        cpu.sp = (cpu.sp - 1);

        //pobierz adres z wierzchołak stosu 0x FFFF FFFE i ustaw w pc
        int addrL = cpu.getMemoryCell(0xFFFE);
        int addrH = cpu.getMemoryCell(0xFFFF) << 8;

        cpu.pc = addrH | addrL;
    }

    @InstructionImpl
    public void bvc(int b) {
        if(cpu.sr.V == VALUE_CLEAR) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void bvs(int b) {
        if(cpu.sr.V == VALUE_SET) {
            cpu.pc = b;
        }
    }

    @InstructionImpl
    public void clc() {
        cpu.sr.C = VALUE_CLEAR;
    }

    @InstructionImpl
    public void cld() {
        cpu.sr.D = VALUE_CLEAR;
    }

    @InstructionImpl
    public void cli() {
        cpu.sr.I = VALUE_CLEAR;
    }

    @InstructionImpl
    public void clv() {
        cpu.sr.V = VALUE_CLEAR;
    }

    @InstructionImpl
    public void cmp(int m) {
        int test = (cpu.accum - m);
        cpu.sr.N = getBinaryArray(test)[_7];
        cpu.sr.C = (cpu.accum>=m) ? 1 : 0;
        cpu.sr.Z = (test==0) ? 1 : 0;
    }

    @InstructionImpl
    public void cpx(int m) {
        int test = (cpu.regX - m);
        cpu.sr.N = getBinaryArray(test)[_7];
        cpu.sr.C = (cpu.regX>=m) ? 1 : 0;
        cpu.sr.Z = (test==0) ? 1 : 0;
    }

    @InstructionImpl
    public void cpy(int m) {
        int test = (cpu.regY - m);
        cpu.sr.N = getBinaryArray(test)[_7];
        cpu.sr.C = (cpu.regY>=m) ? 1 : 0;
        cpu.sr.Z = (test==0) ? 1 : 0;
    }

    @InstructionImpl
    //decrement memory by one
    public void dec(int m, int address) {
        m = ((m - 1) & 0xFF);
        cpu.sr.N = getBinaryArray(m)[_7];
        cpu.sr.Z = (m==0) ? 1 : 0;

        cpu.setMemoryCell(address, (byte)m);
    }

    @InstructionImpl
    //decrement x by one
    public void dex() {
        cpu.regX = (cpu.regX - 1);
        cpu.sr.Z = (cpu.regX==0) ? 1 : 0;
        cpu.sr.N = getBinaryArray(cpu.regX)[_7];
    }

    @InstructionImpl
    //decrement y by one
    public void dey() {
        cpu.regY = (cpu.regY - 1);
        cpu.sr.Z = (cpu.regY==0) ? 1 : 0;
        cpu.sr.N = getBinaryArray(cpu.regY)[_7];
    }

    @InstructionImpl
    //exclusive OR A with memory
    public void eor(int m) {
        cpu.accum = (cpu.accum ^ m);
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = (cpu.accum==0) ? 1 : 0;
    }

    @InstructionImpl
    //increment memory by one
    public void inc(int m, int address) {
        m = ((m + 1) & 0xFF);
        cpu.sr.N = getBinaryArray(m)[_7];
        cpu.sr.Z = (m==0) ? 1:0;

        cpu.setMemoryCell(address, (byte)m);
    }

    @InstructionImpl
    //increment x by one
    public void inx() {
        cpu.regX = (cpu.regX + 1);
        cpu.sr.Z = (cpu.regX==0) ? 1:0;
        cpu.sr.N = getBinaryArray(cpu.regX)[_7];
    }

    @InstructionImpl
    //increment y by one
    public void iny() {
        cpu.regY = (cpu.regY + 1);
        cpu.sr.Z = (cpu.regY==0) ? 1:0;
        cpu.sr.N = getBinaryArray(cpu.regY)[_7];
    }

    @InstructionImpl
    //goto address
    public void jmp(int address) {
        cpu.pc = address;
    }

    @InstructionImpl
    //jump to subroutine
    public void jsr(int b) {
        int test = cpu.pc - 1;

        cpu.setMemoryCell(cpu.sp, BinaryMath.high(test));
        cpu.sp = (cpu.sp - 1);

        cpu.setMemoryCell(cpu.sp, BinaryMath.low(test));
        cpu.sp = (cpu.sp - 1);

        cpu.pc = b;
    }

    @InstructionImpl
    //load A with memory
    public void lda(int m) {
        cpu.accum = m;
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = (cpu.accum==0) ? 1 : 0;
    }

    @InstructionImpl
    //load X with memory
    public void ldx(int m) {
        cpu.regX = m;
        cpu.sr.N = getBinaryArray(cpu.regX)[_7];
        cpu.sr.Z = (cpu.regX==0) ? 1 : 0;
    }

    @InstructionImpl
    //load Y with memory
    public void ldy(int m) {
        cpu.regY = m;
        cpu.sr.N = getBinaryArray(cpu.regY)[_7];
        cpu.sr.Z = (cpu.regY==0) ? 1 : 0;
    }

    @InstructionImpl
    //logical shift right
    public void lsr(int b, int address) {
        cpu.sr.N = 0;
        cpu.sr.C = getBinaryArray(b)[_0];
        b = ((b >> 1) & 0x7F);
        cpu.sr.Z = isZero(b);

        cpu.setMemoryCell(address, (byte)b);
    }

    @InstructionImpl
    //no operation - do nothing
    public void nop() {
    }

    @InstructionImpl
    //A = A OR M
    public void ora(int m) {
        cpu.accum = (cpu.accum | m);
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = isZero(cpu.accum);
    }

    @InstructionImpl
    //push A onto stack
    public void pha() {
        cpu.setMemoryCell(cpu.sp, (byte)cpu.accum);
        cpu.sp = (cpu.sp - 1);
    }

    @InstructionImpl
    //push p (status register) on stack
    public void php() {
        cpu.setMemoryCell(cpu.sp, cpu.getStatusRegister());
        cpu.sp = (cpu.sp - 1);
    }

    @InstructionImpl
    //pull from stack to A
    public void pla() {
        cpu.sp = (cpu.sp + 1);
        cpu.accum = cpu.getMemoryCell(cpu.sp);
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = isZero(cpu.accum);
    }

    @InstructionImpl
    //pull from stack to p (status register)
    public void plp() {
        cpu.sp = (cpu.sp + 1);
        byte val = cpu.getMemoryCell(cpu.sp);
        cpu.setStatusRegister(val);
    }

    @InstructionImpl
    //rotate left
    public void rol(int b, int address) {
        int test = getBinaryArray(b)[_7];
        b = ((b << 1) & 0xFE);
        b = (b | cpu.sr.C);
        cpu.sr.C = test;
        cpu.sr.Z = isZero(b);
        cpu.sr.N = getBinaryArray(b)[_7];

        cpu.setMemoryCell(address, (byte)b);
    }

    @InstructionImpl
    //rotate right
    public void ror(int b, int address) {
        int test = getBinaryArray(b)[_0];
        b = ((b >> 1) & 0x7F);
        b = (b | ((cpu.sr.C  == 1) ? 0x80 : 0x00));
        cpu.sr.C = (byte) test;
        cpu.sr.Z = isZero(b);
        cpu.sr.N = getBinaryArray(b)[_7];

        cpu.setMemoryCell(address, (byte)b);
    }

    @InstructionImpl
    //return from interrupt
    public void rti() {
        cpu.sp = (cpu.sp - 1);
        byte sr = cpu.getMemoryCell(cpu.sp);
        cpu.setStatusRegister(sr);

        cpu.sp = (cpu.sp - 1);
        int l = cpu.getMemoryCell(cpu.sp);

        cpu.sp = (cpu.sp - 1);
        int h = cpu.getMemoryCell(cpu.sp) << 8;

        cpu.pc = (h | l);
    }

    @InstructionImpl
    //return from subroutine
    public void rts() {
        cpu.sp = (cpu.sp + 1);
        int l = cpu.getMemoryCell(cpu.sp);

        cpu.sp = (cpu.sp + 1);
        int h = cpu.getMemoryCell(cpu.sp) << 8;

        cpu.pc = (h | l) + 1;
    }

    @InstructionImpl
    //subtract memory from A with borrow
    public void sbc(int m) {
        int test;
        if(cpu.sr.D == 1) {
            test = (BinaryMath.bdc(cpu.accum) - BinaryMath.bdc(m) - (cpu.sr.C==1?0:1));
            cpu.sr.V = (test > 99 || test < 0) ? 1 : 0;
        } else {
            test = (cpu.accum - m - (cpu.sr.C==1?0:1));
            cpu.sr.V = (test > 127 || test < -128) ? 1 : 0;
        }

        cpu.sr.C = (test>=0) ? 1:0;
        cpu.sr.N = getBinaryArray(test)[_7];
        cpu.sr.Z = isZero(test);
        cpu.accum = (test & 0xFF);
    }

    @InstructionImpl
    //set C flag
    public void sec() {
        cpu.sr.C = VALUE_SET;
    }

    @InstructionImpl
    //set D flag
    public void sed() {
        cpu.sr.D = VALUE_SET;
    }

    @InstructionImpl
    //set I flag
    public void sei() {
        cpu.sr.I = VALUE_SET;
    }

    @InstructionImpl
    //store A in memory
    public void sta(int m) {
        cpu.setMemoryCell(m, (byte)cpu.accum);
    }


    @InstructionImpl  //store X in memory
    public void stx(int m) {
        cpu.setMemoryCell(m, (byte)cpu.regX);
    }

    @InstructionImpl
    //store Y in memory
    public void sty(int m) {
        cpu.setMemoryCell(m, (byte)cpu.regY);
    }

    @InstructionImpl
    //transfer accum to X
    public void tax() {
        cpu.regX = cpu.accum;
        cpu.sr.N = getBinaryArray(cpu.regX)[_7];
        cpu.sr.Z = isZero(cpu.regX);
    }

    @InstructionImpl
    //transfer accum to Y
    public void tay() {
        cpu.regY = cpu.accum;
        cpu.sr.N = getBinaryArray(cpu.regY)[_7];
        cpu.sr.Z = isZero(cpu.regY);
    }

    @InstructionImpl
    //transfer sp to x
    public void tsx() {
        cpu.regX = cpu.sp;
        cpu.sr.N = getBinaryArray(cpu.regX)[_7];
        cpu.sr.Z = isZero(cpu.regX);
    }

    @InstructionImpl
    //transfer X to A
    public void txa() {
        cpu.accum = cpu.regX;
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = isZero(cpu.accum);
    }

    @InstructionImpl
    //transfer X to SP
    public void txs() {
        cpu.sp = cpu.regX;
    }


    @InstructionImpl  //transfer Y to A
    public void tya() {
        cpu.accum = cpu.regY;
        cpu.sr.N = getBinaryArray(cpu.accum)[_7];
        cpu.sr.Z = isZero(cpu.accum);
    }

    private int isZero(int b) {
        return (b==0) ? 1 : 0;
    }
}
