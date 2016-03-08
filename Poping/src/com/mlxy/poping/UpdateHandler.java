package com.mlxy.poping;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
			// 锁定画布。
			canvas = surfaceHolder.lockCanvas();
			
			switch (msg.what) {
			// 重绘所有方块。
			case MESSAGE_REDRAW_ALL:
				surfaceView.redraw(canvas);
				break;
				
			// 解开发来的包，在包含的方块上画上选中标记。
			case MESSAGE_DRAW_SELECTED_MARK:
				surfaceView.redraw(canvas);
				
				ArrayList<Block> blockList = unpackBlockMessage(msg);
				
				for (int i = 0; i < blockList.size(); i++) {
					surfaceView.drawSelectedMark(canvas, blockList.get(i));
				}
				break;
			}
			
		} finally {
			// 解锁画布。
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	/** 把发来的方块解包。*/
	private ArrayList<Block> unpackBlockMessage(Message msg) {
		Bundle data = msg.getData();
		@SuppressWarnings("unchecked")
		ArrayList<Block> blockList = (ArrayList<Block>) data.getSerializable("block");
		return blockList;
	}

}
