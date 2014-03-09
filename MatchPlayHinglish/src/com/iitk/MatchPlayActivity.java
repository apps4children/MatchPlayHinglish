package com.iitk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.iitk.database.TestAdapter;
import com.iitk.matchplay.hinglish.R;
import com.iitk.report.DisplayRecord;
public class MatchPlayActivity extends Activity implements OnClickListener 
{
	Dialog dialog;
	EditText name;
	int saveroll;
	TestAdapter mDbHelper; 
	String savename="";
	
	Intent playEasy;
	Intent playHard;
	MediaPlayer mediaPlayer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        mDbHelper = new TestAdapter(this);
        
        View play = findViewById(R.id.play);
        play.setOnClickListener(this);
        
        View instruction =findViewById(R.id.instruction);
        instruction.setOnClickListener(this);
        
        View score =findViewById(R.id.score);
        score.setOnClickListener(this);
        
        View exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);
        
        userForm();
        
        playEasy= new Intent(this,DisplayAlphabets.class);
        playHard= new Intent(this,GameActivity.class);
        
        mediaPlayer = MediaPlayer.create(MatchPlayActivity.this, R.raw.gamestartsequentern);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
        {
        public void onCompletion(MediaPlayer mp) 
        {
           mediaPlayer.start();
        }
        });
        mediaPlayer.start();
        
    }
    public void onClick(View v) 
    {
    	switch (v.getId()) 
    	{
    	case R.id.play:
    		alert();
    		//Intent playgame = new Intent(this,DisplayAlphabets.class);
        	//startActivity(playgame);
        	break;
    	case R.id.instruction:
    	     Intent gameInstruction= new Intent(this,Instruction.class);
    	     startActivity(gameInstruction);
    	     break;
    	case R.id.score:
  	         Intent gameScore= new Intent(this,DisplayRecord.class);
  	         startActivity(gameScore);
  	         break;
    	case R.id.exit:
             finish();
             System.exit(0);
       	break;
    	}
    }
    
    public void userForm()
	{
		dialog = new Dialog(MatchPlayActivity.this);
		dialog.setContentView(R.layout.userdetail);
		dialog.setTitle("Player Details");
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

        name=(EditText) dialog.findViewById(R.id.name);
        Button save= (Button)dialog.findViewById(R.id.save);
        Button cancel= (Button)dialog.findViewById(R.id.cancel);
		save.setOnClickListener(new OnClickListener() 
        {
        public void onClick(View v) 
        {
        savename=name.getText().toString();
        if (name ==null|| name.length() == 0) 
      	{
		name.setError("Enter Your Name");
		name.requestFocus();
		}
        else
      	{
         int userid;
         userid=Integer.parseInt(savePlayerNameDB(savename));//inserting name in database and fetching id
         savePlayerNameApplication(savename, userid);//saving name and id to shared preference
      	 dialog.dismiss();
      	}
        }
        });
		cancel.setOnClickListener(new OnClickListener() 
        {
        public void onClick(View v) 
        {
        	savePlayerNameApplication("",0);//saving null in name and  0 in id to shared preference to ignore recording
        	dialog.dismiss();
        }
        });
		dialog.show();
	}
    
    public void savePlayerNameApplication(String name,int id)
    {
    SharedPreferences sharedPref= getSharedPreferences("mypref", MODE_PRIVATE);
    SharedPreferences.Editor editor= sharedPref.edit();
    editor.putString("playerName",name);
    editor.putInt("playerID",id);
    editor.commit();
    }
    
    public String savePlayerNameDB(String name) 
    {
    	String id;
		mDbHelper.createDatabase();       
		mDbHelper.open(); 
		id=mDbHelper.insertStudent(name); 
		mDbHelper.close();
		return id;
	}
    public void alert()
    {
    	String[] levels = {"Learn alphabets/अक्षरों को पहचानना सीखे", "Match alphabets and numbers/अक्षरों और संख्याओं को मिलाएँ"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setTitle(getString(R.string.levelSelect));
		builder.setItems(levels, new DialogInterface.OnClickListener() 
		{
		    public void onClick(DialogInterface dialog, int item) 
		    {
              if(item==0)
              {
            	  startActivity(playEasy);
              }
              else
            	  startActivity(playHard);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
    }
    
    
    @Override
    protected void onPause() {
      super.onPause();
      mediaPlayer.pause();
    }
    @Override
    protected void onResume() {
      super.onResume();
      mediaPlayer.start();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}