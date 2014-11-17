package com.jes.experiments;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Piotr Kulma on 2014-11-16.
 */

public class ExtractorForm extends JFrame {
    public ExtractorForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(new ExtractorPanel(), BorderLayout.CENTER);
        pack();
        setSize(500, 500);
        setVisible(true);
    }
}
