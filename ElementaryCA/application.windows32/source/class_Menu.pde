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
  
  
  void display() {
    
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
  
  
  void select() {
    
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
  
  
  void footnote(String comment) {
    
    int size = width/32;
    
    textFont(century, size);
    textAlign(CENTER);
    
    fill(96,16,64);
    text("* * *", width/2, height -size*2);
    
    fill(colText);
    text(comment, width/2, height -size);
    
  }
  
  
}// end of class
