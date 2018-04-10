package teamCreator;

import csv.CSVReader;
import csv.CSVWriter;
import teamCreator.core.Graph;
import teamCreator.core.merges.MergeType;
import teamCreator.core.Node;
import teamCreator.views.Generating;
import teamCreator.views.Parameters;
import teamCreator.views.Welcome;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamCreator {
    private JFrame frame;
    private CSVReader csv;
    private File workingDirectory;
    private Graph teams;

    public TeamCreator() {
        workingDirectory = new File(".");
        frame = new JFrame("TeamCreator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        handleStart();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void swapContentPane(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
    }

    public void handleStart() {
        swapContentPane(new Welcome(this).getPanel());
    }

    public void handleOpenFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            workingDirectory = fileChooser.getSelectedFile().getParentFile();
            csv = new CSVReader(fileChooser.getSelectedFile());
            swapContentPane(new Parameters(this, csv.getData().size()).getPanel());
        }
    }

    public void handleParameters(int targetTeams, int targetTeamSize, MergeType merge, boolean audit) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                CSVWriter rosterWriter = new CSVWriter(fileChooser.getSelectedFile());
                BufferedWriter logWriter = null;
                if (audit) {
                    logWriter = new BufferedWriter(new FileWriter(new File(fileChooser.getSelectedFile().getPath().replaceAll("^(.*)(\\..*)$", "$1_audit.txt"))));
                }
                Generating progress = new Generating();
                swapContentPane(progress.getPanel());
                teams = new Graph(csv.getData(), merge);
                progress.update(25);
                if (audit) {
                    logWriter.write(teams + "\n");
                    logWriter.write("Collapsing " + teams.count() + " teams to " + targetTeams + " teams of no more than " + targetTeamSize + " members " + String.valueOf(merge).toLowerCase() + " edges\n\n");
                }
                for (int distance = 0; teams.count() > targetTeams; distance++) {
                    teams.collapse(distance, targetTeamSize);
                    progress.update(25 +  75 * (targetTeams/teams.count()));
                    if (audit)
                        logWriter.write("Collapse mutual distances of " + distance + " or less, resulting in " + teams);
                }
                if (audit) logWriter.close();
                List<List<String>> rosters = new ArrayList<>();
                int team = 1;
                for (Node n : teams.getNodes()) {
                    List<String> roster = new ArrayList<>(n.getMembers());
                    roster.add(0,"Team " + team++);
                    rosters.add(roster);
                }
                rosterWriter.writeLists(rosters);
                rosterWriter.close();
                System.exit(0);
            } catch (IOException e) {
                System.err.println("I/O exception writing rosters to " + fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public static void main(String[] args) {
        new TeamCreator();
    }
}