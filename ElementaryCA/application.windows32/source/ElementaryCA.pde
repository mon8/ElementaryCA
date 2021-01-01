/*  ElementaryCA
    Michael O'Neill
    
    Latest revision: 22/12/2020
       
    All code written by Michael O'Neill except where otherwise stated  */
    
    
PFont arial, century, corbel;
Menu menu;
CA ca;


// Global variables:

  // Colours
  color colBG = color(0,0,16);
  color paneFill = color(0,64,64);
  color colStroke = color(0,235,235);
  color colHighlight = color(255,0,255);
  color colText = color(255);
  
  color live = color(0,128,128);
  color dead = colBG;
  
  // Page-related
  int page = 0;
  int pageLim = 2; // Index of final page
  boolean lastFrameKey = false;
  
  // CA-related
  boolean[] rule = new boolean[8];
  int scale;


void setup() {
  
  fullScreen();
  //size(1024,576);
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


void draw(){
  
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


void titlePage() {
  
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


void pageControl() {
  
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
void newPage(int n) {
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
int ruleToDecimal(boolean[] r) {
  int decimal = 0;
  for(int bit = 7; bit >= 0; bit--){
    if(r[7 -bit] == true){
      decimal = (int)(decimal +1 *pow(2, bit));
    }
  }
  return decimal;
}
