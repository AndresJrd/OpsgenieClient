package view;

import config.Config;
import entity.Teams;
import helper.KeyValue;
import view.util.NumericTextField;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import entity.Response;
import http.ClientHttp;
import util.Exporter;
import util.Methods;

import java.net.URL;
import java.util.Comparator;


public class PopUp {
    JFrame jfM;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void about(TrayIcon trayIcon) {
        try {
            trayIcon.displayMessage("# Opsgenie Client beta", "Copyrigth © PostCompra ML ", TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void exit() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Estas seguro que deseas salir?"));
        int result = JOptionPane.showConfirmDialog(null, panel, "#ID Opsgenie Client",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void alarmsByTeam(Config config) {
        JComboBox comboTeams;
        JTextField fielAlarmsToFind = new NumericTextField(false, false);
        JTextField fielFileName = new JTextField();
        JLabel jLabelStatus = new JLabel("");
        jLabelStatus.setForeground(Color.red);
        comboTeams = new JComboBox();
        try {
            String url = "https://api.opsgenie.com/v2/teams/";
            System.out.println("url:" + url);
            ClientHttp clientHttp = new ClientHttp(new URL(url), config.getToken());
            Teams teams = clientHttp.executeRequest(clientHttp.setConfig(Methods.GET.toString()), Teams.class);
            teams.getData().sort(Comparator.comparing(a->a.getName()));
            teams.getData().stream().forEach(team -> {
                comboTeams.addItem(new KeyValue(team.getName(),team.getId()));
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JButton jb2 = new JButton("Find");
        try {
            JPanel jp1;


            jfM = new JFrame("Opsgenie Alarms ");
            jfM.setLayout(null);

            jp1 = new JPanel(new GridLayout(4, 2, 2, 2));
            jp1.setBounds(10, 10, 300, 100);
            jp1.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.black),
                    BorderFactory.createEmptyBorder(10, 15, 5, 15)));
            jp1.setVisible(true);

            jp1.add(new JLabel("Team"));
            jp1.add(comboTeams);

            jp1.add(new JLabel("Last Alarms"));
            fielAlarmsToFind.setText("100");

            jp1.add(fielAlarmsToFind);
            jp1.add(new JLabel("File name"));
            fielFileName.setText("report");
            jp1.add(fielFileName);
            jp1.add(jb2);
            jp1.add(jLabelStatus);

            jfM.add(jp1);

            jfM.setLocation(100, 100);
            jfM.setResizable(true);
            jfM.setVisible(true);
            jfM.setSize(325, 160);
            jfM.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            ActionListener findActionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] options = {"Ok", "Cancel"};
                    int result = JOptionPane.showOptionDialog(jfM, "Sure to download report and export to .xlsx file?", "Opsgenie Client",
                            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                    if (result == JOptionPane.OK_OPTION) {
                        jLabelStatus.setText("Downloading reports...");
                        SwingWorker sWorker = new SwingWorker() {
                            public Boolean doInBackground() throws Exception {
                                String url = "https://api.opsgenie.com/v2/alerts?limit=" + fielAlarmsToFind.getText() + "&sort=createdAt&offset=0&order=desc&query=teams="+((KeyValue) comboTeams.getSelectedItem()).getValue();
                                System.out.println("url:" + url);
                                ClientHttp clientHttp = new ClientHttp(new URL(url), config.getToken());
                                Response response = clientHttp.executeRequest(clientHttp.setConfig(Methods.GET.toString()), Response.class);
                                Exporter exporter = new Exporter("OpsGenieAlarm Detail");
                                return exporter.exportDataDetail(response, fielFileName.getText());
                            }

                            public void done() {
                                jLabelStatus.setText("Process finished!");
                            }
                        };
                        sWorker.execute();
                    }
                }
            };
            jb2.addActionListener(findActionListener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
