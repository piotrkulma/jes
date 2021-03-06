package com.jes.emu6502.instruction;

import com.jes.emu6502.Emulator6502;
import com.jes.utils.BinaryMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Piotr Kulma on 2014-11-23.
 */
public class InstructionExecutor {
    private Logger LOG = LogManager.getLogger(InstructionExecutor.class);

    private Emulator6502 cpu;
    private InstructionImplementation instruction;

    private Map<Mnemonic, Method> methodMap;

    public InstructionExecutor(Emulator6502 emulator) throws Exception {
        this.cpu = emulator;
        this.instruction = new InstructionImplementation(cpu);

        initializeMethodMap();
    }

    public void executeOperation(Instruction instr, int[] p) throws Exception {
        invokeMethod(instr, p);
    }

    private void invokeMethod(Instruction instr, int[] p) throws Exception {
        Method method = methodMap.get(instr.getMnemonic());

        if(method == null) {
            throw new RuntimeException(MessageFormat.format("Method not found for mnemonic '''{0}'", instr.getMnemonic().name()));
        }
        int parameters = method.getParameterCount();

        if(parameters == 0) {
            method.invoke(instruction);
        } else if(parameters == 1) {
            method.invoke(instruction, BinaryMath.byteToIntCorrection(p[0]));
        } else if(parameters == 2){
            method.invoke(instruction, BinaryMath.byteToIntCorrection(p[0]), BinaryMath.byteToIntCorrection(p[1]));
        } else if(parameters == 3){
            method.invoke(instruction, BinaryMath.byteToIntCorrection(p[0]), BinaryMath.byteToIntCorrection(p[1]), instr.getAddressingMode());
        }  else {
            throw new RuntimeException("Too much method parameters");
        }
    }

    private void initializeMethodMap() throws Exception {
        Mnemonic[] mnemonics = Mnemonic.values();

        Method[] methods = instruction.getClass().getMethods();

        int count = 0;
        methodMap = new HashMap<Mnemonic, Method>();

        for(Mnemonic mnemonic : mnemonics) {
            for(Method method : methods) {
                Annotation annotation = method.getAnnotation(InstructionImpl.class);

                if(annotation != null && mnemonic.name().equals(method.getName().toUpperCase())) {
                    methodMap.put(mnemonic, method);
                    count++;
                    LOG.info("Metod added to mapper: " + method.getName());
                    break;
                }
            }
        }

        LOG.info(MessageFormat.format("Mapper found {0} methods", count));
    }
}
