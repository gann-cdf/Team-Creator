package teamCreator.views;

import teamCreator.TeamCreator;
import teamCreator.core.Graph;
import teamCreator.core.merges.MergeType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Parameters implements DocumentListener {
    private double count;
    private JPanel panel;
    private JTextField teams;
    private JTextField teamSize;
    private JButton generateTeamsButton;
    private JLabel participants;
    private JCheckBox syncTeamSize;
    private JCheckBox saveAuditLog;
    private JComboBox mergeType;
    private JComboBox islandAvoidance;

    public Parameters(TeamCreator controller, int count) {
        this.count = count;
        participants.setText(participants.getText().replace("%COUNT%", Integer.toString(count)));
        teams.getDocument().addDocumentListener(this);
        teamSize.getDocument().addDocumentListener(this);
        teams.setText("2");
        generateTeamsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleParameters(
                        Integer.parseInt(teams.getText()),
                        Integer.parseInt(teamSize.getText()),
                        (Graph.IslandAvoidance) islandAvoidance.getSelectedItem(),
                        (MergeType) mergeType.getSelectedItem(),
                        saveAuditLog.isSelected()
                );
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (e.getDocument().getLength() > 0) {
            changedUpdate(e);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        if (syncTeamSize.isSelected()) {
            if (e.getDocument() == teams.getDocument()) {
                teamSize.getDocument().removeDocumentListener(this);
                teamSize.setText(Integer.toString((int) Math.ceil(count / Integer.parseInt(teams.getText()))));
                teamSize.getDocument().addDocumentListener(this);
            } else if (e.getDocument() == teamSize.getDocument()) {
                teams.getDocument().removeDocumentListener(this);
                teams.setText(Integer.toString((int) Math.ceil(count / Integer.parseInt(teamSize.getText()))));
                teams.getDocument().addDocumentListener(this);
            }
        }
    }

    private void createUIComponents() {
        islandAvoidance = new JComboBox(Graph.IslandAvoidance.values());
        mergeType = new JComboBox(MergeType.values());
    }
}
