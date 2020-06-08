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
  
  void addThrow(Field field)
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
  
  void switchPlayer()
  {
    currentPlayer.notTurn();
    if (currentPlayer == firstPlayer)
      currentPlayer = secondPlayer;
    else
      currentPlayer = firstPlayer;
    currentPlayer.turn();
  }
  
  Boolean finished()
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
  
  void correctStart(Player previousFirstPlayer)
  {
    currentPlayer = (previousFirstPlayer == firstPlayer ? secondPlayer : firstPlayer);
    currentPlayer.turn();
    previousFirstPlayer.notTurn();
  }
}
