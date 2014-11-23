package com.jes.utils;

import com.jes.Configuration;
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
 * Created by Piotr Kulma on 2014-11-22.
 */
public class Disassembler {
    public static Logger LOG = LogManager.getLogger(Disassembler.class);

    private Map<String, String> mnemonics;
    private Map<String, Integer> bytesNumber;
    public Disassembler() {
        try {
            loadOpCodesData();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> disassemble(byte[] machineCode) {
        List<String> asmCode = new ArrayList<String>();

        int instrNumber = 32768;
        for(int i=0; i<machineCode.length; i++) {
            String operationCode = CommonUtils.convertBCDtoHex(machineCode[i]);
            if(mnemonics.containsKey(operationCode)) {
                int bytes = bytesNumber.get(operationCode) - 1;
                String instruction = mnemonics.get(operationCode);

                if (bytes > 0) {
                    for (int j = 0; j < bytes; j++) {
                        instruction += " " + CommonUtils.convertBCDtoHex(machineCode[++i]);
                    }
                }
                asmCode.add(Integer.toHexString(instrNumber++) + " " + instruction);
            } else {
                asmCode.add(Integer.toHexString(instrNumber++) + " " + operationCode);
            }
        }
        return asmCode;
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
}