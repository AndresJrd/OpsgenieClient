
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import io.Io;
import view.PopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Main {
    static TrayIcon trayIcon;
    static PopUp pop = new PopUp();
    static Config config;

    public static void main(String[] args) {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                MenuItem about = new MenuItem("About...");
                MenuItem exit = new MenuItem("Exit");

                Image image = Toolkit.getDefaultToolkit().getImage("ops.png");
                trayIcon = new TrayIcon(image, "Genius");
                trayIcon.setImageAutoSize(true);
                final PopupMenu popMenu = new PopupMenu();
                Menu displayMenu = new Menu("Reports");
                MenuItem alarmReports = new MenuItem("Alarms");
                displayMenu.add(alarmReports);

                popMenu.add(displayMenu);
                popMenu.addSeparator();
                popMenu.add(about);
                popMenu.addSeparator();
                popMenu.add(exit);
                try {
                    tray.add(trayIcon);
                    trayIcon.setToolTip("#Opsgenie MercadoLibre ©");

                    ObjectMapper mapper = new ObjectMapper();
                    String data=Io.readFile("ops.json");
                    data=data.replace("[","");
                    data=data.replace("]","");

                    System.out.println(data);
                    config = mapper.readValue(data, Config.class);

                } catch (AWTException e) {
                    JOptionPane.showMessageDialog(null, "File not found opsgenie.json", "Opsgenie Client", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }

                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        MenuItem item = (MenuItem) e.getSource();
                        if ("Exit".equals(item.getLabel())) {
                            pop.exit();
                        }
                        if ("About...".equals(item.getLabel())) {
                            pop.about(trayIcon);
                        }
                        if("Alarms".equals(item.getLabel())){
                            pop.alarmsByTeam(config);
                        }

                    }
                };
                trayIcon.setPopupMenu(popMenu);

                exit.addActionListener(listener);
                about.addActionListener(listener);
                alarmReports.addActionListener(listener);

            } else
                System.out.println("Not supported");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
