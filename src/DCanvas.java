import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;
import java.io.*;

public class DCanvas extends GameCanvas implements Runnable {
  private Display  display;
  private boolean  sleeping;
  private boolean  finish;
  private long     frameDelay;
  private float    inputDelay;
  private Random   rand;
  private Image    background;
  private Image    logo;
  private Image    carStop;
  private Image    carGo;
  private Image    carLeft;
  private Image    carRight;
  private Image    liveCar;
  private Sprite   carSprite;
  private Sprite[]   carEnemySprite = new Sprite[4];
  private Sprite[] treeSprite = new Sprite[2];
  private Sprite   roadSprite;
  private Sprite   flagSprite;
  private Sprite   finishSprite; 
  private int[]    carEmemyYSpeed = new int[4];
  private boolean  gameOver;
  private boolean  gameWin;
  private int      highTime,time,positionCar,score,carYSpeed,numLives;

// ���������� �������������
   private int     flagXPosition = 100; 
   private int     flagYPosition = 40;
   private int     roadXPosition = 74;
   private int     carXPosition = 115;
   private int     carYPosition = 70;
   private int     carEnemyXPosition1 = 25;
   private int     carEnemyYPosition1 = 0;
   private int     carEnemyXPosition2 = 55;
   private int     carEnemyYPosition2 = 70;
   private int     carEnemyXPosition3 = 85;
   private int     carEnemyYPosition3 = 200;
   private int     carEnemyXPosition4 = 115;
   private int     carEnemyYPosition4 = 300;
   private int     treeXPosition1 = 0;
   private int     treeYPosition1 = 10;
   private int     treeXPosition2 = 140;
   private int     treeYPosition2 = 70;
   private int     liveCarXPosition = 160;
   private int     liveCarYPosition = 90;
   private int     timeYPosition = 80;
   private int     scoreYPosition = 90;
   private int     textXPosition = 80;
   private int     textYPosition = 40;
   private int     centerScreen = 55;

  public DCanvas(Display d) {
    super(true);
    display = d;

    // ���������� ������� ������ (30 fps)
    frameDelay = 33;

    // �������� �������� �����
    inputDelay = 0;
  }
  
  public void start() {
    // ���������� ����� ��� ������� �����
    display.setCurrent(this);

    // ���������������� ��������� ��������� �����
    rand = new Random();

    // ������������� ����������
    gameOver = false;
    gameWin = false;
    numLives = 3;
    score = 0;
    carYSpeed = 0;
    finish = false;
    positionCar = 2;
    time = 0;
    highTime = 33;

    // ������������� �������� ����������� � ��������
    try {
      background = Image.createImage("/Land.png");
      logo = Image.createImage("/Logo.png");
      carStop = Image.createImage("/CarStop.png");
      carGo = Image.createImage("/Car.png");
      carRight = Image.createImage("/CarRight.png");
      carLeft = Image.createImage("/CarLeft.png");
      liveCar = Image.createImage("/LiveCar.png");

      flagSprite = new Sprite(Image.createImage("/Flag.png"));
      flagSprite.setPosition(flagXPosition, flagYPosition);      

      roadSprite = new Sprite(Image.createImage("/Road.png"));
      roadSprite.setPosition(roadXPosition, -110);

      carSprite = new Sprite(Image.createImage("/Car.png"));
      carSprite.setPosition(carXPosition, carYPosition);

      carEnemySprite[0] = new Sprite(Image.createImage("/CarEnemy1.png"));
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEmemyYSpeed[0] = 2;
      carEnemySprite[1] = new Sprite(Image.createImage("/CarEnemy1.png"));
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEmemyYSpeed[1] = 3;
      carEnemySprite[2] = new Sprite(Image.createImage("/CarEnemy2.png"));
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEmemyYSpeed[2] = -3;
      carEnemySprite[3] = new Sprite(Image.createImage("/CarEnemy2.png"));
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
      carEmemyYSpeed[3] = -2;

      treeSprite[0] = new Sprite(Image.createImage("/Tree.png"));
      treeSprite[0].setPosition(treeXPosition1, treeYPosition1);

      treeSprite[1] = new Sprite(Image.createImage("/Tree.png"));
      treeSprite[1].setPosition(treeXPosition2, treeYPosition2);
  
    }
    catch (IOException e) {
      System.err.println("Failed loading images!");
    }

    
    // ������� �������
    Graphics g = getGraphics();
    g.drawImage(logo, 0, 0, Graphics.TOP | Graphics.LEFT);
    // ������� ���������� ������ �� �����
    flushGraphics();

    while(1==1){
      int keyState = getKeyStates();
      if ((keyState & FIRE_PRESSED) != 0){
        // ��������� ����� ��������
        sleeping = false;
        Thread t = new Thread(this);
        t.start();
        return;
      }
    }
  }
  
  public void stop() {
    // ���������� ��������
    sleeping = true;
  }
  
  public void run() {
    Graphics g = getGraphics();
    
    // �������� ������� ����
    while (!sleeping) {
      update();
      draw(g);
      try {
        Thread.sleep(frameDelay);
      }
      catch (InterruptedException ie) {}             
    }
      
  }

  private void update() {
    // ���������, ������������ �� �����
    if (gameOver) {
      int keyState = getKeyStates();
      if ((keyState & FIRE_PRESSED) != 0) {
        // ����� ����� ����
        carSprite.setPosition(carXPosition, carYPosition);
        gameOver = false;
        score = 0;
        numLives = 3;
        carYSpeed = 0;
        time = 0;
        finish = false;
      flagSprite.setPosition(flagXPosition, flagYPosition);  
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
      }
      // ���� ��������, ��� ������������� ����������
      return;
    }

   // ���������, ������������ �� �����
    if (gameWin) {
      int keyState = getKeyStates();
      if ((keyState & FIRE_PRESSED) != 0) {
        // ����� ����� ����
        carSprite.setPosition(carXPosition, carYPosition);
        gameWin = false;
        score = 0;
        numLives = 3;
        carYSpeed = 0;
        finish = false;
        time = 0;
      flagSprite.setPosition(flagXPosition, flagYPosition);  
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
      }

      // ���� ��������, ��� ������������� ����������
      return;
    }

    // ���������� ���������������� ����
    if (++inputDelay > 0.01) {
      int keyState = getKeyStates();
      if ((keyState & LEFT_PRESSED) != 0) {
        carSprite.move(-carYSpeed, 0);
        if (carYSpeed != 0)
        positionCar = 4;       
      }
      else if ((keyState & RIGHT_PRESSED) != 0) {
        carSprite.move(carYSpeed, 0);
        if (carYSpeed != 0)
        positionCar = 6;    
      }
      if ((keyState & UP_PRESSED) != 0) {
        carSprite.move(0, -6);
        if (carYSpeed<10)
        carYSpeed++;
        positionCar = 2;
      }
      else if ((keyState & DOWN_PRESSED) != 0) {
        carSprite.move(0, 6);
        if (carYSpeed>0)
        carYSpeed--;
        positionCar = 8;
      }
      checkBounds(carSprite, false);

      // �������� �������� �����
      inputDelay = 0;
    }

    if(flagSprite.getY()<=150){
      flagSprite.move(0,carYSpeed);
    }
   
    // �������� �������� ���������� � �������
    score = score+carYSpeed;
    time++;
    
    if (finish==false){
    if (score >= 4900){
      flagSprite.setPosition(flagXPosition,-100);
      finish = true; } 
    }
    if (score > 5000){
     gameWin = true;
    }
    // �������� ������ ��������
       roadSprite.move(0,carYSpeed);
       if (roadSprite.getY() > -10)
         roadSprite.setPosition(roadSprite.getX(), -110);    

    // �������� ������� ������
    for (int i = 0; i < 2; i++){
       treeSprite[i].move(0,carYSpeed);
       if (treeSprite[i].getY() > 120)
         treeSprite[i].setPosition(treeSprite[i].getX(), -20);

      // ��������� ������������ ����� ��������� ���������� � ������
      if (carSprite.collidesWith(treeSprite[i], true)) {
        // ������������� ���� ��� ������������
        AlertType.ERROR.playSound(display);

        // ���������, �� ����������� ����
        if (--numLives == 0) {
          gameOver = true;
        } else {
          // ������� ������ ���������� �� �����
          carSprite.setPosition(90, 70);
          carYSpeed = 0;
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
        }

        // ��� ������������� ��������� ������� �����������
        break;
      }
    }
    // �������� ������� �����
    for (int i = 0; i < 4; i++){
       carEnemySprite[i].move(0,carYSpeed+carEmemyYSpeed[i]);
       if (carEnemySprite[i].getY() > 400)
         carEnemySprite[i].setPosition(carEnemySprite[i].getX(), -20);   

      // ��������� ������������ ����� ��������� �����������
      if (carSprite.collidesWith(carEnemySprite[i], true)) {
        // ������������� ���� ��� ������������
        AlertType.ERROR.playSound(display);

        // ���������, �� ����������� ����
        if (--numLives == 0) {
          gameOver = true;
        } else {
          // ������� ������ ���������� �� �����
          carSprite.setPosition(90, 70);
          carYSpeed = 0;
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
        }

        // ��� ������������� ��������� ������� �����������
        break;
      }
    }
  }

  private void draw(Graphics g) {
    // ������� ������� �����������
    g.drawImage(background, 0, 0, Graphics.TOP | Graphics.LEFT);
    
    // ���������� ������ ��������
    roadSprite.paint(g);


    // ���������� ������ ����������
    if (positionCar == 2){
        carSprite.setImage(carGo,20,40);    
        carSprite.paint(g);
        positionCar = 2;
     }
    if (positionCar == 8){
        carSprite.setImage(carStop,20,40);    
        carSprite.paint(g);
        positionCar = 2;
     }
    if (positionCar == 4){
        carSprite.setImage(carLeft,20,40);    
        carSprite.paint(g);
        positionCar = 2;
     }
    if (positionCar == 6){
        carSprite.setImage(carRight,20,40);    
        carSprite.paint(g);
        positionCar = 2;
     }

    // ���������� ������ ������
    flagSprite.paint(g);

    // ���������� ������� �����������
    for (int i = 0; i < 4; i++)
      carEnemySprite[i].paint(g);

    // ���������� ������� ������
    for (int i = 0; i < 2; i++)
      treeSprite[i].paint(g);

    // �������� ���������� �����
    for (int i = 0; i < numLives; i++)
      g.drawImage(liveCar, liveCarXPosition - ((i + 1) * 8), liveCarYPosition, Graphics.TOP | Graphics.LEFT);

      g.setColor(255, 255, 255); // �����
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString(" " + score, 0, scoreYPosition, Graphics.TOP | Graphics.LEFT);

      g.setColor(255, 255, 255); // �����
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString(" " + time, 0, timeYPosition, Graphics.TOP | Graphics.LEFT);
    
    if (gameWin) {
      // ������� ��������� � ����� ���� � ����
      g.setColor(255, 0, 0); // �������
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString("�����!", textXPosition, textYPosition, Graphics.TOP | Graphics.HCENTER);
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
      g.drawString("����� " + time + " ���.", textXPosition, textYPosition+30, Graphics.TOP | Graphics.HCENTER);
      if (highTime > time)//30);
        highTime = time; // 30;
      g.drawString("������" + highTime + " ���.", textXPosition, textYPosition+40, Graphics.TOP | Graphics.HCENTER);
    }

    if (gameOver) {
      // ������� ��������� � ����� ���� � ����
      g.setColor(255, 0, 0); // �������
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString("GAME OVER", textXPosition, textYPosition, Graphics.TOP | Graphics.HCENTER);
    }

    // ������� ���������� ������ �� �����
    flushGraphics();
  }


  private void checkBounds(Sprite sprite, boolean wrap) {
    Random   rand1;
    
    // ���������������� ��������� ��������� �����
 

    // �����������/���������� ������
    if (wrap) {
    
      // ����������� ������ � �������� ���������
      if (sprite.getX() < -sprite.getWidth())
        sprite.setPosition(getWidth(), sprite.getY());
      else if (sprite.getX() > getWidth())
        sprite.setPosition(-sprite.getWidth(), sprite.getY());
      if (sprite.getY() < -sprite.getHeight())
        sprite.setPosition(sprite.getX(), getHeight());
      else if (sprite.getY() > getHeight())
        sprite.setPosition(sprite.getX(), -sprite.getHeight());
    }
    else {
      // ���������� ������ � ���� ������
      if (sprite.getX() < 0)
        sprite.setPosition(0, sprite.getY());
      else if (sprite.getX() > (getWidth() - sprite.getWidth()))
        sprite.setPosition(getWidth() - sprite.getWidth(), sprite.getY());
      if (sprite.getY() < centerScreen)
        sprite.setPosition(sprite.getX(), centerScreen);
      else if (sprite.getY() > (getHeight() - sprite.getHeight()))
        sprite.setPosition(sprite.getX(), getHeight() - sprite.getHeight());
    }
  }
}

