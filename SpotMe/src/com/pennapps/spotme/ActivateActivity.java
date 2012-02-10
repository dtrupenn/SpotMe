package com.pennapps.spotme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class ActivateActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate);
		
		Button activateButton = (Button) this.findViewById(R.id.activateButton);
		activateButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				
				System.out.println("SpotMe activated.");
			}
		});
	}

}
