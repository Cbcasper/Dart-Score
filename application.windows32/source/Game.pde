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
  
  void update(Field field)
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
  
  void show()
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
  
  void showSets()
  {
    textSize(30);
    textAlign(CENTER);
    fill(255);
    text(getWonSets(firstPlayer), width / 3, 40);
    text(getWonSets(secondPlayer), 2 * width / 3, 40);
    text("Sets", width / 2, 40);
  }
  
  void showIntro()
  {
    textSize(30);
    textAlign(CORNER);
    fill(255);
    text("Number of sets: " + str(numberOf[0]), 5, 40);
    text("Number of legs: " + str(numberOf[1]), 5, 80);
  }
  
  int getWonSets(Player player)
  {
    int result = 0;
    for (Set set: sets)
      if (set.winner == player)
        result++;
    return result;
  }
  
  void finishGame()
  {
    if (getWonSets(firstPlayer) > getWonSets(secondPlayer))
      firstPlayer.win();
    else
      secondPlayer.win();
  }
  
  void enter()
  {
    currentEnterValue++;
    if (currentEnterValue == 4)
      startGame();
  }
  
  void addCharacter(char character)
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
  
  void startGame()
  {
    sets = new ArrayList<Set>();
    for (int i = 0; i < numberOf[0]; i++)
      sets.add(new Set(numberOf[1], firstPlayer, secondPlayer));
    currentSet = sets.get(0);
    started = true;
  }
}
