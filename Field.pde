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
  color fieldColor;
  PVector upperLeft;
  Ring ring;
  int value;

  Field(color fieldColor, PVector upperLeft, Ring ring, int value)
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
  
  void show()
  {
    stroke(0);
    fill(fieldColor);
    float angle = upperLeft.heading();
    arc(width / 2, height / 2, upperLeft.mag(), upperLeft.mag(), angle, angle + (TWO_PI / 20), PIE);
  }
  
  int getScore()
  {
    return value * ring.multiplier;
  }
}
