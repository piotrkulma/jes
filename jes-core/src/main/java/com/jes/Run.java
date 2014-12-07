package com.jes;

import com.jes.nes.Nes;
import com.jes.nesfile.NESFile;
import com.jes.nesfile.NesFileBuilder;
import com.jes.utils.CommonUtils;
import com.jes.utils.Disassembler;
import com.jes.utils.ManualDocParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */
public class Run {
    private static Logger LOG = LogManager.getLogger(Run.class);

    public static void main(String... args) throws Exception {
        //runTest();
        emulation();
        //dissassemble();
        //generateOpCodeConf();
    }

    private static void runTest() throws Exception {
        List<String> listA = fetchTextFileContent("nes_log.log");
        List<String> listB = fetchTextFileContent("nestest.log");

        LOG.info(MessageFormat.format("{0} {1}", listA.size(), listB.size()));

        for(int i=0; i<listA.size(); i++) {
            String lineA = listA.get(i);
            int indexA = lineA.indexOf("SR:");

            String testA = lineA.substring(indexA+3, indexA + 5);

            String lineB = listB.get(i);
            int indexB = lineB.indexOf("P:");

            String testB = lineB.substring(indexB+2, indexB + 4);

            if(!testA.equals(testB)) {
                LOG.info(listA.get(i));
                LOG.info(listB.get(i));
                break;
            }
        }
    }

    private static List<String> fetchTextFileContent(String path) throws Exception {
        List<String> list = new ArrayList<String>();

        URL url = ClassLoader.getSystemResource(path);
        FileReader fr = new FileReader(new File(url.getFile()));
        BufferedReader br = new BufferedReader(fr);

        String line;

        while((line = br.readLine()) != null) {
            list.add(line);
        }

        return list;
    }

    private static void generateOpCodeConf() throws Exception{
        ManualDocParser parser = new ManualDocParser();
        List<String> confs = parser.parse(Configuration.M6502_MANUAL_PATH);
        FileOutputStream fos = new FileOutputStream(new File(Configuration.TEST_OPERATION_CODE_SAVING_PATH));

        for(String conf : confs) {
            LOG.info(conf);
            fos.write(conf.getBytes());
            fos.write("\r\n".getBytes());
        }

        fos.flush();
        fos.close();
    }

    private static void emulation() throws Exception{
        URL resourceURL = ClassLoader.getSystemResource(Configuration.TEST_ROM_PATH);
        NESFile nesFile = NesFileBuilder.buildNESFile(resourceURL.getFile());
        Nes nes = new Nes(nesFile);

        nes.runNesEmulation();
    }

    private static void dissassemble() throws Exception {
        URL resourceURL = ClassLoader.getSystemResource(Configuration.TEST_ROM_PATH);
        NESFile nesFile = NesFileBuilder.buildNESFile(resourceURL.getFile());
        Nes nes = new Nes(nesFile);


        byte[] rom = CommonUtils.getPartialArray(nes.getCpu().memoryMap,
                Nes.CPU_ROM_LOWER_BANK_START_ADDR,
                Nes.CPU_ROM_UPPER_BANK_END_ADDR);


        FileOutputStream fos = new FileOutputStream(new File(Configuration.TEST_PROGRAM_MEMORY_DUMP_PATH));

        Disassembler disassembler = new Disassembler();
        List<String> assembler = disassembler.disassemble(rom);

        for(String asm : assembler) {
            LOG.info(asm);
            fos.write(asm.getBytes());
            fos.write("\r\n".getBytes());
        }

        fos.flush();
        fos.close();
    }
}
