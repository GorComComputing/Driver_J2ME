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

// Переменные переносимости
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

    // Установить частоту кадров (30 fps)
    frameDelay = 33;

    // Обнулить задержку ввода
    inputDelay = 0;
  }
  
  public void start() {
    // Установить холст как текущий экран
    display.setCurrent(this);

    // Инициализировать генератор случайных чисел
    rand = new Random();

    // Инициализация переменных
    gameOver = false;
    gameWin = false;
    numLives = 3;
    score = 0;
    carYSpeed = 0;
    finish = false;
    positionCar = 2;
    time = 0;
    highTime = 33;

    // Инициализация фонового изображения и спрайтов
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

    
    // Вывести логотип
    Graphics g = getGraphics();
    g.drawImage(logo, 0, 0, Graphics.TOP | Graphics.LEFT);
    // Вывести содержимое буфера на экран
    flushGraphics();

    while(1==1){
      int keyState = getKeyStates();
      if ((keyState & FIRE_PRESSED) != 0){
        // Запустить поток анимации
        sleeping = false;
        Thread t = new Thread(this);
        t.start();
        return;
      }
    }
  }
  
  public void stop() {
    // Остановить анимацию
    sleeping = true;
  }
  
  public void run() {
    Graphics g = getGraphics();
    
    // Основной игровой цикл
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
    // Проверить, перезапущена ли играа
    if (gameOver) {
      int keyState = getKeyStates();
      if ((keyState & FIRE_PRESSED) != 0) {
        // Старт новой игры
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
      // Игра окончена, нет необходимости обновления
      return;
    }

   // Проверить, перезапущена ли играа
    if (gameWin) {
      int keyState = getKeyStates();
      if ((keyState & FIRE_PRESSED) != 0) {
        // Старт новой игры
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

      // Игра окончена, нет необходимости обновления
      return;
    }

    // Обработать пользовательский ввод
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

      // Обнулить задержку ввода
      inputDelay = 0;
    }

    if(flagSprite.getY()<=150){
      flagSprite.move(0,carYSpeed);
    }
   
    // Обновить счеетчик расстояния и времени
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
    // Обновить спрайт разметки
       roadSprite.move(0,carYSpeed);
       if (roadSprite.getY() > -10)
         roadSprite.setPosition(roadSprite.getX(), -110);    

    // Обновить спрайты кустов
    for (int i = 0; i < 2; i++){
       treeSprite[i].move(0,carYSpeed);
       if (treeSprite[i].getY() > 120)
         treeSprite[i].setPosition(treeSprite[i].getX(), -20);

      // Проверить столкновение между спрайтами автомобиля и кустов
      if (carSprite.collidesWith(treeSprite[i], true)) {
        // Воспроизвести звук при столкновении
        AlertType.ERROR.playSound(display);

        // Проверить, не законченали игра
        if (--numLives == 0) {
          gameOver = true;
        } else {
          // Вернуть спрайт автомобиля на место
          carSprite.setPosition(90, 70);
          carYSpeed = 0;
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
        }

        // Нет необходимости обновлять спрайты автомобилей
        break;
      }
    }
    // Обновить спрайты машин
    for (int i = 0; i < 4; i++){
       carEnemySprite[i].move(0,carYSpeed+carEmemyYSpeed[i]);
       if (carEnemySprite[i].getY() > 400)
         carEnemySprite[i].setPosition(carEnemySprite[i].getX(), -20);   

      // Проверить столкновение между спрайтами автомобилей
      if (carSprite.collidesWith(carEnemySprite[i], true)) {
        // Воспроизвести звук при столкновении
        AlertType.ERROR.playSound(display);

        // Проверить, не законченали игра
        if (--numLives == 0) {
          gameOver = true;
        } else {
          // Вернуть спрайт автомобиля на место
          carSprite.setPosition(90, 70);
          carYSpeed = 0;
      carEnemySprite[0].setPosition(carEnemyXPosition1, carEnemyYPosition1);
      carEnemySprite[1].setPosition(carEnemyXPosition2, carEnemyYPosition2);
      carEnemySprite[2].setPosition(carEnemyXPosition3, carEnemyYPosition3);
      carEnemySprite[3].setPosition(carEnemyXPosition4, carEnemyYPosition4);
        }

        // Нет необходимости обновлять спрайты автомобилей
        break;
      }
    }
  }

  private void draw(Graphics g) {
    // Вывести фоновое изображение
    g.drawImage(background, 0, 0, Graphics.TOP | Graphics.LEFT);
    
    // Нарисовать спрайт разметки
    roadSprite.paint(g);


    // Нарисовать спрайт автомобиля
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

    // Нарисовать спрайт старта
    flagSprite.paint(g);

    // Нарисовать спрайты автомобилей
    for (int i = 0; i < 4; i++)
      carEnemySprite[i].paint(g);

    // Нарисовать спрайты кустов
    for (int i = 0; i < 2; i++)
      treeSprite[i].paint(g);

    // Показать оставшиеся жизни
    for (int i = 0; i < numLives; i++)
      g.drawImage(liveCar, liveCarXPosition - ((i + 1) * 8), liveCarYPosition, Graphics.TOP | Graphics.LEFT);

      g.setColor(255, 255, 255); // белый
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString(" " + score, 0, scoreYPosition, Graphics.TOP | Graphics.LEFT);

      g.setColor(255, 255, 255); // белый
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString(" " + time, 0, timeYPosition, Graphics.TOP | Graphics.LEFT);
    
    if (gameWin) {
      // Вывести сообщение о конце игры и счет
      g.setColor(255, 0, 0); // красный
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString("ФИНИШ!", textXPosition, textYPosition, Graphics.TOP | Graphics.HCENTER);
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
      g.drawString("Время " + time + " сек.", textXPosition, textYPosition+30, Graphics.TOP | Graphics.HCENTER);
      if (highTime > time)//30);
        highTime = time; // 30;
      g.drawString("Лучшее" + highTime + " сек.", textXPosition, textYPosition+40, Graphics.TOP | Graphics.HCENTER);
    }

    if (gameOver) {
      // Вывести сообщение о конце игры и счет
      g.setColor(255, 0, 0); // красный
      g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
      g.drawString("GAME OVER", textXPosition, textYPosition, Graphics.TOP | Graphics.HCENTER);
    }

    // Вывести содержимое буфера на экран
    flushGraphics();
  }


  private void checkBounds(Sprite sprite, boolean wrap) {
    Random   rand1;
    
    // Инициализировать генератор случайных чисел
 

    // Переместить/остановить спрайт
    if (wrap) {
    
      // Переместить спрайт в исходное положение
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
      // Остановить спрайт у края экрана
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

