Game game;
Board board; //<>//
Boolean onRules = true;
String rules = "Welcome to Dart Score!\nPress enter to continue to the next screen, \nwhere you can enter the names of the players\n and the number of sets and legs.\nTo enter the names, type the first name, \npress enter and type the next name. \nGo to the number of sets and legs with enter, \nand adjust them by using + and -. \nPress enter after setting the sets to go to the legs \nand press enter to start the game.";

void setup()
{
  size(1680, 1050);
  background(0, 80, 0);
  
  game = new Game();
  board = new Board();
}

void keyPressed() 
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

void mousePressed()
{
  if (!game.started)
    return;
  float distance = dist(mouseX, mouseY, width / 2, height / 2);
  int ring = board.getRing(distance);
  PVector angleVector = new PVector(width / 2 - mouseX, height / 2 - mouseY);
  float heading = map(angleVector.heading(), -PI, PI, 0, 20) + 0.5;
  game.update(board.getClickedField(ring, heading));
}

void draw()
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
