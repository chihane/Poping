package com.mlxy.poping;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

/** 用于更新画面内容的句柄。 */
public class UpdateHandler extends Handler {
	public static final int MESSAGE_REDRAW_ALL = 0;
	public static final int MESSAGE_DRAW_SELECTED_MARK = 1;
	
	private GameSurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	
	public UpdateHandler(GameSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		this.surfaceHolder = surfaceView.getHolder();
	}
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Canvas canvas = null;
		try {
			canvas = surfaceHolder.lockCanvas(new Rect(0, surfaceView.getTopSidePosition(),
									surfaceView.getWidth(), surfaceView.getHeight()));
			switch (msg.what) {
			case MESSAGE_REDRAW_ALL:
				surfaceView.redraw(canvas);
				break;
			case MESSAGE_DRAW_SELECTED_MARK:
				surfaceView.redraw(canvas);
				
				ArrayList<Block> blockList = unpackBlockMessage(msg);
				Log.v("asdf", blockList.size()+"");
				for (int i = 0; i < blockList.size(); i++) {
					surfaceView.drawSelectedMark(canvas, blockList.get(i));
				}
				break;
			}
			
		} finally {
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	/** 把发来的方块解包。*/
	private ArrayList<Block> unpackBlockMessage(Message msg) {
		Bundle data = msg.getData();
		ArrayList<Block> blockList = (ArrayList<Block>) data.getSerializable("block");
		return blockList;
	}

}
