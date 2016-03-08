package com.mlxy.poping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GameOverActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);

		findViewById(R.id.button_replay).setOnClickListener(this);

		TextView textView = (TextView) findViewById(R.id.textView1);
		Intent intent = this.getIntent();
		textView.setText("总计玩了" + intent.getLongExtra("level", 0) + "关，得分" + intent.getLongExtra("score", 0));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.button_replay:
				Intent intent = new Intent(this, GameActivity.class);
				intent.putExtra("newGame", true);
				this.startActivity(intent);
				this.finish();
				break;
		}
	}
}
