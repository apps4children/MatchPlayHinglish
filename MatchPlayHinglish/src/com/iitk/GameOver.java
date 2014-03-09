package com.iitk;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import com.iitk.matchplay.hinglish.R;
import com.iitk.report.DisplayRecord;
public class GameOver extends Activity implements OnClickListener 
{
	MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gameover);
        
        View play = findViewById(R.id.play);
        play.setOnClickListener(this);
        
        View score =findViewById(R.id.score);
        score.setOnClickListener(this);
        
        View exit = findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }
    public void onClick(View v) 
    {
    	switch (v.getId()) 
    	{
    	case R.id.play:
    		 Intent playgame = new Intent(this,GameActivity.class);
        	 startActivity(playgame);
        	 finish();
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
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            this.finish();
    }
}