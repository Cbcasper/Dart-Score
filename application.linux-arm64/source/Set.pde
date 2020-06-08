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
  
  void addThrow(Field field)
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
  
  void show()
  {
    textSize(30);
    textAlign(CENTER);
    fill(255);
    text(getWonLegs(firstPlayer), width / 3, 80);
    text(getWonLegs(secondPlayer), 2 * width / 3, 80);
    text("Legs", width / 2, 80);
  }
  
  Boolean finished()
  {
    if (getWonLegs(firstPlayer) > legs.size() / 2)
      winner = firstPlayer;
    else if (getWonLegs(secondPlayer) > legs.size() / 2)
      winner = secondPlayer;
    else
      return false;
    return true;
  }
  
  int getWonLegs(Player player)
  {
    int result = 0;
    for (Leg leg: legs)
      if (leg.winner == player)
        result++;
    return result;
  }
}
