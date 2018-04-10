package teamCreator.views;

import javax.swing.*;

public class Generating {
    private JProgressBar progressBar1;
    private JPanel panel;

    public void update(int value) {
        progressBar1.setValue(value);
    }

    public JPanel getPanel() {
         return panel;
    }
}
