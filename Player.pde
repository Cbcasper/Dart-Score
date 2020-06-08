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
  
  color displayColor;
  color turnColor;
  color backGroundColor;
  
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
  
  void show()
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
  
  void showParameters(int x, int y)
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
  
  void win()
  {
    this.flashing = true;
    this.displayColor = backGroundColor;
  }
  
  void resetScore()
  {
    this.score = 501;
  }
  
  void addCharacter(char character)
  {
    if (!nameStarted)
    {
      name = str(character);
      nameStarted = true;
    }
    else
      name += character;
  }
  
  void notTurn()
  {
    this.displayColor = backGroundColor;
  }
  
  void turn()
  {
    this.displayColor = turnColor;
  }
}
