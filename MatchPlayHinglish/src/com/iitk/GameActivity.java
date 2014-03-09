package com.iitk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

import com.iitk.database.TestAdapter;
import com.iitk.matchplay.hinglish.R;
public class GameActivity extends Activity implements OnClickListener
{
	StringBuilder sb = new StringBuilder();
	TestAdapter mDbHelper;
	String wrongQuestion=null;
	int totalmistakes=0;
	
	Animation animAlpha,animScale,animBounce; 
	String matchtext=null;
	Button matchbutton1,matchbutton2,button,button2;
	GridView gridView;
	private ArrayList<Button> mButtons=null;
	String [] alphabets;
	int screenCounter=1,matchedPair=0;
	Typeface face1;
	Intent intent;
	Intent gameover;
	
	SoundManager snd;
	int right,wrong,newscreen;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.matchgame);
        
        mDbHelper = new TestAdapter(this);
        
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.matchplay_hindi);//play on load
        mediaPlayer.start();//play on load
        
        animAlpha=AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        animScale=AnimationUtils.loadAnimation(this,R.anim.anim_scale);
        animBounce=AnimationUtils.loadAnimation(this,R.anim.bounce);
                
        snd = new SoundManager(getApplicationContext());
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        right=snd.load(R.raw.right);
        wrong=snd.load(R.raw.wrong);
        newscreen=snd.load(R.raw.fallmatchplay);
               
        Button back=(Button)findViewById(R.id.back);
        Button next=(Button)findViewById(R.id.next);
        
        intent =new Intent(this,MatchPlayActivity.class);
        gameover =new Intent(this,GameOver.class);
        back.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v) 
			{
				finish();
		    }
			});
        
        next.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v) 
			{
				screenCounter=screenCounter+1;
				if(screenCounter<=10)
				{
			        createGridView();
				}
				else if(screenCounter>10)
				{
					gameEnd();
				}
		}});
        
        initialGridView();
        
        //face1 = Typeface.createFromAsset(getAssets(),"fonts/KidsAlphabet.ttf");
    }
	public void onClick(View v) 
	{
		button=(Button) v;
		String buttonText=(String) button.getText();
		if(matchtext==null)
		{
			button.setBackgroundResource(R.drawable.shadow);
			button.setOnClickListener(null);
			matchtext=buttonText;
			matchbutton1=button;
		}
		else if(matchtext!=null)
		{
			if(matchtext.equalsIgnoreCase(buttonText))
			{  
				//
				if(wrongQuestion!=null&&wrongQuestion.equalsIgnoreCase(matchtext))
				{
			    sb.append(","+buttonText);
				wrongQuestion=null;
				}
				else
			    sb.append(matchtext+"-"+buttonText);
				sb.append("\n");
			    //	
				
				matchbutton2=button;
				matchbutton2.setBackgroundResource(R.drawable.shadow);
				matchbutton2.setOnClickListener(null);
				matchbutton2.startAnimation(animScale);
				matchbutton1.startAnimation(animScale);
				snd.play(right);
				//remove visibilty
				removeButtons();
				matchedPair=matchedPair+1;
				//System.out.println("matched Counter="+matchedPair);
			}
			else
			{
				snd.play(wrong);
				button.startAnimation(animAlpha);	
				
				//
				totalmistakes=totalmistakes+1;
				if(wrongQuestion==null){
				   wrongQuestion=matchtext;
				   sb.append(matchtext+"-"+buttonText);
				}
				else if(wrongQuestion.equalsIgnoreCase(matchtext)){
				   sb.append(","+buttonText);
				}
				else if(!wrongQuestion.equalsIgnoreCase(matchtext)){
				   //sb.append("\n");
				   wrongQuestion=matchtext;
				   sb.append(matchtext+"-"+buttonText);  
				}//
			}
		}
		
		if(matchedPair==7)
        {
     	  matchedPair=0;
	      screenCounter=screenCounter+1;
	      if(screenCounter<=10)
	      createGridView();
	      else
	    	  gameEnd();
        }
	}
	/*****************************************************game Over********************************************************************/
	public void gameEnd()
	{
		//
	     if(sb.toString().length()==0)
	     sb.append("No mistakes");
	     savescore(sb.toString());
	     saveMistakes(String.valueOf(totalmistakes));
	     //
	     
		int DELAY = 1000;
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() 
	    {            
	        public void run() 
	        {
	        	startActivity(gameover);
				finish();
	        }
	    }, DELAY);
	}
	public void removeButtons()
	{		
		int DELAY = 1000;
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() 
	    {            
	        public void run() 
	        {
	        	matchbutton2.setBackgroundColor(Color.TRANSPARENT);
	        	matchbutton2.setText("");
	        	matchbutton2.setOnClickListener(null);
				
				matchbutton1.setBackgroundColor(Color.TRANSPARENT);
				matchbutton1.setText("");
				matchbutton1.setOnClickListener(null);
				matchtext=null;
	        }
	    }, DELAY);
	 }
	/****************************************************create gridview without timer******************************************************/
	public void initialGridView()
	{   
		snd.play(newscreen);
		matchtext=null;
		mButtons = new ArrayList<Button>();
		alphabets=randomCharacter(screenCounter);
		//
		   DisplayMetrics metrics = new DisplayMetrics();
		   getWindowManager().getDefaultDisplay().getMetrics(metrics);
		   int width = metrics.widthPixels;
		   int height = metrics.heightPixels;
		   height=height-height*28/100;
		   //
		
        Button button= null;
 	    for (int i=0;i<35;i++) 
 	     {
 	     button =(Button)getLayoutInflater().inflate(R.layout.gridcell, null);
 	    button.setMinimumHeight(height/5);
 	     button.setText(alphabets[i]);
 	     button.setId(i);
 	     //button.setTypeface(face1);
 	     button.setTag(alphabets[i]);
 	     if(alphabets[i].length()==0)
         	button.setBackgroundColor(Color.TRANSPARENT);
 	     else if(i<7)
	     {
	     button.setBackgroundResource(R.drawable.b1);
	     button.setOnClickListener(this);
	     }
	     else if(i>6&&i<14)
	     {
	     button.setBackgroundResource(R.drawable.b2);
	     button.setOnClickListener(this);
	     }
	     else if(i>13&&i<21)
	     {
	     button.setBackgroundResource(R.drawable.b3);
	     button.setOnClickListener(this);
	     }
	     else if(i>20&&i<28)
	     {
	     button.setBackgroundResource(R.drawable.b4);
	     button.setOnClickListener(this);
	     }
	     else if(i>27&&i<35)
	     {
	     button.setBackgroundResource(R.drawable.b5);
	     button.setOnClickListener(this);
	     }
 	     mButtons.add(button);
 	     }
         gridView = (GridView) findViewById(R.id.gridview);
         gridView.setAdapter(new CustomAdapter(mButtons));
         CustomAdapter ca=new CustomAdapter(mButtons);
         ca.notifyDataSetChanged();
         gridView.startAnimation(animBounce);
	}
	/****************************************************create gridview with timer******************************************************/
	public void createGridView()
	{   
		
		matchtext=null;
		mButtons = new ArrayList<Button>();
		alphabets=randomCharacter(screenCounter);
		
		 //
		   DisplayMetrics metrics = new DisplayMetrics();
		   getWindowManager().getDefaultDisplay().getMetrics(metrics);
		   int width = metrics.widthPixels;
		   int height = metrics.heightPixels;
		   height=height-height*28/100;
		   //
        Button button= null;
 	    for (int i=0; i<35; i++) 
 	     {
 	     button =(Button)getLayoutInflater().inflate(R.layout.gridcell, null);
 	     button.setMinimumHeight(height/5);
 	     button.setText(alphabets[i]);
 	     button.setId(i);
 	     //button.setTypeface(face1);
 	     button.setTag(alphabets[i]);
 	     if(alphabets[i].length()==0)
         	button.setBackgroundColor(Color.TRANSPARENT);
 	    else if(i<7)
	     {
	     button.setBackgroundResource(R.drawable.b1);
	     button.setOnClickListener(this);
	     }
	     else if(i>6&&i<14)
	     {
	     button.setBackgroundResource(R.drawable.b2);
	     button.setOnClickListener(this);
	     }
	     else if(i>13&&i<21)
	     {
	     button.setBackgroundResource(R.drawable.b3);
	     button.setOnClickListener(this);
	     }
	     else if(i>20&&i<28)
	     {
	     button.setBackgroundResource(R.drawable.b4);
	     button.setOnClickListener(this);
	     }
	     else if(i>27&&i<35)
	     {
	     button.setBackgroundResource(R.drawable.b5);
	     button.setOnClickListener(this);
	     }
 	     mButtons.add(button);
 	     }
 	    int DELAY = 1000;
	    Handler handler = new Handler();
	    handler.postDelayed(new Runnable() 
	    {            
	        public void run() 
	        {
	          snd.play(newscreen);
 	          gridView = (GridView) findViewById(R.id.gridview);
	          gridView.setAdapter(new CustomAdapter(mButtons));
	          CustomAdapter ca=new CustomAdapter(mButtons);
	          ca.notifyDataSetChanged();
              gridView.startAnimation(animBounce);
	        }
	    }, DELAY);
	}
	/**************************************************************random character******************************************************/
	public String[] randomCharacter(int screen)
	{
		int startPosition=0,digitStartPosition=0;
        Random generator = new Random();
        ArrayList<String> sourceChar=new ArrayList<String>();
        ArrayList<String> fourteenAlphabets=new ArrayList<String>();
        
		String [] capital={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String [] small={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		String [] digit={"0","1","2","3","4","5","6","7","8","9"};
		String [] pattern={"","","","","","",""
				          ,"","","","","","",""
				          ,"","","","","","",""
				          ,"","","","","","",""
				          ,"","","","","","",""};
		String [] lastScreen={"1","9","Z","z","A","a","B","b","C","c","D","d","1","9"};
		String [] allCombination={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9"};
		int [] charPosition={3,9,10,11,14,15,16,18,19,20,23,24,25,31};
		
		//provide range for all five screen which will seen in game
		if(screen==1){
			startPosition=0;
			digitStartPosition=0;
		}
		else if(screen==2){
			startPosition=5;
			digitStartPosition=2;
		}
		else if(screen==3){
			startPosition=10;
			digitStartPosition=4;
		}
		else if(screen==4){
			startPosition=15;
			digitStartPosition=6;
		}
		else if(screen==5){
			startPosition=20;
			digitStartPosition=8;
		}
			
	    //selecting first 14 char do display
        for(int i=startPosition;i<startPosition+5;i++){//5 from capital
        	sourceChar.add(capital[i]);
        	sourceChar.add(small[i]);
        }
        for(int i=digitStartPosition;i<digitStartPosition+2;i++){//2 from digit
        	sourceChar.add(digit[i]);
        }
        for(int i=digitStartPosition;i<digitStartPosition+2;i++){//5 from small
        	sourceChar.add(digit[i]);
        }
        
        //final random char generation
        if(screen<=5)
        {
           for(int i=0;i<14;i++)
           {
        	int position = generator.nextInt(sourceChar.size());
        	fourteenAlphabets.add(sourceChar.get(position));
        	sourceChar.remove(position);
           }
        }
        else if(screen==6)
        {
        	List<String> stringList = new ArrayList<String>(Arrays.asList(lastScreen)); 
        	for(int i=0;i<14;i++)
            {
         	int position = generator.nextInt(stringList.size());
         	fourteenAlphabets.add(stringList.get(position));
         	stringList.remove(position);
            }
        }
        else if(screen>6)
        {
        	List<String> stringList2=new ArrayList<String>(Arrays.asList(allCombination)); 
        	List<String> stringList=new ArrayList<String>(); 
        	for(int i=0;i<7;i++)
            {
         	int position = generator.nextInt(stringList2.size());
         	stringList.add(stringList2.get(position));
         	stringList2.remove(position);
            }
        	for(int i=0;i<7;i++)
            {
        	stringList.add(stringList.get(i));
            }
        	for(int i=0;i<14;i++)
            {
         	int position = generator.nextInt(stringList.size());
         	fourteenAlphabets.add(stringList.get(position));
         	stringList.remove(position);
            }
        }
        for(int i=0;i<charPosition.length;i++)
        {
            pattern[charPosition[i]]=fourteenAlphabets.get(i);
        }
        //String[] alphabet=fourteenAlphabets.toArray(new String[0]);  
 		//return alphabet;
        return pattern;
	} 
	@Override
    public void onBackPressed() {
            super.onBackPressed();
            this.finish();
    }
	
	public void savescore(String data)
    {
    	int id;
    	SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
    	String playerName= sharedPref.getString("playerName", null);
    	id= sharedPref.getInt("playerID",0);
    	//id=Integer.parseInt(playerID);
    	
    	System.out.println("Player Name at Level1:="+playerName);
    	System.out.println("Player Id="+id);
    	if(playerName!=null&&playerName.length()>0)
    	{
    		mDbHelper.createDatabase();       
    		mDbHelper.open(); 
    		mDbHelper.updateLevel(id, data,"level1"); 
    		mDbHelper.close();
    	}
    }
	
	public void saveMistakes(String data)
    {
    	int id;
    	SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
    	String playerName= sharedPref.getString("playerName", null);
    	id= sharedPref.getInt("playerID",0);
    	//id=Integer.parseInt(playerID);
    	
    	System.out.println("Player Name at Level1:="+playerName);
    	System.out.println("Player Id="+id);
    	if(playerName!=null&&playerName.length()>0)
    	{
    		mDbHelper.createDatabase();       
    		mDbHelper.open(); 
    		mDbHelper.updateLevel(id, data,"totalmistake"); 
    		mDbHelper.close();
    	}
    }
}