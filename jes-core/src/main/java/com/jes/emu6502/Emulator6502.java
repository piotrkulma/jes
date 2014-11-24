package com.jes.emu6502;

import com.jes.Configuration;
import com.jes.emu6502.addressing.AddressingModeValueResolver;
import com.jes.emu6502.instruction.Instruction;
import com.jes.emu6502.instruction.InstructionExecutor;
import com.jes.emu6502.instruction.Mnemonic;
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

    public static final int CPU_MEMORY_SIZE = 0x10000;

    public int pc;//16-bit;
    public byte acm;
    public byte regX;
    public byte regY;
    private byte stackPointer;
    public StatusRegister sr;

    private byte[] memoryMap;

    private Map<String, String> mnemonics;
    private Map<String, Integer> bytesNumber;

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
            LOG.error(", e");
            throw new RuntimeException(e);
        }
    }

    public void runEmulation() {
        try {
            Instruction instr = getActualInstruction();
            //TODO po zaimplementowaniu adresowań oraz instrukcji
            //addressingResolver.resolveValue(instr.getCode(), instr.getParameters());
            //instructionExecutor.executeOperation(instr.getCode(), instr.getParameters());
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    public void setMemoryCell(int index, byte value) {
        memoryMap[index] = value;
    }

    public byte getMemoryCell(int index) {
        return memoryMap[index];
    }

    private Instruction getActualInstruction() {
        Instruction instruction = null;

        String operationCode = CommonUtils.convertBCDtoHex(memoryMap[pc++]);
        if(mnemonics.containsKey(operationCode)) {
            int bytes = bytesNumber.get(operationCode) - 1;
            String mnemonic = mnemonics.get(operationCode);

            instruction = new Instruction(Mnemonic.valueOf(mnemonic), null, bytes);

            if (bytes > 0) {
                for (int j = 0; j < bytes; j++) {
                    instruction.setParameter(j, memoryMap[pc++]);
                }
            }
        }

        return instruction;
    }

    private void loadOpCodesData() throws Exception{
        String line;
        URL resourceURL = ClassLoader.getSystemResource(Configuration.OPERATION_CODE_CONFIG_PATH);
        BufferedReader reader = new BufferedReader(new FileReader(new File(resourceURL.getFile())));

        mnemonics = new HashMap<String, String>();
        bytesNumber = new HashMap<String, Integer>();
        while((line = reader.readLine()) != null) {
            String array[] = line.split(",");
            mnemonics.put(array[CommonUtils.INDEX_OPERATION_CODE], array[CommonUtils.INDEX_MNEMONIC]);
            bytesNumber.put(array[CommonUtils.INDEX_OPERATION_CODE], Integer.parseInt(array[CommonUtils.INDEX_BYTES_NUMBER]));
        }

        reader.readLine();
    }

    private void initializeCpu() {
        pc = 32768;
        acm = 0;
        regX = 0;
        regY = 0;
        stackPointer = 0;

        sr = new StatusRegister();
    }
}
