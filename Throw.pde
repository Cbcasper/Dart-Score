class Throw
{
  Player player;
  ArrayList<Field> throwValues;
  
  Throw(Player player)
  {
    throwValues = new ArrayList<Field>();
    this.player = player;
  }
  
  Boolean full()
  {
    return throwValues.size() == 3;
  }
  
  void add(Field field)
  {
    throwValues.add(field);
  }
}
