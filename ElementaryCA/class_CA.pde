class CA {
  
  /* The code within this class was mostly written by Daniel Shiffman (2012).
  
       Source:
       https://natureofcode.com/book/chapter-7-cellular-automata/
     
     For the sake of simplicity, any code within this class that is original work
     will be identified with comments (The project author will be referred to
     as 'MON' where appropriate). */
  
  
  boolean[] cells;  // *Shiffman uses arrays of integers to store cell-states. ElementaryCA uses boolean arrays.
  
  int cellSize;
  int generation = 1;
  
  int detailPaneH;
  
  CA(int size) {
    
    cellSize = size;
    cells = new boolean[width/cellSize];
  
  }

  
  // Creating a new generation
  void computeNextGen() {
    
    boolean[] nextgen = new boolean[cells.length];
    
    for(int i = 0; i < cells.length; i++) {
      
      boolean left;
      boolean sub;
      boolean right;
      
      
      /* The wrap-around functionality of this method was created by MON. This causes
         the leftmost cell to treat the rightmost cell as its left neighbour, and the
         rightmost cell to treat the leftmost cell as its right neighbour.
         
         Shiffman's version ignores the cells on either edge of each generation and this
         results in an incomplete representation of the CA. */
         
      // Wrap around at leftmost cell
      if (i == 0) {
        left  = cells[cells.length -1];
        sub   = cells[i];
        right = cells[i +1];
      }
      // Wrap around at rightmost cell
      else if (i == cells.length -1) {
        left  = cells[i -1];
        sub   = cells[i];
        right = cells[0];
      }
      // All cells in-between
      else {
        left  = cells[i -1];
        sub   = cells[i];
        right = cells[i +1];
      }
      
      nextgen[i] = getResult(left, sub, right);
    }
    cells = nextgen; // Filling curent gen array with next gen array
    generation++;
  }
  
  
  
  // Draw CA
  void display() {
    for(int i = 0; i < cells.length; i++) {
      if (cells[i] == false) fill(dead);
      else                   fill(live);
      if(scale > 2) stroke(colBG);
      else noStroke();

      rectMode(CORNER);
      rect(i*cellSize, generation*cellSize, cellSize, cellSize);
    }
    detailPane();
  }
  
  // Determining result for cell
  boolean getResult(boolean l, boolean s, boolean r) {
    if      (l == false && s == false && r == false) return rule[7];
    else if (l == false && s == false && r == true ) return rule[6];
    else if (l == false && s == true  && r == false) return rule[5];
    else if (l == false && s == true  && r == true ) return rule[4];
    else if (l == true  && s == false && r == false) return rule[3];
    else if (l == true  && s == false && r == true ) return rule[2];
    else if (l == true  && s == true  && r == false) return rule[1];
    else if (l == true  && s == true  && r == true ) return rule[0];
    return false;
  }
  
  
  void reset() {
    for(int i = 0; i < cells.length; i++) {
      cells[i] = false;
    }
    cells[cells.length/2] = true;
    //println(cells.length);
    generation = 0;
  }
  
  
  // This method to display info such as the CA number was created by MON:
  // Pane at bottom with rule-number
  void detailPane() {
    
    int textSize = width/42;
    
    detailPaneH = height -width/2;
    
    fill(colBG);
    noStroke();
    rectMode(CORNER);
    rect(0, height -detailPaneH, width, detailPaneH);
    
    fill(colText);
    textFont(century, textSize);
    textAlign(CENTER);
    text("Rule " + ruleToDecimal(rule), width/2, height -detailPaneH +textSize*3/2);
    
    textAlign(LEFT);
    text("<< 'Backspace'", width/32, height -detailPaneH +textSize*3/2);
    
    //Debugging info:
    /*
    textAlign(RIGHT);
    textFont(arial, width/64);
    text("WIDTH: " + width + "   HEIGHT: " + (height -detailPaneH) + "\nSCALE: " + scale + "   GEN: " + generation, width -10, height -detailPaneH*2/3);
    */
    
  }
  
}
