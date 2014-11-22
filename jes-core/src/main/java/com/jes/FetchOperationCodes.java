package com.jes;

import com.jes.utils.ManualDocParser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Piotr Kulma on 2014-11-22.
 */
public class FetchOperationCodes {
    public static void main(String... args) throws Exception {
        ManualDocParser parser = new ManualDocParser();
        List<String> codes = parser.parse(Configuration.M6502_MANUAL_PATH);

        FileOutputStream fos = new FileOutputStream(new File(Configuration.TEST_OPERATION_CODE_SAVING_PATH));

        for(String code: codes) {
            fos.write(code.getBytes());
            fos.write("\r\n".getBytes());
        }

        fos.flush();
        fos.close();
    }
}
