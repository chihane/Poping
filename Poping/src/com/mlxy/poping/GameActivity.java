package com.mlxy.poping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;

public class GameActivity extends Activity {
	GameSurfaceView surfaceView;
	File savedFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		surfaceView = new GameSurfaceView(this);
		savedFile = new File(Environment.getExternalStorageDirectory(), "blocklist.sav");
		
		setContentView(surfaceView);
		
		boolean newGame = this.getIntent().getBooleanExtra("newGame", true);
		if (!newGame) {
			load();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		save();
	}
	
	/** 保存游戏进度。*/
	private void save() {
		// 保存分数和关卡。
		Editor editor = this.getPreferences(MODE_PRIVATE).edit();
		editor.putLong("score", surfaceView.getScore());
		editor.putLong("level", surfaceView.getLevel());
		editor.commit();
		
		// 保存方块列表。
		FileOutputStream out = null;
		ObjectOutputStream output = null;
		try {
			out = this.openFileOutput(savedFile.getName(), Context.MODE_PRIVATE);
			output = new ObjectOutputStream(out);
			
			output.writeObject(GameSurfaceView.blockList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/** 读取游戏进度。*/
	private void load() {
		// 如果文件不存在就跳出读取，否则用户清除数据之后会报错。
		if (!savedFile.exists()) {
			return;
		}
		
		// 读取分数和关卡。
		SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
		surfaceView.setScore(pref.getLong("score", 0));
		surfaceView.setLevel(pref.getLong("level", 0));
		
		// 读取方块列表。
		FileInputStream in = null;
		ObjectInputStream input = null;
		try {
			in = this.openFileInput(savedFile.getName());
			input = new ObjectInputStream(in);
			
			GameSurfaceView.blockList = (Block[][]) input.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
