package main;

import view.LoginView;
import view.ModernUI;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModernUI.install();
            new LoginView().setVisible(true);
        });
    }
}