class Board
{
  Field[] fields;
  HashMap<Integer, Ring> ringMap;
  IntList ringDistances;
  IntList fieldValues;
  color green = color(0, 150, 0);
  color red = color(170, 0, 0);

  
  Board()
  {
    fields = new Field[80];
    
    ringMap = new HashMap<Integer, Ring>();
    ringMap.put(0, Ring.Inner);
    ringMap.put(1, Ring.Triple);
    ringMap.put(2, Ring.Outer);
    ringMap.put(3, Ring.Double);
    
    
    int adjustedWidth = width - 200;
    ringDistances = new IntList(adjustedWidth * 0.025, adjustedWidth * 0.05, adjustedWidth * 0.25, adjustedWidth * 0.28, adjustedWidth * 0.465, adjustedWidth * 0.5);
    
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
  
  void showNumbers()
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
  
  void showBullsEye()
  {
    fill(green);
    stroke(0);
    ellipse(width / 2, height / 2, ringDistances.get(1), ringDistances.get(1));
    fill(red);
    stroke(0);
    ellipse(width / 2, height / 2, ringDistances.get(0), ringDistances.get(0));
  }
  
  void show()
  {
    showNumbers();
    for(Field field: fields)
      field.show();
    showBullsEye();
  }
  
  Field getClickedField(int ring, float heading)
  {
    if (ring == 0)
      return new Field(50);
    else if (ring == 1)
      return new Field(25);
    else if (ring == 6)
      return new Field(0);
    ring = int(map(ring, 2, 5, 3, 0));
    return fields[(ring * 20) + ((floor(heading) + 5) % 20)];
  }
  
  color getColor(int i, int j)
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
  
  int getRing(float distance)
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
