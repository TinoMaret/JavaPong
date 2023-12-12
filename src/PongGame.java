import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class PongGame extends JPanel implements ActionListener {
    private final int SCREEN_WIDTH = 1000;
    private final int SCREEN_HEIGHT = 600;
    private boolean running = true;
    private int player1Score = 0;
    private int player2Score = 0;
    private final int PLAYER_1_POSITION_X = 100;
    private final int PLAYER_2_POSITION_X = 900;
    private int player1Position = SCREEN_HEIGHT/2-50;
    private int player2Position = SCREEN_HEIGHT/2-50;
    private final int PLAYER_DIMENSION_X = 30;
    private final int PLAYER_DIMENSION_Y = 100;
    private final int PLAYER_VELOCITY = 10;
    private int ballVelocityX = 10;
    private final int BALL_DIMENSION = 20;
    private int ballX;
    private int ballY;
    private int ballVelocityY;
    private Random random;
    private Timer timer;
    private List<String> input = new ArrayList<String>();
    public JLabel score = new JLabel();

    public PongGame(){
        setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setOpaque(true);
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        score.setFont(new Font("Ink Free", Font.BOLD, 75));
        score.setForeground(Color.WHITE);
        score.setText(player1Score + "    " + player2Score);
        add(score);
        StartGame();
    }

    public void StartGame(){
        random = new Random();
        timer = new Timer(16,this);
        timer.start();
        ballStart();
    }


    public void ballStart(){
        ballX = SCREEN_WIDTH/2-BALL_DIMENSION/2;
        ballY = SCREEN_HEIGHT/2-BALL_DIMENSION/2;
        int flip = random.nextInt(1,2);
        if(flip == 1)
            ballVelocityX = -ballVelocityX;
        ballVelocityY = random.nextInt(3,15);
        if(flip == 1)
            ballVelocityY = -ballVelocityY;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(PLAYER_1_POSITION_X,player1Position,PLAYER_DIMENSION_X,PLAYER_DIMENSION_Y);
        g.fillRect(PLAYER_2_POSITION_X,player2Position,PLAYER_DIMENSION_X,PLAYER_DIMENSION_Y);
        g.fillOval(ballX,ballY,BALL_DIMENSION,BALL_DIMENSION);
    }

    public void move(List<String> input){
        if (input.contains("1U"))
            player1Position -= PLAYER_VELOCITY;
        if (input.contains("1D"))
            player1Position += PLAYER_VELOCITY;
        if (input.contains("2U"))
            player2Position -= PLAYER_VELOCITY;
        if (input.contains("2D"))
            player2Position += PLAYER_VELOCITY;
    }

    public void ballMove(){
        ballX += ballVelocityX;
        ballY -= ballVelocityY;
    }

    public void checkCollision(){
        if((ballY+BALL_DIMENSION >= player1Position && ballY <= player1Position + PLAYER_DIMENSION_Y) && (ballX <= PLAYER_1_POSITION_X+PLAYER_DIMENSION_X && ballX >= PLAYER_1_POSITION_X)) {
            ballVelocityX = -ballVelocityX;
            ballX = PLAYER_1_POSITION_X+PLAYER_DIMENSION_X;
        }
        if((ballY+BALL_DIMENSION >= player2Position && ballY <= player2Position + PLAYER_DIMENSION_Y) && (ballX+BALL_DIMENSION > PLAYER_2_POSITION_X && ballX < PLAYER_2_POSITION_X + PLAYER_DIMENSION_X)) {
            ballVelocityX = -ballVelocityX;
            ballX = PLAYER_2_POSITION_X-BALL_DIMENSION;
        }
        if(ballY < 0) {
            ballVelocityY = -ballVelocityY;
            ballY = 0;
        }
        if(ballY+BALL_DIMENSION > SCREEN_HEIGHT) {
            ballVelocityY = -ballVelocityY;
            ballY = SCREEN_HEIGHT - BALL_DIMENSION;
        }
        if(ballX < -BALL_DIMENSION) {
            player2Score++;
            score.setText(player1Score + "    " + player2Score);
            ballStart();
        }
        if(ballX > SCREEN_WIDTH) {
            player1Score++;
            score.setText(player1Score + "    " + player2Score);
            ballStart();
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        ballMove();
        move(input);
        checkCollision();
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP)
                input.remove("2U");
            if(key == KeyEvent.VK_DOWN)
                input.remove("2D");
            if(key == KeyEvent.VK_W)
                input.remove("1U");
            if(key == KeyEvent.VK_S)
                input.remove("1D");
        }
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP && !(input.contains("2U")))
                input.add("2U");
            if(key == KeyEvent.VK_DOWN && !(input.contains("2D")))
                input.add("2D");
            if(key == KeyEvent.VK_W && !(input.contains("1U")))
                input.add("1U");
            if(key == KeyEvent.VK_S && !(input.contains("1D")))
                input.add("1D");
        }
    }
}
