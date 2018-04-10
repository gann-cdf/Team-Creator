package teamCreator.views;

import teamCreator.TeamCreator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Welcome {
    private JButton openCSVFileButton;
    private JButton quitButton;
    private JPanel panel;

    public Welcome(TeamCreator controller) {
        openCSVFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleOpenFile();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}
