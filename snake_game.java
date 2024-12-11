package com.zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class snake_game extends JFrame {

    public snake_game() {
        load_up_user_interface();
    }

    private void load_up_user_interface() {
        create_baord();
        render_frame();
    }

    private void create_baord() {
        add(new com.zetcode.Board());
    }

    private void render_frame() {
        setResizable(false);
        pack();
        setTitle("Welcome to the Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame gameFrame = new com.zetcode.snake_game();
            gameFrame.setVisible(true);
        });
    }
}
