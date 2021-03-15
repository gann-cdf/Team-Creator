package teamCreator;

import csv.CSVReader;
import csv.CSVWriter;
import teamCreator.core.Graph;
import teamCreator.core.Node;
import teamCreator.core.merges.MergeType;
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

    public void handleOpenFile(Format format, int nameIndex, int prefStartIndex, int prefEndIndex) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            workingDirectory = fileChooser.getSelectedFile().getParentFile();
            switch (format) {
                case SurveyMonkey:
                    csv = CSVReader.parseSurveyMonkeyResponses(fileChooser.getSelectedFile(), nameIndex, prefStartIndex, prefEndIndex);
                    break;
                case Office365:
                    csv = CSVReader.parseOffice365Responses(fileChooser.getSelectedFile());
                    break;
            }
            swapContentPane(new Parameters(this, csv.getData().size()).getPanel());
        }
    }

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

    public void handleParameters(int targetTeams, int targetTeamSize, Graph.IslandAvoidance islandAvoidance, MergeType merge, boolean audit) {
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

                Graph teams = new Graph(csv.getData(), islandAvoidance, merge);

                // find the longest edge in the graph
                int maxDistance = teams.getLongestEdge();

                // based on the merge algorithm, calculate the longest possible edge we want to consider
                switch (merge) {
                    case Summing:
                        // nth triangular number -- https://math.stackexchange.com/a/60581
                        maxDistance = (int) Math.ceil((Math.pow(maxDistance, 2) + maxDistance) / 2);
                        break;
                    case Averaging:
                    case Maximizing:
                    default:
                        // just use our discovered maxDistance
                        break;
                }
                progress.update(25);
                if (audit) {
                    logWriter.write(teams + "\n");
                    logWriter.write("Collapsing " + teams.count() + " teams to " + targetTeams + " teams of no more than " + targetTeamSize + " members " + String.valueOf(merge).toLowerCase() + " edges, collapsing to a maximum distance of " + maxDistance + "\n\n");
                }

                int prevCount = 0;
                for (int distance = 0; teams.count() > targetTeams && distance <= maxDistance; distance++) {
                    teams.collapse(distance, targetTeamSize);
                    progress.update(25 + 75 * (targetTeams / teams.count()));
                    if (teams.count() != prevCount && audit) {
                        logWriter.write("Collapse mutual distances of " + distance + " or less, resulting in " + teams);
                        prevCount = teams.count();
                    }
                }
                if (audit) {
                    logWriter.write("Final " + teams);
                    logWriter.close();
                }
                List<List<String>> rosters = new ArrayList<>();
                int team = 1;
                for (Node n : teams.getNodes()) {
                    List<String> roster = new ArrayList<>(n.getMembers());
                    roster.add(0, "Team " + team++);
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

    public enum Format {SurveyMonkey, Office365}

    public static void main(String[] args) {
        new TeamCreator();
    }
}