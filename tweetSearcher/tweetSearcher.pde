import twitter4j.conf.*;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.api.*;
import java.util.*;
import java.awt.Rectangle;
import ddf.minim.*;
import ddf.minim.analysis.*;


Twitter twitter;
String searchString = "#BournemouthUni";
List<Status> tweets;

int currentTweet;
int cycle;
int step;
int step2;
int c1;
int c2;
int c3;
int flag;

PFont mono;

Minim minim;
AudioInput in;
FFT fft;

int w;
PImage fade;
float rWidth, rHeight;
 
void setup()
{
  cycle = 0;
    size(1920,1080, P3D);
  background(0);
    ConfigurationBuilder cb = new ConfigurationBuilder();
   cb.setOAuthConsumerKey("##################");
   cb.setOAuthConsumerSecret("#########################");
   cb.setOAuthAccessToken("###############-#################");
   cb.setOAuthAccessTokenSecret("#####################");
    TwitterFactory tf = new TwitterFactory(cb.build());

    twitter = tf.getInstance();

    getNewTweets();

    currentTweet = 0;

    thread("refreshTweets");
    
    step = 100;
    step2 = 500;
    
    mono = createFont("LCD_Solid.ttf", 100);
    textFont(mono);
    minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, 512);
  fft = new FFT(in.bufferSize(), in.sampleRate());
  fft.logAverages(60, 7);
  stroke(18, 109, 0);
  w = width/fft.avgSize();
  strokeWeight(w);
  strokeCap(SQUARE);
  background(0);
  fade = get(0, 0, width, height);
  rWidth = width * 0.99;
  rHeight = height * 0.99;
  c1 = 18;
  c2 = 109;
  c3 = 0;
  flag = 1;
  
}

void draw()
{
  
    background(0);
  tint(255, 255, 255, 252);
  image(fade, 0, 0, rWidth, rHeight);
    fill(c1, c2, c3);
    stroke(c1, c2, c3);
    
    fft.forward(in.mix);

for(int i=0; i < fft.avgSize(); i++)
  {
    line((i * w) + (w / 2), height, (i * w) + (w / 2), height - fft.getAvg(i) * 10);
    
  }
  fill(18, 109, 0);
  
    
    if(flag == 1){
      c2 = c2 + 5;
    }
    if(c2==255){
      flag = 0;
    }
    if(flag == 0){
      c2= c2 -5;
    }
    if(c2==80){
      flag = 1;
    }


  
    currentTweet = currentTweet + 1;

    if (currentTweet >= tweets.size())
    {
        currentTweet = 0;
    }

    Status status = tweets.get(currentTweet);

    fill(18, 109, 0);
    
    println(cycle); 

  stroke(18, 109, 0);
    delay(40);
    if (cycle < 25){
    cycle = cycle + 1;
    
    }
    else{
      cycle = 0;
      textSize(25);
      
      text(status.getText(), width-650, height - (height - step), 600, 100);
      step = step + 75;
//      currentTweet = currentTweet + 1;
//      status = tweets.get(currentTweet);
//      text(status.getText(), width-650, height - (height - step2), 600, 400);
//      step2 = step2 + 50;
      println("Tweeted"); 
      
    }
    if(step > height - 400){
     step = 100; 
    }
    
    if(step2 > height - 200){
     step2 = 100; 
    }
    noTint();
    rect(0, height-1, width-18, 100);
    noStroke();
    triangle(width-18, height, width-19, height-10, width+1, height);
     fade = get(0, 0, width, height);
     textSize(height/15);
     fill(0, 0, 0);
     text("#BournemouthUni", width - width / 2.5, height - 25);
}

void getNewTweets()
{
    try 
    {
        Query query = new Query(searchString);
        QueryResult result = twitter.search(query);
        tweets = result.getTweets();
    } 
    catch (TwitterException te) 
    {
        System.out.println("Failed to search tweets: " + te.getMessage());
        System.exit(-1);
    } 
}

void refreshTweets()
{
    while (true)
    {
        getNewTweets();
        println("Updated Tweets"); 
        delay(900000);
        fill(0, 255);
        rect(0, 0, width, height);
    }
}
