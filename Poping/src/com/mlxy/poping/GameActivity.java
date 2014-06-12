package com.mlxy.poping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

	@SuppressLint("WrongCall")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GameSurfaceView(this));
	}
}
