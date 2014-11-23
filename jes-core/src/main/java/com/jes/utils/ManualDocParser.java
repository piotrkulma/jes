package com.jes.utils;

import com.jes.emu6502.addressing.AddressingMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-22.
 */
public class ManualDocParser {
    private Logger LOG = LogManager.getLogger(ManualDocParser.class);

    private static final String SEPARATOR = ",";
    private static final String DOCUMENTATION_HEADER_TAG = "| Addressing Mode| Assembly Language Form| OP CODE |No. Bytes|No. Cycles|";
    private static final String DOCUMENTATION_BORDER_TAG = "+----------------+-----------------------+---------+---------+----------+";
    private static final String ADDRESING_MODE_HEADERS[] =
            {
                    "Immediate",
                    "Absolute",
                    "Absolute,X",
                    "Absolute,Y",
                    "(Indirect,X)",
                    "(Indirect),Y",
                    "Zero Page",
                    "Zero Page,X",
                    "Zero Page,Y",
                    "Accumulator",
                    "Relative",
                    "Implied",
                    "Indirect",
            };

    public ManualDocParser() {
    }

    public List<String> parse(String manualPath) throws Exception {
        URL resourceURL = ClassLoader.getSystemResource(manualPath);
        File file = new File(resourceURL.getFile());
        BufferedReader reader = new BufferedReader(new FileReader(file));

        return processFile(reader);
    }

    private List<String> processFile(BufferedReader reader) throws Exception {
        String line, temp, mnemonic = "";
        List<String> doc = new ArrayList<String>();

        LOG.info("PARSING MANUAL");

        while((line = reader.readLine()) != null) {
            temp = getMnemonic(line);
            if(temp != null) {
                mnemonic = temp;
            }
            processOperationCodeSection(reader, doc, mnemonic, line);
        }

        LOG.info(MessageFormat.format("INSTRUCTIONS FOUND {0}", doc.size()));
        reader.close();

        return doc;
    }

    private void processOperationCodeSection(BufferedReader reader, List<String> doc, String mnemonic, String line) throws Exception{
        if(line.contains(DOCUMENTATION_HEADER_TAG)){
            reader.readLine();
            while(!(line = reader.readLine()).contains(DOCUMENTATION_BORDER_TAG)) {
                String[] array = line.split("\\|");
                String opcodeInfo = stringArrayToString(array, mnemonic);
                doc.add(opcodeInfo);
                LOG.info(opcodeInfo);
            }
        }
    }

    private String getMnemonic(String line) {
        int counter = 0;
        String[] rawArray = line.split("     ");

        for(int i=0; i<rawArray.length; i++) {
            rawArray[i] = rawArray[i].trim();
            if(rawArray[i].length()> 0) {
                counter++;
            }
        }

        if(counter == 3 && rawArray[0].equals(rawArray[rawArray.length-1])) {
            LOG.info(MessageFormat.format("Found instruction set ''{0}''", rawArray[0]));
            return rawArray[0];
        }

        return null;
    }

    private String replaceAddresingHeader(String am) {
        for(int i=0; i<ADDRESING_MODE_HEADERS.length; i++) {
            if(ADDRESING_MODE_HEADERS[i].equals(am)) {
                return AddressingMode.values()[i].name();
            }
        }
        throw new RuntimeException(MessageFormat.format("Code ''{0}'' not found", am));
    }

    private String stringArrayToString(String[] array, String mnemonic) {
        StringBuilder builder = new StringBuilder("");

        for(int i=0; i<array.length; i++) {
            array[i] = array[i].trim();
        }

        builder.append(array[3]);
        builder.append(SEPARATOR);
        builder.append(mnemonic);
        builder.append(SEPARATOR);
        builder.append(replaceAddresingHeader(array[1]));
        builder.append(SEPARATOR);
        builder.append(array[4]);
        builder.append(SEPARATOR);
        builder.append(array[5]);

        return builder.toString();
    }
}
