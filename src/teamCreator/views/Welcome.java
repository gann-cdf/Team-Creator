package teamCreator.views;

import teamCreator.TeamCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Welcome {
    public static final String OFFICE365 = "Office 365";
    public static final String SURVEYMONKEY = "SurveyMonkey";

    private JButton openCSVFileButton;
    private JButton quitButton;
    private JPanel panel;
    private JComboBox<TeamCreator.Format> format;
    private JTextField nameIndex;
    private JTextField prefStartIndex;
    private JTextField prefEndIndex;
    private JLabel formatDescription;
    private JLabel nameIndexLabel;
    private JLabel prefStartIndexLabel;
    private JLabel prefEndIndexLabel;

    public Welcome(TeamCreator controller) {
        openCSVFileButton.addActionListener(e -> {
            if (OFFICE365.equals(format.getSelectedItem())) {
                controller.handleOpenFile(TeamCreator.Format.Office365, 0, 0, 0);
            } else if (SURVEYMONKEY.equals(format.getSelectedItem())) {
                if (nameIndex.getText().length() > 0 && prefStartIndex.getText().length() > 0 && prefEndIndex.getText().length() > 0) {
                    controller.handleOpenFile(TeamCreator.Format.SurveyMonkey, Integer.parseInt(nameIndex.getText()), Integer.parseInt(prefStartIndex.getText()), Integer.parseInt(prefEndIndex.getText()));
                }
            }
        });
        quitButton.addActionListener(e -> System.exit(0));
        format.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (OFFICE365.equals(format.getSelectedItem())) {
                    formatDescription.setText(
                            "<html>" +
                                    "<p>This program expects a 2-column CSV file.</p>" +
                                    "<p>The first column is a list of the participants' names and the second " +
                                    "column is a semicolon-separated list of that participants ranking (from " +
                                    "most to least desired) of the other participants.</p>" +
                                    "</html>"
                    );
                    nameIndex.setVisible(false);
                    nameIndexLabel.setVisible(false);
                    prefStartIndex.setVisible(false);
                    prefStartIndexLabel.setVisible(false);
                    prefEndIndex.setVisible(false);
                    prefEndIndexLabel.setVisible(false);
                } else if (SURVEYMONKEY.equals(format.getSelectedItem())) {
                    formatDescription.setText(
                            "<html>" +
                                    "<p>This program expects a CSV file with a first row of questions, followed " +
                                    "by a row of column headers, followed by survey responses.</p>" +
                                    "<p>The respondent's name should be in one column (nameIndex, zero-indexed), " +
                                    "while the rankings of other people are numerically represented in a " +
                                    "contiguous block of columns from prefStartIndex to prefEndIndex (zero-" +
                                    "indexed).</p>" +
                                    "</html>"
                    );
                    nameIndex.setVisible(true);
                    nameIndexLabel.setVisible(true);
                    prefStartIndex.setVisible(true);
                    prefStartIndexLabel.setVisible(true);
                    prefEndIndex.setVisible(true);
                    prefEndIndexLabel.setVisible(true);
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}
