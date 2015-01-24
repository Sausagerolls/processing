import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import twitter4j.conf.*; 
import twitter4j.*; 
import twitter4j.auth.*; 
import twitter4j.api.*; 
import java.util.*; 
import java.awt.Rectangle; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 

import twitter4j.conf.*; 
import twitter4j.internal.async.*; 
import twitter4j.internal.org.json.*; 
import twitter4j.internal.logging.*; 
import twitter4j.json.*; 
import twitter4j.internal.util.*; 
import twitter4j.management.*; 
import twitter4j.auth.*; 
import twitter4j.api.*; 
import twitter4j.util.*; 
import twitter4j.internal.http.*; 
import twitter4j.*; 
import twitter4j.internal.json.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class tweetSearcher extends PApplet {











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
// The font "AndaleMono-48.vlw"" must be located in the 
// current sketch's "data" directory to load successfully

Minim minim;
AudioInput in;
FFT fft;

int w;
PImage fade;
float rWidth, rHeight;
 
public void setup()
{
  cycle = 0;
    size(1920,1080, P3D);
  background(0);
    ConfigurationBuilder cb = new ConfigurationBuilder();
   cb.setOAuthConsumerKey("GgU2l2vRAPSkyK8aCvzQlGbQW");
   cb.setOAuthConsumerSecret("LFUJxbYZf8ZZ5coYzr7Zf4XY0QV2GaktfYdFhKL6GOK60WKCMK");
   cb.setOAuthAccessToken("166946480-UNKBQiGq3dZJB1MfaFx5YnI5sylVdGMtSydWsNeg");
   cb.setOAuthAccessTokenSecret("l39iKKnHUFq2Bep4wUL8SX23DLUTM26dlqB8APGji9Utr");
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
  rWidth = width * 0.99f;
  rHeight = height * 0.99f;
  c1 = 18;
  c2 = 109;
  c3 = 0;
  flag = 1;
  
}

public void draw()
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
     text("#BournemouthUni", width - width / 2.5f, height - 25);
}

public void getNewTweets()
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

public void refreshTweets()
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--hide-stop", "tweetSearcher" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
