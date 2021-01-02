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

public class ElementaryCA extends PApplet {

/*  ElementaryCA
    Michael O'Neill
    
    Latest revision: 22/12/2020
       
    All code written by Michael O'Neill except where otherwise stated  */
    
    
PFont arial, century, corbel;
Menu menu;
CA ca;


// Global variables:

  // Colours
  int colBG = color(0,0,16);
  int paneFill = color(0,64,64);
  int colStroke = color(0,235,235);
  int colHighlight = color(255,0,255);
  int colText = color(255);
  
  int live = color(0,128,128);
  int dead = colBG;
  
  // Page-related
  int page = 0;
  int pageLim = 2; // Index of final page
  boolean lastFrameKey = false;
  
  // CA-related
  boolean[] rule = new boolean[8];
  int scale;


public void setup() {
  
  //fullScreen();
  
  background(colBG);
  frameRate(60);
  
  century = createFont("CENTURY.TTF", 32);
  corbel = createFont("corbell.ttf", 32);
  arial = createFont("arial.ttf", 32);
  
  menu = new Menu();
  
  scale = 8;
  
  // Arbitrarily set the rule to Rule 110 (01101110) on start-up:  
  rule[0] = false;
  rule[1] = true;
  rule[2] = true;
  rule[3] = false;
  rule[4] = true;
  rule[5] = true;
  rule[6] = true;
  rule[7] = false;
  
/* Rule 110 is known to be Turing complete (Cook, 2004). This
   implies that, in principle, any calculation or
   computer program can be simulated using this
   automaton.
   
   http://www.complex-systems.com/pdf/15-1-1.pdf
   https://en.wikipedia.org/wiki/Rule_110  */ 
  
}


public void draw(){
  
  pageControl();
  
  // Title page
  if (page == 0) titlePage();
  
  // Menu page
  else if (page == 1) {
    menu.display();
    menu.select();
    ca = new CA(scale);
    ca.reset();
  }
  
  // CA page
  else if (page == 2) {
    ca.display();
    ca.computeNextGen();
  }
  
  // New pages for quick testing (enable in pageControl())
  else if (page >= 2) {
    ca.reset();
    newPage(page);
  }
  
}


public void titlePage() {
  
  float delay = 2; // Title page duration in seconds
  int framesOnTitle = frameCount;
  
  int titleSize = width/14;
  
  background(colBG);
  
  textFont(century, titleSize);
  textAlign(CENTER);

  fill(0,64,64);
  text("ElementaryCA", width/2 -titleSize/16, height*2/5 +titleSize/16);
  fill(colText);
  text("ElementaryCA", width/2, height*2/5);
  
  textFont(corbel, width/38);
  text("Michael O'Neill", width/2, height*3/5);
  
  if(framesOnTitle > 60*delay) page++;
}


public void pageControl() {
  
  int limit = 2;
  
  // To next page
  if(page < limit){
    if(!lastFrameKey && keyPressed) {
      if(key == ENTER) {
        background(colBG);
        page++;
        lastFrameKey = true;
      }
    }
  }
  
  // To previous page
  if(page > 1) {
    if(!lastFrameKey && keyPressed) {
      if(key == BACKSPACE) {
        background(colBG);
        page--;
        lastFrameKey = true;
      }
    }
  }
  
  lastFrameKey = keyPressed; // Prevents skipping multiple pages at keyPressed
}


// Creates new pages as placeholders
public void newPage(int n) {
  background(colBG);
  
  fill(colText);
  textAlign(CENTER);
  textSize(width/24);
  text("This is page " + n, width/2, height/2);
  
  textAlign(LEFT);
  textSize(width/32);
  text("Rule " + ruleToDecimal(rule), width/32, height *31/32);
}


// Returns rule as decimal value
public int ruleToDecimal(boolean[] r) {
  int decimal = 0;
  for(int bit = 7; bit >= 0; bit--){
    if(r[7 -bit] == true){
      decimal = (int)(decimal +1 *pow(2, bit));
    }
  }
  return decimal;
}
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
  public void computeNextGen() {
    
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
  public void display() {
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
  public boolean getResult(boolean l, boolean s, boolean r) {
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
  
  
  public void reset() {
    for(int i = 0; i < cells.length; i++) {
      cells[i] = false;
    }
    cells[cells.length/2] = true;
    //println(cells.length);
    generation = 0;
  }
  
  
  // This method to display info such as the CA number was created by MON:
  // Pane at bottom with rule-number
  public void detailPane() {
    
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
class Menu {
  
  boolean lastFrameClick = false;
  
  int margin = width/32;
  
  int textSize = width/32;
    
  int paneW = width -margin*2;
  int condW = paneW/8;
  int paneH = condW*3/4;
  int tileSize = condW/4;
  
  int scaleX = width/2;
  int scaleY = height*2/3;
  
  
  public void display() {
    
    background(colBG);
    
    // Display rule as decimal value
    fill(colText);
    textFont(century, textSize);
    textAlign(LEFT);
    text("Rule " + ruleToDecimal(rule), margin, height/2 -paneH -textSize/2);
   
    // Draw pane:
    stroke(colStroke);
    rectMode(CORNER);
    fill(paneFill);
    rect(margin, height/2 -paneH, paneW, paneH);
    for(int i = 0; i < 8; i++) {
      line(margin +(i*condW), height/2, margin +(i*condW), height/2 -paneH);
    }
    
    
    // Draw subjects:
    for(int i = 0; i < 8; i++) {
      int xPos = margin +(i*condW) +condW/2;
      int yPos = height/2 -paneH*2/3;
      
      if(i == 0) fill(live);
      if(i == 1) fill(live);
      if(i == 2) fill(dead);
      if(i == 3) fill(dead);
      if(i == 4) fill(live);
      if(i == 5) fill(live);
      if(i == 6) fill(dead);
      if(i == 7) fill(dead);
      
      stroke(colStroke);
      rectMode(CENTER);
      rect(xPos, yPos, tileSize, tileSize);
    }
    
    // Draw left neighbours:
    for(int i = 0; i < 8; i++) {
      int xPos = margin +(i*condW) +condW/2 -tileSize;
      int yPos = height/2 -paneH*2/3;
      
      if(i == 0) fill(live);
      if(i == 1) fill(live);
      if(i == 2) fill(live);
      if(i == 3) fill(live);
      if(i == 4) fill(dead);
      if(i == 5) fill(dead);
      if(i == 6) fill(dead);
      if(i == 7) fill(dead);
      
      stroke(colStroke);
      rectMode(CENTER);
      rect(xPos, yPos, tileSize, tileSize);
    }
    
    // Draw right neighbours:
    for(int i = 0; i < 8; i++) {
      int xPos = margin +(i*condW) +condW/2 +tileSize;
      int yPos = height/2 -paneH*2/3;
      
      if(i == 0) fill(live);
      if(i == 1) fill(dead);
      if(i == 2) fill(live);
      if(i == 3) fill(dead);
      if(i == 4) fill(live);
      if(i == 5) fill(dead);
      if(i == 6) fill(live);
      if(i == 7) fill(dead);
      
      stroke(colStroke);
      rectMode(CENTER);
      rect(xPos, yPos, tileSize, tileSize);
    }
    
    // Draw result tiles:
    for(int i = 0; i < 8; i++) {
      int xPos = margin +(i*condW) +condW/2;
      int yPos = height/2 -paneH*1/3;
      
      if (mouseX < width -margin && mouseX > margin && mouseY < height/2 && mouseY > height/2 - paneH) {
        if (mouseX < margin +(i*condW) + condW && mouseX > margin +(i*condW)) {
          stroke(colHighlight);
          footnote("Toggle the states of the resulting cells to set your rule");
        }
      }
      
      if (rule[i] == false) fill(dead);
      else if (rule[i] == true) fill(live);
      
      rectMode(CENTER);
      rect(xPos, yPos, tileSize, tileSize);
      stroke(colStroke);
      fill(dead);
    }
    
    // Draw scale selector:
    stroke(colStroke);
    fill(colBG);
    rectMode(CENTER);
    rect(scaleX, scaleY, textSize*2, textSize*2);
    
    fill(colText);
    textAlign(CENTER);
    text(scale, width/2, height*2/3 +textSize/3);
    
    textFont(century, width/42);
    textAlign(RIGHT);
    text("'Enter' >>", width -width/32, height/16);
  
  }
  
  
  public void select() {
    
    // Selecting the rule:
    for(int i = 0; i < 8; i++) {
      // If mouse clicked inside pane
      if (mouseX < width -margin && mouseX > margin && mouseY < height/2 && mouseY > height/2 - paneH) {
        if (mouseX < margin +(i*condW) + condW && mouseX > margin +(i*condW)) {
          if (!lastFrameClick && mousePressed) {
            
            if (!rule[i]) {
              rule[i] = true;
              //println("rule " + (i+1) + ": " + rule[i]);
            }
            
            else if (rule[i]) {
              rule[i] = false;
              //println("rule " + (i+1) + ": " + rule[i]);  
            }
            
          }
        }
      }
    }
    
    // Selecting scale (L and W of each cell in px)
    if(mouseX > scaleX -textSize && mouseX < scaleX +textSize && mouseY > scaleY -textSize && mouseY < scaleY +textSize) {
      
      stroke(colHighlight);
      noFill();
      rectMode(CENTER);
      rect(scaleX, scaleY, textSize*2, textSize*2);
      
      footnote("Set the pixel size of cells. Left-click increases. Right-click decreases.");
      if(!lastFrameClick && mousePressed) {
        if(mouseButton == LEFT ){
          scale *= 2;
          if(scale > 64) scale = 64;
        }
        if(mouseButton == RIGHT) {
          scale /= 2;
          if(scale < 1) scale = 1;
        }
        
        lastFrameClick = true;
      }
    }
    
    
    lastFrameClick = mousePressed;
  }
  
  
  public void footnote(String comment) {
    
    int size = width/32;
    
    textFont(century, size);
    textAlign(CENTER);
    
    fill(96,16,64);
    text("* * *", width/2, height -size*2);
    
    fill(colText);
    text(comment, width/2, height -size);
    
  }
  
  
}// end of class
  public void settings() {  size(1024,576); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "ElementaryCA" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
