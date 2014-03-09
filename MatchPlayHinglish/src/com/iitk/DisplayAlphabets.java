package com.iitk;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.iitk.matchplay.hinglish.R;
public class DisplayAlphabets extends Activity implements OnClickListener
{
	Button next;
	Button previous;
	ViewPager viewPager;
	private int focusedPage = 0;
	
	int a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z;
	MediaPlayer mediaPlayer;
	SoundManager snd;
	Timer timer;
	int delay = 1000;
	  @Override
	  public void onCreate(Bundle savedInstanceState) 
	  {
	    super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.display_alphabets);
	    viewPager = (ViewPager) findViewById(R.id.view_pager);
	    ImageAdapter adapter = new ImageAdapter(this);
	    viewPager.setAdapter(adapter);
	    next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.back);
        
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        previous.setVisibility(View.GONE);   
        
        viewPager.setOnPageChangeListener(new MyPageChangeListener()); 
        
       //sound managing
        mediaPlayer = MediaPlayer.create(DisplayAlphabets.this, R.raw.a);//play on load
        timer();//play on load
        
        snd = new SoundManager(getApplicationContext());
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        a=snd.load(R.raw.a);
        b=snd.load(R.raw.b);
        c=snd.load(R.raw.c);
        d=snd.load(R.raw.d);
        e=snd.load(R.raw.e);
        f=snd.load(R.raw.f);
        g=snd.load(R.raw.g);
        h=snd.load(R.raw.h);
        i=snd.load(R.raw.i);
        j=snd.load(R.raw.j);
        k=snd.load(R.raw.k);
        l=snd.load(R.raw.l);
        m=snd.load(R.raw.m);
        n=snd.load(R.raw.n);
        o=snd.load(R.raw.o);
        p=snd.load(R.raw.p);
        q=snd.load(R.raw.q);
        r=snd.load(R.raw.r);
        s=snd.load(R.raw.s);
        t=snd.load(R.raw.t);
        u=snd.load(R.raw.u);
        v=snd.load(R.raw.v);
        w=snd.load(R.raw.w);
        x=snd.load(R.raw.x);
        y=snd.load(R.raw.y);
        z=snd.load(R.raw.z);
        
        Button backtohome=(Button)findViewById(R.id.backtohome);
        backtohome.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v) 
			{
					finish();
		}});
     }
    public void onClick(View v){
    	if (v == next) {
    		viewPager.setCurrentItem(getItem(+1), true);
    	}
    	else if (v == previous) {
    		viewPager.setCurrentItem(getItem(-1), true);
    	}
    	
    	hideAndShowbutton();
    }
    private int getItem(int i) {
        int a = viewPager.getCurrentItem();
        i += a;
        return i;
    }
    public void hideAndShowbutton()
    {
    	int b=viewPager.getCurrentItem();
    	if(b>0)
    	{
    		previous.setVisibility(View.VISIBLE);
    	}
    	else if(b==0)
    	{
    		previous.setVisibility(View.GONE);
    	}
    	
    	if(b==25)
    	{
    		next.setVisibility(View.GONE);
    	}
    	else
    	{
    		next.setVisibility(View.VISIBLE);
    	}
    	//System.out.println("Item number=:"+b);
    }   
    
    
    //current page detector
    private class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener 
    {
        @Override
        public void onPageSelected(int position) 
        {
        	playSounds(position);//play sounds
            focusedPage = position;
            //System.out.println("SimpleOnPageChangeListener=:"+focusedPage);
            if(focusedPage>0)
        	{
        		previous.setVisibility(View.VISIBLE);
        	}
        	else if(focusedPage==0)
        	{
        		previous.setVisibility(View.GONE);
        	}
        	
        	if(focusedPage==25)
        	{
        		next.setVisibility(View.GONE);
        	}
        	else
        	{
        		next.setVisibility(View.VISIBLE);
        	}
        }
    }
    
    public void playSounds(int id)
	{
    	int position=id;
		if(position==0)
			snd.play(a);
		else if(position==1)
			snd.play(b);
		else if(position==2)
			snd.play(c);
		else if(position==3)
			snd.play(d);
		else if(position==4)
			snd.play(e);
		else if(position==5)
			snd.play(f);
		else if(position==6)
			snd.play(g);
		else if(position==7)
			snd.play(h);
		else if(position==8)
			snd.play(i);
		else if(position==9)
			snd.play(j);
		else if(position==10)
			snd.play(k);
		else if(position==11)
			snd.play(l);
		else if(position==12)
			snd.play(m);
		else if(position==13)
			snd.play(n);
		else if(position==14)
			snd.play(o);
		else if(position==15)
			snd.play(p);
		else if(position==16)
			snd.play(q);
		else if(position==17)
			snd.play(r);
		else if(position==18)
			snd.play(s);
		else if(position==19)
			snd.play(t);
		else if(position==20)
			snd.play(u);
		else if(position==21)
			snd.play(v);
		else if(position==22)
			snd.play(w);
		else if(position==23)
			snd.play(x);
		else if(position==24)
			snd.play(y);
		else if(position==25)
			snd.play(z);
	}
    public void timer()
	{
		timer= new Timer();
        timer.schedule( new TimerTask()
        {
           public void run() 
           { 
        	   mediaPlayer.start();
           }
         },delay);
	}
}

