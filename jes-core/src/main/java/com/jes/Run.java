package com.jes;

import com.jes.nes.Nes;
import com.jes.nesfile.NESFile;
import com.jes.nesfile.NesFileBuilder;
import com.jes.utils.CommonUtils;
import com.jes.utils.Disassembler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */
public class Run {
    private static Logger LOG = LogManager.getLogger(Run.class);

    public static void main(String... args) throws Exception {
        URL resourceURL = ClassLoader.getSystemResource(Configuration.TEST_ROM_PATH);
        NESFile nesFile = NesFileBuilder.buildNESFile(resourceURL.getFile());
        Nes nes = new Nes(nesFile);

        nes.runNesEmulation();

/*        byte[] rom = CommonUtils.getPartialArray(nes.getCpu().getMemoryMap(),
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
        fos.close();*/
    }
}
