package com.jes.emu6502;

import com.jes.Configuration;
import com.jes.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Piotr Kulma on 2014-11-20.
 */
public class Emulator6502 {
    public static Logger LOG = LogManager.getLogger(Emulator6502.class);

    public static final int CPU_MEMORY_SIZE = 0x10000;

    private int programCounter;//16-bit;
    private byte accumulator;
    private byte registerX;
    private byte registerY;
    private byte stackPointer;
    private StatusRegister statusRegister;

    private byte[] memoryMap;

    private Map<String, String> mnemonics;
    private Map<String, Integer> bytesNumber;

    public Emulator6502() {
        try {
            loadOpCodesData();
            initializeCpu();

            memoryMap = new byte[CPU_MEMORY_SIZE];
        }catch(Exception e) {
            LOG.error(", e");
            throw new RuntimeException(e);
        }
    }

    public void runEmulation() {
        try {
            //while(true) {
                Instruction instr = getActualInstruction();
                executeInstruction(instr);
            LOG.info("xxx");
                //Thread.sleep(400);
           // }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    private void executeInstruction(Instruction instr) {
        //TODO
    }

    private Instruction getActualInstruction() {
        Instruction instruction = null;

        String operationCode = CommonUtils.convertBCDtoHex(memoryMap[programCounter++]);
        if(mnemonics.containsKey(operationCode)) {
            int bytes = bytesNumber.get(operationCode) - 1;
            String mnemonic = mnemonics.get(operationCode);

            instruction = new Instruction(mnemonic, bytes);

            if (bytes > 0) {
                for (int j = 0; j < bytes; j++) {
                    instruction.setParameter(j, memoryMap[programCounter++]);
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
        programCounter = 32768;
        accumulator = 0;
        registerX = 0;
        registerY = 0;
        stackPointer = 0;

        statusRegister = new StatusRegister();
    }

    public byte[] getMemoryMap() {
        return memoryMap;
    }

    public void setMemoryCell(int index, byte value) {
        memoryMap[index] = value;
    }
}
