package com.iitk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.iitk.matchplay.hinglish.R;
public class Instruction extends Activity 
{
@Override
protected void onCreate(Bundle savedInstanceState) 
{
super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.instruction);

Button backtohome=(Button)findViewById(R.id.back);
backtohome.setOnClickListener(new Button.OnClickListener()
{
	public void onClick(View v) 
	{
			finish();
    }
});
}
@Override
public void onBackPressed() {
        super.onBackPressed();
        this.finish();
}
}