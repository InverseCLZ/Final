package com.zetcode;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


//----------------------------------------------------------------------------------------------------------------------
public class Board extends JPanel implements ActionListener {

    private final int board_width = 300;
    private final int board_height = 300;
    private final int food_size = 10;
    private final int all_food = 900;
    private final int randomized_positioning = 29;
    private final int short_delay = 140;

    private final int x[] = new int[all_food];
    private final int y[] = new int[all_food];

    private int dots;
    private int food_x;
    private int food_y;

    private boolean go_left = false;
    private boolean go_right = true;
    private boolean go_up = false;
    private boolean go_down = false;
    private boolean in_game = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
//----------------------------------------------------------------------------------------------------------------------
    public Board() {
        gameboard_setup();
    }
    private void gameboard_setup() {
        key_binds();
        gameboard_style();
        game_resources_render();
        starting_new_game();
    }
    private void key_binds() {
        addKeyListener(new TAdapter());
        setFocusable(true);
    }
    private void gameboard_style() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(board_width, board_height));
    }
    private void game_resources_render() {
        render_game_images();
    }
    private void starting_new_game() {
        initGame();
    }
//----------------------------------------------------------------------------------------------------------------------
private void render_game_images() {
    ball = render_image("src/resources/dot.png");
    apple = render_image("src/resources/apple.png");
    head = render_image("src/resources/head.png");
}
    private Image render_image(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        return icon.getImage();
    }
//----------------------------------------------------------------------------------------------------------------------
    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        spawn_more_food();
        timer = new Timer(short_delay, this);
        timer.start();
    }
//----------------------------------------------------------------------------------------------------------------------
@Override
public void paintComponent(Graphics g) {
    super.paintComponent(g);
    render_gameplay(g);
}

    private void render_gameplay(Graphics g) {
        if (in_game) {
            draw_food(g);
            draw_snake(g);
            Toolkit.getDefaultToolkit().sync();
        } else {
            game_over_prompt(g);
        }
    }

    private void draw_food(Graphics g) {
        g.drawImage(apple, food_x, food_y, this);
    }

    private void draw_snake(Graphics g) {
        for (int z = 0; z < dots; z++) {
            if (z == 0) {
                g.drawImage(head, x[z], y[z], this); // Draw head of the snake
            } else {
                g.drawImage(ball, x[z], y[z], this); // Draw body of the snake
            }
        }
    }
    private void game_over_prompt(Graphics g) {
        game_over_lol(g);
    }

//----------------------------------------------------------------------------------------------------------------------

    private void game_over_lol(Graphics g) {
        String user_message = "Game Over";
        Font small_font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(small_font);
        g.setColor(Color.white);
        g.setFont(small_font);
        int messageX = (board_width - metrics.stringWidth(user_message)) / 2;
        int messageY = board_height / 2;

        g.drawString(user_message, messageX, messageY);
    }

    private void check_food_absorption() {
        boolean is_food_eaten = (x[0] == food_x) && (y[0] == food_y);
        if (is_food_eaten) {
            dots++;
            spawn_more_food();
        }
    }

    //----------------------------------------------------------------------------------------------------------------------
private void updateSnakePosition() {
    // Shifting pos of the snake body segments
    for (int z = dots; z > 0; z--) {
        x[z] = x[z - 1];
        y[z] = y[z - 1];
    }

    // Update the head position based on the current direction
    if (go_left) {
        x[0] -= food_size;
    } else if (go_right) {
        x[0] += food_size;
    } else if (go_up) {
        y[0] -= food_size;
    } else if (go_down) {
        y[0] += food_size;
    }
}

//----------------------------------------------------------------------------------------------------------------------
private void checkCollision() {
    for (int z = dots; z > 0; z--) {
        boolean selfCollision = (z > 4) && (x[0] == x[z]) && (y[0] == y[z]);
        if (selfCollision) {
            in_game = false;
        }
    }

    boolean outOfBounds = (y[0] >= board_height) || (y[0] < 0) || (x[0] >= board_width) || (x[0] < 0);
    if (outOfBounds) {
        in_game = false;
    }
    if (!in_game) {
        timer.stop();
    }
}

//----------------------------------------------------------------------------------------------------------------------
    private void spawn_more_food() {

        int r = (int) (Math.random() * randomized_positioning);
        food_x = ((r * food_size));

        r = (int) (Math.random() * randomized_positioning);
        food_y = ((r * food_size));
    }
    //----------------------------------------------------------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (in_game) {
            check_food_absorption();
            checkCollision();
            move();
        }
        repaint();
    }
    //----------------------------------------------------------------------------------------------------------------------
    private void move() {
    }
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
//----------------------------------------------------------------------------------------------------------------------
            if ((key == KeyEvent.VK_LEFT) && (!go_right)) {
                go_left = true;
                go_up = false;
                go_down = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!go_left)) {
                go_right = true;
                go_up = false;
                go_down = false;
            }
            if ((key == KeyEvent.VK_UP) && (!go_down)) {
                go_up = true;
                go_right = false;
                go_left = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!go_up)) {
                go_down = true;
                go_right = false;
                go_left = false;
            }
        }
    }
}
