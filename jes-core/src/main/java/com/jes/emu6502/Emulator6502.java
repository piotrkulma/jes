package com.jes.emu6502;

import com.jes.Configuration;
import com.jes.emu6502.addressing.AddressingMode;
import com.jes.emu6502.addressing.AddressingModeValueResolver;
import com.jes.emu6502.instruction.Instruction;
import com.jes.emu6502.instruction.InstructionExecutor;
import com.jes.emu6502.instruction.Mnemonic;
import com.jes.emu6502.instruction.OpCodeConf;
import com.jes.utils.BinaryMath;
import com.jes.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Piotr Kulma on 2014-11-20.
 */
public class Emulator6502 {
    public static Logger LOG = LogManager.getLogger(Emulator6502.class);


    public static final int INDEX_OPERATION_CODE    = 0;
    public static final int INDEX_MNEMONIC          = 1;
    public static final int INDEX_ADDRESS_MODE      = 2;
    public static final int INDEX_BYTES_NUMBER      = 3;
    public static final int INDEX_CYCLES_NUMBER     = 4;


    public static final int CPU_MEMORY_SIZE     = 0x10000;

    public static final int SR_INDEX_B          = 3;

    public static final int VALUE_SET           = 1;
    public static final int VALUE_CLEAR         = 0;

    public int pc;//16-bit;
    public byte accum;
    public byte regX;
    public byte regY;
    public byte sp;
    public StatusRegister sr;

    private byte[] memoryMap;

    private Map<String, OpCodeConf> operationCodeConfMap;

    private InstructionExecutor instructionExecutor;
    private AddressingModeValueResolver addressingResolver;

    public Emulator6502() {
        try {
            loadOpCodesData();
            initializeCpu();

            memoryMap = new byte[CPU_MEMORY_SIZE];

            instructionExecutor = new InstructionExecutor(this);
            addressingResolver = new AddressingModeValueResolver(this);
        }catch(Exception e) {
            LOG.error("", e);
            throw new RuntimeException(e);
        }
    }

    public void runEmulation() {
        try {
            Instruction instr = getActualInstruction();
            int[] parameters = addressingResolver.resolveValue(instr.getAddressingMode(), instr.getParameters());
            instructionExecutor.executeOperation(instr.getMnemonic(), parameters);
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    public void setMemoryCell(int address, byte value) {
        memoryMap[address] = value;
    }

    public byte getMemoryCell(int address) {
        return memoryMap[address];
    }

    public byte getStatusRegister() {
        int[] array = {sr.N, sr.V, 1, sr.B, sr.D, sr.I, sr.Z, sr.C};

        return (byte)BinaryMath.binaryToDecimal(array);
    }

    public int[] getStatusRegisterArray() {
        int[] array = {sr.N, sr.V, 1, sr.B, sr.D, sr.I, sr.Z, sr.C};

        return array;
    }

    public void setStatusRegister(byte b) {
        int[] array = BinaryMath.getBinaryArray(b);
        sr.N = array[0];
        sr.V = array[1];
        //none = array[2];
        sr.B = array[3];
        sr.D = array[4];
        sr.I = array[5];
        sr.Z = array[6];
        sr.C = array[7];
    }

    private Instruction getActualInstruction() {
        Instruction instr = null;

        String operationCode = CommonUtils.convertBCDtoHex(memoryMap[pc++]);
        if(operationCodeConfMap.containsKey(operationCode)) {
            OpCodeConf conf = operationCodeConfMap.get(operationCode);
            instr = new Instruction(conf);

            if (instr.getParametersNumber()> 0) {
                for (int j = 0; j < instr.getParametersNumber(); j++) {
                    instr.setParameter(j, memoryMap[pc++]);
                }
            }
        }

        return instr;
    }

    private void loadOpCodesData() throws Exception{
        String line;
        URL resourceURL = ClassLoader.getSystemResource(Configuration.OPERATION_CODE_CONFIG_PATH);
        BufferedReader reader = new BufferedReader(new FileReader(new File(resourceURL.getFile())));

        operationCodeConfMap = new HashMap<String, OpCodeConf>();
        while((line = reader.readLine()) != null) {
            String array[] = line.split(",");

            OpCodeConf conf = new OpCodeConf(
                    array[INDEX_OPERATION_CODE],
                    Mnemonic.valueOf(array[INDEX_MNEMONIC]),
                    AddressingMode.valueOf(array[INDEX_ADDRESS_MODE]),
                    Integer.parseInt(array[INDEX_BYTES_NUMBER]),
                    array[INDEX_CYCLES_NUMBER]);

            operationCodeConfMap.put(array[INDEX_OPERATION_CODE], conf);
        }

        reader.readLine();
    }

    private void initializeCpu() {
        pc = 32768;
        accum = 0;
        regX = 0;
        regY = 0;
        sp = 0;

        sr = new StatusRegister();
    }
}
