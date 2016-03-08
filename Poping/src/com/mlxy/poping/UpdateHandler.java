package com.mlxy.poping;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;

/** ���ڸ��»������ݵľ���� */
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
			// ����������
			canvas = surfaceHolder.lockCanvas();
			
			switch (msg.what) {
			// �ػ����з��顣
			case MESSAGE_REDRAW_ALL:
				surfaceView.redraw(canvas);
				break;
				
			// �⿪�����İ����ڰ����ķ����ϻ���ѡ�б�ǡ�
			case MESSAGE_DRAW_SELECTED_MARK:
				surfaceView.redraw(canvas);
				
				ArrayList<Block> blockList = unpackBlockMessage(msg);
				
				for (int i = 0; i < blockList.size(); i++) {
					surfaceView.drawSelectedMark(canvas, blockList.get(i));
				}
				break;
			}
			
		} finally {
			// ����������
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	/** �ѷ����ķ�������*/
	private ArrayList<Block> unpackBlockMessage(Message msg) {
		Bundle data = msg.getData();
		@SuppressWarnings("unchecked")
		ArrayList<Block> blockList = (ArrayList<Block>) data.getSerializable("block");
		return blockList;
	}

}
