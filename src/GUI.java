import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class GUI {
    GUI(int L){
        LAYER=L;
        BLOCK_NUMBER=L*BLOCK_GAP;
    }
    //Table initialize
    private final int TABLE_WIDTH = 300;
    private final int TABLE_HEIGHT = 400;
    //Racket initialize
    private final int RACKET_Y = 370;
    private final int RACKET_HEIGHT = 1;
    private final int RACKET_WIDTH = 60;
    private final int BALL_SIZE = 9;
    //Block initialize
    private final int BLOCK_GAP = 6;
    private int LAYER = 5;
    private int BLOCK_NUMBER = BLOCK_GAP*LAYER;
    private boolean[] BLOCK_ELIMINATE = new boolean[BLOCK_NUMBER];
    private final int BLOCK_WIDTH = TABLE_WIDTH / BLOCK_GAP;
    private final int BLOCK_HEIGHT = 15;
    private JFrame frame=new JFrame("Pinball game");
    Random r=new Random();
    private int ySpeed= r.nextInt(5)+5;
    private final int xyRate = r.nextInt(2)*2-1;
    private int xSpeed = (int)(ySpeed * xyRate * 0.7);
    private int ballX = r.nextInt(200) + 50;
    private int ballY = r.nextInt(10) + LAYER * BLOCK_HEIGHT;
    private int racketX = r.nextInt(200);
    private MyCanvas Printer = new MyCanvas();
    Timer timer;
    private int isLose = 0;
    private void CreateGUI(){
        Printer.setPreferredSize(new Dimension(TABLE_WIDTH , TABLE_HEIGHT));
        frame.add(Printer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyAdapter keyProcessor = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    if(racketX > 10) racketX -= 10;
                    else racketX = 0;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                    if(racketX < TABLE_WIDTH - RACKET_WIDTH - 10) racketX += 10;
                    else racketX = TABLE_WIDTH - RACKET_WIDTH;
                }
            }
        };
        frame.addKeyListener(keyProcessor);
        Printer.addKeyListener(keyProcessor);
        ActionListener MainTask = evt ->{
            if(ballX <= 0||ballX+BALL_SIZE >= TABLE_WIDTH) xSpeed=-xSpeed;
            else if((ballX + BALL_SIZE/2< racketX || ballX - BALL_SIZE/2> racketX + RACKET_WIDTH)&&ballY >= RACKET_Y - BALL_SIZE){
                timer.stop();
                isLose=1;
                Printer.repaint();
            }
            else if(ballY >= RACKET_Y - BALL_SIZE&&ballX + BALL_SIZE/2>= racketX&&ballX - BALL_SIZE/2<= racketX+ RACKET_WIDTH) ySpeed = -ySpeed;
            else if(ballY <= 0){
                timer.stop();
                isLose=2;
                Printer.repaint();
            }
            else{
                for(int i=0;i<BLOCK_NUMBER;i++){
                    if(BLOCK_ELIMINATE[i]) continue;
                    int ThisX = (i % BLOCK_GAP)*BLOCK_WIDTH + 1;
                    int ThisY = (i / BLOCK_GAP)*BLOCK_HEIGHT;
                    if(ballX + BALL_SIZE >= ThisX && ballX <= ThisX + BLOCK_WIDTH - 2 && ballY + BALL_SIZE >= ThisY && ballY <= ThisY + BLOCK_HEIGHT){
                        BLOCK_ELIMINATE[i] = true;
                        if(Math.abs(ThisX - ballX - BALL_SIZE/2) <= BALL_SIZE*0.7) xSpeed=-xSpeed;
                        else ySpeed=-ySpeed;
                    }
                }
            }
            ballX += xSpeed;
            ballY += ySpeed;
            Printer.repaint();
        };
        timer = new Timer(50,MainTask);
        timer.start();
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        GUI Demo = new GUI(5);
        JFrame JF = new JFrame("Difficulty selection");
        JF.setSize(215,460);
        JF.setVisible(true);
        JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel =new JPanel();
        JF.add(panel);
        Demo.SetComponents(panel,JF);
    }
    private void SetComponents(JPanel panel,JFrame JF){
        panel.setLayout(null);
        JLabel ChooseD = new JLabel("Select Difficulty");
        ChooseD.setBounds(50,10,180,20);
        panel.add(ChooseD);
        JButton Easy = new JButton("Easy");
        Easy.setBounds(20,40,160,110);
        panel.add(Easy);
        Easy.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JF.setVisible(false);
                new GUI(1).CreateGUI();
            }
        });
        JButton Normal = new JButton("Normal");
        Normal.setBounds(20,160,160,110);
        panel.add(Normal);
        Normal.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JF.setVisible(false);
                new GUI(3).CreateGUI();
            }
        });
        JButton Hard = new JButton("Hard");
        Hard.setBounds(20,280,160,110);
        panel.add(Hard);
        Hard.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JF.setVisible(false);
                new GUI(5).CreateGUI();
            }
        });
    }
    class MyCanvas extends Canvas{
        @Override
        public void paint(Graphics g) {
            if(isLose==1){
                g.setColor(new Color(255, 0, 0));
                g.setFont(new Font("Times",Font.BOLD,30));
                g.drawString("Game Over!",70,200);
            }
            else if(isLose==2){
                g.setColor(new Color(238, 0, 255));
                g.setFont(new Font("Times",Font.BOLD,30));
                g.drawString("You Win!!!",70,200);
            }
            else{
                g.setColor(new Color(0, 255, 217, 255));
                g.fillOval(ballX,ballY,BALL_SIZE,BALL_SIZE);
                g.setColor(new Color(0,0,0, 206));
                g.fillRect(racketX,RACKET_Y,RACKET_WIDTH,RACKET_HEIGHT);
                g.setColor(new Color(255, 100, 0));
                for(int i=0;i<BLOCK_NUMBER;i++) {
                    if(BLOCK_ELIMINATE[i]) continue;
                    g.fillRect((i % BLOCK_GAP)*BLOCK_WIDTH + 1, (i / BLOCK_GAP)*BLOCK_HEIGHT, BLOCK_WIDTH - 2, BLOCK_HEIGHT-1);
                }
            }
        }
    }
}
