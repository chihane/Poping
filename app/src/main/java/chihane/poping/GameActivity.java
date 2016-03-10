package chihane.poping;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

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
//			load();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

//		save();
	}

//	private void save() {
//		Editor editor = this.getPreferences(MODE_PRIVATE).edit();
//		editor.putLong("score", surfaceView.getScore());
//		editor.putLong("level", surfaceView.getLevel());
//		editor.commit();
//
//		FileOutputStream out = null;
//		ObjectOutputStream output = null;
//		try {
//			out = this.openFileOutput(savedFile.getName(), Context.MODE_PRIVATE);
//			output = new ObjectOutputStream(out);
//
//			output.writeObject(GameSurfaceView.blockMatrix);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				out.close();
//				output.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	private void load() {
//		if (!savedFile.exists()) {
//			return;
//		}
//
//		SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
//		surfaceView.setScore(pref.getLong("score", 0));
//		surfaceView.setLevel(pref.getLong("level", 0));
//
//		FileInputStream in = null;
//		ObjectInputStream input = null;
//		try {
//			in = this.openFileInput(savedFile.getName());
//			input = new ObjectInputStream(in);
//
//			GameSurfaceView.blockMatrix = (Block[][]) input.readObject();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				in.close();
//				input.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
