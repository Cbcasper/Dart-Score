import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Dart_Score extends PApplet {

Game game;
Board board; //<>//
Boolean onRules = true;
String rules = "Welcome to Dart Score!\nPress enter to continue to the next screen, \nwhere you can enter the names of the players\n and the number of sets and legs.\nTo enter the names, type the first name, \npress enter and type the next name. \nGo to the number of sets and legs with enter, \nand adjust them by using + and -. \nPress enter after setting the sets to go to the legs \nand press enter to start the game.";

public void setup()
{
  
  background(0, 80, 0);
  
  game = new Game();
  board = new Board();
}

public void keyPressed() 
{
  if (key == ENTER || key == RETURN)
  {
    if (onRules)
      onRules = false;
    else
      game.enter();
  }
  else if (keyCode != SHIFT)
    game.addCharacter(key);
}

public void mousePressed()
{
  if (!game.started)
    return;
  float distance = dist(mouseX, mouseY, width / 2, height / 2);
  int ring = board.getRing(distance);
  PVector angleVector = new PVector(width / 2 - mouseX, height / 2 - mouseY);
  float heading = map(angleVector.heading(), -PI, PI, 0, 20) + 0.5f;
  game.update(board.getClickedField(ring, heading));
}

public void draw()
{
  background(0, 80, 0);
  if (onRules)
  {
    textSize(50);
    textAlign(CENTER);
    text(rules, width / 2, 50);
  }
  else
  {
    game.show();
    board.show();
  }
}
class Board
{
  Field[] fields;
  HashMap<Integer, Ring> ringMap;
  IntList ringDistances;
  IntList fieldValues;
  int green = color(0, 150, 0);
  int red = color(170, 0, 0);

  
  Board()
  {
    fields = new Field[80];
    
    ringMap = new HashMap<Integer, Ring>();
    ringMap.put(0, Ring.Inner);
    ringMap.put(1, Ring.Triple);
    ringMap.put(2, Ring.Outer);
    ringMap.put(3, Ring.Double);
    
    
    int adjustedWidth = width - 200;
    ringDistances = new IntList(adjustedWidth * 0.025f, adjustedWidth * 0.05f, adjustedWidth * 0.25f, adjustedWidth * 0.28f, adjustedWidth * 0.465f, adjustedWidth * 0.5f);
    
    fieldValues = new IntList(20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5);
    
    float currentAngle = -HALF_PI - (TWO_PI / 40);
    int index = 0;
    for (int j = 3; j >= 0; j--)
      for (int i = 0; i < 20; i++)
      {
        PVector angleVector = PVector.fromAngle(currentAngle);
        Field field = new Field(getColor(i, j), PVector.mult(angleVector, ringDistances.get(j + 2)), ringMap.get(j), fieldValues.get(i));
        fields[index] = field;
        currentAngle += TWO_PI / 20;
        index++;
      }
  }
  
  public void showNumbers()
  {
    fill(255 );
    textSize(30);
    textAlign(CENTER);
    float angle = -HALF_PI;
    for(int i = 0; i < 20; i++)
    {
      PVector angleVector = PVector.fromAngle(angle);
      angleVector.mult(ringDistances.get(ringDistances.size() - 1) / 2 + 25);
      text(fieldValues.get(i), width / 2 + angleVector.x, height / 2 + angleVector.y + 10);
      angle += TWO_PI / 20;
    }
  }
  
  public void showBullsEye()
  {
    fill(green);
    stroke(0);
    ellipse(width / 2, height / 2, ringDistances.get(1), ringDistances.get(1));
    fill(red);
    stroke(0);
    ellipse(width / 2, height / 2, ringDistances.get(0), ringDistances.get(0));
  }
  
  public void show()
  {
    showNumbers();
    for(Field field: fields)
      field.show();
    showBullsEye();
  }
  
  public Field getClickedField(int ring, float heading)
  {
    if (ring == 0)
      return new Field(50);
    else if (ring == 1)
      return new Field(25);
    else if (ring == 6)
      return new Field(0);
    ring = PApplet.parseInt(map(ring, 2, 5, 3, 0));
    return fields[(ring * 20) + ((floor(heading) + 5) % 20)];
  }
  
  public int getColor(int i, int j)
  {
    if (j % 2 == 0)
    {
      IntList colors = new IntList(color(0), color(255));
      return colors.get(i % 2);
    }
    else if (j % 2 == 1)
    {
      IntList colors = new IntList(red, green);
      return colors.get(i % 2);
    }
    return color(0);
  }
  
  public int getRing(float distance)
  {
    int ring = 6;
    for (int i = 0; i < 6; i++)
    if (distance < board.ringDistances.get(i) / 2)
    {
      ring = i;
      break;
    }
    return ring;
  }
}
enum Ring
{
  Inner(1), Triple(3), Outer(1), Double(2);
  int multiplier;
  
  private Ring(int multiplier)
  {
    this.multiplier = multiplier;
  }
}

class Field
{
  int fieldColor;
  PVector upperLeft;
  Ring ring;
  int value;

  Field(int fieldColor, PVector upperLeft, Ring ring, int value)
  {
    this.fieldColor = fieldColor;
    this.upperLeft = upperLeft;
    this.ring = ring;
    this.value = value;
  }
  
  Field(int value)
  {
    this.fieldColor = color(0);
    this.upperLeft = new PVector(0, 0);
    this.ring = Ring.Inner;
    this.value = value;
  }
  
  public void show()
  {
    stroke(0);
    fill(fieldColor);
    float angle = upperLeft.heading();
    arc(width / 2, height / 2, upperLeft.mag(), upperLeft.mag(), angle, angle + (TWO_PI / 20), PIE);
  }
  
  public int getScore()
  {
    return value * ring.multiplier;
  }
}
class Game
{
  Player firstPlayer;
  Player secondPlayer;
  ArrayList<Set> sets;
  Set currentSet;
  int currentEnterValue = 0;
  int[] numberOf = {1, 1};
  Boolean started = false;
  
  Game()
  {
    firstPlayer = new Player("Player 1", Order.First);
    secondPlayer = new Player("Player 2", Order.Second);
  }
  
  public void update(Field field)
  {
    currentSet.addThrow(field);
    if (currentSet.finished())
      if (sets.indexOf(currentSet) + 1 < sets.size())
      {
        currentSet = sets.get(sets.indexOf(currentSet) + 1);
        if (getWonSets(firstPlayer) > sets.size() / 2 || getWonSets(secondPlayer) > sets.size() / 2)
          finishGame();
      }
      else
        finishGame();
  }
  
  public void show()
  {
    firstPlayer.show();
    secondPlayer.show();
    if (started)
    {
      currentSet.show();
      showSets();
    }
    else
      showIntro();
  }
  
  public void showSets()
  {
    textSize(30);
    textAlign(CENTER);
    fill(255);
    text(getWonSets(firstPlayer), width / 3, 40);
    text(getWonSets(secondPlayer), 2 * width / 3, 40);
    text("Sets", width / 2, 40);
  }
  
  public void showIntro()
  {
    textSize(30);
    textAlign(CORNER);
    fill(255);
    text("Number of sets: " + str(numberOf[0]), 5, 40);
    text("Number of legs: " + str(numberOf[1]), 5, 80);
  }
  
  public int getWonSets(Player player)
  {
    int result = 0;
    for (Set set: sets)
      if (set.winner == player)
        result++;
    return result;
  }
  
  public void finishGame()
  {
    if (getWonSets(firstPlayer) > getWonSets(secondPlayer))
      firstPlayer.win();
    else
      secondPlayer.win();
  }
  
  public void enter()
  {
    currentEnterValue++;
    if (currentEnterValue == 4)
      startGame();
  }
  
  public void addCharacter(char character)
  {
    if (currentEnterValue > 4)
      return;
    switch (currentEnterValue)
    {
      case 0: firstPlayer.addCharacter(character);                break;
      case 1: secondPlayer.addCharacter(character);               break;
      case 2:
      case 3: 
        if (character == '+')
          numberOf[currentEnterValue - 2] += 2;
        else if (character == '-')
          numberOf[currentEnterValue - 2] -= 2;
    }
  }
  
  public void startGame()
  {
    sets = new ArrayList<Set>();
    for (int i = 0; i < numberOf[0]; i++)
      sets.add(new Set(numberOf[1], firstPlayer, secondPlayer));
    currentSet = sets.get(0);
    started = true;
  }
}
class Leg
{
  ArrayList<Throw> throwArray;
  Player firstPlayer;
  Player secondPlayer;
  
  Player winner;
  
  Player currentPlayer;
  Throw currentThrow;
  
  Leg(Player firstPlayer, Player secondPlayer)
  {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
    this.currentPlayer = firstPlayer;
    throwArray = new ArrayList<Throw>();
    currentThrow = new Throw(currentPlayer);
  }
  
  public void addThrow(Field field)
  {
    Boolean bust = false;
    int subtracted = currentPlayer.score - field.getScore();
    if (subtracted < 0 || subtracted == 1)
    {
      bust = true;
      while (!currentThrow.full())
        currentThrow.add(new Field(0));
    }
    
    if (!bust)
    {
      currentThrow.add(field);
      currentPlayer.score -= field.getScore();
    }
    if (currentThrow.full())
    {
      throwArray.add(currentThrow);
      currentThrow = new Throw(currentPlayer);
      switchPlayer();
    }
  }
  
  public void switchPlayer()
  {
    currentPlayer.notTurn();
    if (currentPlayer == firstPlayer)
      currentPlayer = secondPlayer;
    else
      currentPlayer = firstPlayer;
    currentPlayer.turn();
  }
  
  public Boolean finished()
  {
    if (firstPlayer.score == 0)
    {
      winner = firstPlayer;
      return true;
    }
    else if (secondPlayer.score == 0)
    {
      winner = secondPlayer;
      return true;
    }
    return false;
  }
  
  public void correctStart(Player previousFirstPlayer)
  {
    currentPlayer = (previousFirstPlayer == firstPlayer ? secondPlayer : firstPlayer);
    currentPlayer.turn();
    previousFirstPlayer.notTurn();
  }
}
enum Order
{
  First, Second
}

class Player
{
  String name;
  int score;
  Order order;
  Boolean nameStarted = false;
  
  int displayColor;
  int turnColor;
  int backGroundColor;
  
  Boolean flashing;
  
  Player(String name, Order order)
  {
    this.name = name;
    this.score = 501;
    this.order = order;
    
    this.turnColor = color(0, 0, 255);
    this.backGroundColor = color(0, 80, 0);
    if (order == Order.First)
      this.displayColor = turnColor;
    else
      this.displayColor = backGroundColor;
      
    flashing = false;
  }
  
  public void show()
  {
    switch (order)
    {
      case First:
        showParameters(100, 300);
        break;
      case Second:
        showParameters(width - 100, 300);
        break;
    }
  }
  
  public void showParameters(int x, int y)
  {
    textSize(30);
    textAlign(CENTER);
    if (flashing)
      if (frameCount % 10 < 5)
        fill(0);
      else
        fill(255);
    else
      fill(255);
    text(name + ": \n" + str(score), x, y);
    stroke(displayColor);
    fill(displayColor);
    ellipse(x + str(score).length() * 20, y + 35, 20, 20);
  }
  
  public void win()
  {
    this.flashing = true;
    this.displayColor = backGroundColor;
  }
  
  public void resetScore()
  {
    this.score = 501;
  }
  
  public void addCharacter(char character)
  {
    if (!nameStarted)
    {
      name = str(character);
      nameStarted = true;
    }
    else
      name += character;
  }
  
  public void notTurn()
  {
    this.displayColor = backGroundColor;
  }
  
  public void turn()
  {
    this.displayColor = turnColor;
  }
}
class Set
{
  ArrayList<Leg> legs;
  Player firstPlayer;
  Player secondPlayer;
  
  Player winner;
  Leg currentLeg;
  
  Set(int numberOfLegs, Player firstPlayer, Player secondPlayer)
  {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
    legs = new ArrayList<Leg>();
    for (int i = 0; i < numberOfLegs; i++)
      legs.add(new Leg(firstPlayer, secondPlayer));
    currentLeg = legs.get(0);
  }
  
  public void addThrow(Field field)
  {
    currentLeg.addThrow(field);
    if (currentLeg.finished() && legs.indexOf(currentLeg) + 1 < legs.size())
    {
      Player previousFirstPlayer = currentLeg.firstPlayer;
      currentLeg = legs.get(legs.indexOf(currentLeg) + 1);
      currentLeg.correctStart(previousFirstPlayer);
      firstPlayer.resetScore();
      secondPlayer.resetScore();
    }
  }
  
  public void show()
  {
    textSize(30);
    textAlign(CENTER);
    fill(255);
    text(getWonLegs(firstPlayer), width / 3, 80);
    text(getWonLegs(secondPlayer), 2 * width / 3, 80);
    text("Legs", width / 2, 80);
  }
  
  public Boolean finished()
  {
    if (getWonLegs(firstPlayer) > legs.size() / 2)
      winner = firstPlayer;
    else if (getWonLegs(secondPlayer) > legs.size() / 2)
      winner = secondPlayer;
    else
      return false;
    return true;
  }
  
  public int getWonLegs(Player player)
  {
    int result = 0;
    for (Leg leg: legs)
      if (leg.winner == player)
        result++;
    return result;
  }
}
class Throw
{
  Player player;
  ArrayList<Field> throwValues;
  
  Throw(Player player)
  {
    throwValues = new ArrayList<Field>();
    this.player = player;
  }
  
  public Boolean full()
  {
    return throwValues.size() == 3;
  }
  
  public void add(Field field)
  {
    throwValues.add(field);
  }
}
  public void settings() {  size(1680, 1050); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Dart_Score" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
