package chihane.poping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.button_start).setOnClickListener(this);
		findViewById(R.id.button_continue).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, GameActivity.class);
		
		switch (v.getId()) {
		
		case R.id.button_start:
			i.putExtra("newGame", true);
			break;
		case R.id.button_continue:
			i.putExtra("newGame", false);
		}
		
		startActivity(i);
		finish();
	}
}
