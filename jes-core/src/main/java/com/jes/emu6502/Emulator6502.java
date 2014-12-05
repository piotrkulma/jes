package com.jes.emu6502;

import com.jes.Configuration;
import com.jes.emu6502.addressing.AddressingModeValueResolver;
import com.jes.emu6502.instruction.Instruction;
import com.jes.emu6502.instruction.InstructionExecutor;
import com.jes.emu6502.instruction.OpCodeConf;
import com.jes.utils.BinaryMath;
import com.jes.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * MOS 6502 emulation
 *
 * Created by Piotr Kulma on 2014-11-20.
 */
public class Emulator6502 {
    public static Logger LOG = LogManager.getLogger(Emulator6502.class);

    public static final int CPU_MEMORY_SIZE         = 0x10000;

    public int pc;
    public int accum;
    public int regX;
    public int regY;
    public int sp;
    public StatusRegister sr;

    private byte[] memoryMap;

    private Map<String, OpCodeConf> operationCodeMap;

    private InstructionExecutor instructionExecutor;
    private AddressingModeValueResolver addressingResolver;

    public Emulator6502() throws Exception{
        initializeOperationCodeMap();
        initializeRegisters();
        initializeMemoryMap();

        instructionExecutor = new InstructionExecutor(this);
        addressingResolver = new AddressingModeValueResolver(this);
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

    public void runEmulation() throws Exception{
        Instruction instr;
        int[] parameters;
        while (true) {
            LOG.info(MessageFormat.format("Actual line: 0x{0} ({1})", CommonUtils.convertBCDtoHex(pc), pc));
            instr = nextInstruction();
            LOG.info(instr);

            parameters = addressingResolver.resolveValue(instr.getAddressingMode(), instr.getParameters());

            if (parameters.length == 1) {
                LOG.info(MessageFormat.format("Resolved parameters v:0x{0} ", CommonUtils.convertBCDtoHex(parameters[0])));
            } else if (parameters.length == 2) {
                LOG.info(MessageFormat.format("Resolved parameters v:0x{0} a:0x{1} ",
                        CommonUtils.convertBCDtoHex(parameters[0]),
                        CommonUtils.convertBCDtoHex(parameters[1])));
            }

            instructionExecutor.executeOperation(instr.getMnemonic(), parameters);

            LOG.info(this);
            Thread.sleep(1000);

            LOG.info("##########");
        }
    }

    @Override
    public String toString() {
        StringBuffer stringBuff = new StringBuffer("");

        stringBuff.append("Emulator6502=[ ");
        stringBuff.append(sr);
        stringBuff.append(", PC: ");
        stringBuff.append(pc);
        stringBuff.append(", SP: ");
        stringBuff.append(sp);
        stringBuff.append(", A: ");
        stringBuff.append(accum);
        stringBuff.append(", X: ");
        stringBuff.append(regX);
        stringBuff.append(", Y: ");
        stringBuff.append(regY);
        stringBuff.append(" ] ");
        return stringBuff.toString();
    }

    private Instruction nextInstruction() {
        Instruction instr = null;

        String operationCode = CommonUtils.convertBCDtoHex(memoryMap[pc++]);
        StringBuffer buffer = new StringBuffer("Raw code: ");
        buffer.append(operationCode);
        buffer.append(" ");
        if(operationCodeMap.containsKey(operationCode)) {
            OpCodeConf conf = operationCodeMap.get(operationCode);
            instr = new Instruction(conf);

            if (instr.getParametersNumber()> 0) {
                for (int j = 0; j < instr.getParametersNumber(); j++) {
                    instr.setParameter(j, memoryMap[pc++]);
                    buffer.append(CommonUtils.convertBCDtoHex(BinaryMath.byteToIntCorrection(memoryMap[pc - 1])));
                    buffer.append(" ");
                }
            }
        }

        LOG.info(buffer.toString());
        return instr;
    }

    private void initializeOperationCodeMap() throws Exception{
        String line;
        URL resourceURL = ClassLoader.getSystemResource(Configuration.OPERATION_CODE_CONFIG_PATH);
        BufferedReader reader = new BufferedReader(new FileReader(new File(resourceURL.getFile())));

        operationCodeMap = new HashMap<String, OpCodeConf>();
        while((line = reader.readLine()) != null) {
            String array[] = line.split(",");

            OpCodeConf conf = new OpCodeConf(array);

            operationCodeMap.put(array[OpCodeConf.INDEX_OPERATION_CODE], conf);
        }

        reader.readLine();
    }

    private void initializeMemoryMap() {
        memoryMap = new byte[CPU_MEMORY_SIZE];

        for(int i=0; i<CPU_MEMORY_SIZE; i++) {
            memoryMap[i] = 0;
        }
    }

    private void initializeRegisters() {
        pc = 32768;
        accum = 0;
        regX = 0;
        regY = 0;
        sp = 0;

        sr = new StatusRegister();
    }
}
