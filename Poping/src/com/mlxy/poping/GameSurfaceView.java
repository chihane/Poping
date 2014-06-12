package com.mlxy.poping;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** ��Ϸ�����ࡣ */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	/** ÿ�еķ������� */
	public final static int BLOCKS_PER_ROW = 10;
	/** ÿ�еķ������� */
	public final static int BLOCKS_PER_COLUMN = 10;

	private int screenWidth;
	private int screenHeight;

	/** һ������ı߳��� */
	private int sideLengthOfBlock;

	/** ���ߵ������ꡣ */
	private int topSidePosition;

	/** ������з���Ķ�ά���顣 */
	public final static Block[][] blockList = new Block[BLOCKS_PER_ROW][BLOCKS_PER_COLUMN];

	/** ��ͼ�û��ʡ� */
	private Paint paint;
	
	/** ���������þ���� */
	private UpdateHandler updateHandler;
	
	/** ������ķ��顣*/
	private Block selectedBlock;
	/** ��ѡ�еķ����顣*/
	private ArrayList<Block> selectedBlocks;

	/** ���캯���� */
	public GameSurfaceView(Context context) {
		super(context);
		this.getHolder().addCallback(this);
		
		paint = new Paint();
		updateHandler = new UpdateHandler(this);
		
		selectedBlock = null;
		selectedBlocks = null;
		
		initBlockList();
	}
	
	/** ��ʼ�������б� */
	private void initBlockList() {
		int id = 0;
		for (int i = 0; i < BLOCKS_PER_ROW; i++) {
			for (int j = 0; j < BLOCKS_PER_COLUMN; j++) {
				// �����������ȡһ����ɫ���뵱ǰ����λ�á�
				blockList[i][j] = new Block(id, getRandomColor(), i, j);
				id++;
			}
		}
	}

	/** ��Block����ɫ�б��������ȡһ����ɫ�� */
	private int getRandomColor() {
		int index = (int) (Math.random() * Block.COLOR_LIST.length);
		return Block.COLOR_LIST[index];
	}

	/** Surface�ĳ�ʼ���� */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// ��ȡ��Ļ�ֱ��ʡ�
		Rect surfaceFrame = holder.getSurfaceFrame();
		this.screenWidth = surfaceFrame.width();
		this.screenHeight = surfaceFrame.height();

		this.sideLengthOfBlock = screenWidth / BLOCKS_PER_ROW;

		this.topSidePosition = screenHeight - sideLengthOfBlock * BLOCKS_PER_COLUMN;

		updateHandler.sendEmptyMessage(UpdateHandler.MESSAGE_REDRAW_ALL);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	/** �����������ݡ� */
	public void redraw(Canvas canvas) {
		// �����塣
		canvas.drawColor(Color.WHITE);

		// �������λ������з��顣
		for (int i = 0; i < BLOCKS_PER_ROW; i++) {
			for (int j = 0; j < BLOCKS_PER_COLUMN; j++) {
				if (blockList[i][j] == null) {
					continue;
				}
				drawBlock(canvas, blockList[i][j]);
			}
		}
	}

	/** ���Ƴ�һ�������ķ��顣 */
	private void drawBlock(Canvas canvas, Block block) {
		// ���û�����ɫ��
		paint.setColor(block.getColor());

		// ȡ�������Ե���ϱ�Ե���꣬ȷ�����ζ���
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate,
				topCoordinate,
				leftCoordinate + sideLengthOfBlock,
				topCoordinate + sideLengthOfBlock);

		// ���Ʒ��顣
		canvas.drawRect(r, paint);
	}
	
	/** ��ָ���ķ����ϻ���ѡ�б�־�� */
	public void drawSelectedMark(Canvas canvas, Block block) {
		paint.setColor(Color.BLACK);
		
		// ȡ�������Ե���ϱ�Ե���ꡣ
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		
		canvas.drawCircle(leftCoordinate + sideLengthOfBlock / 2, topCoordinate + sideLengthOfBlock / 2,
				sideLengthOfBlock / 6, paint);
	}
	
	/** �����Ļ�¼���*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		selectedBlock = getTouchedBlock(event.getX(), event.getY());
		
		// ��Ӧλ��û�з����ֱ�ӷ��ء�
		if (selectedBlock == null) {
			return super.onTouchEvent(event);
		}
		
		// �����ѡ�������ǿյģ����߱�����ķ��鲻����ѡ�������
		if (selectedBlocks == null || !selectedBlocks.contains(selectedBlock)) {
			// ��ȡͬɫ�����б�������͸�Handler���»��档
			selectedBlocks = Algorithm.getBlocksInSameColor(selectedBlock);
			Message msg = packBlockMessages(selectedBlocks, UpdateHandler.MESSAGE_DRAW_SELECTED_MARK);
			updateHandler.sendMessage(msg);
		} else {
			// TODO �����߼�
			destroyBlocks(selectedBlocks);
			//dropBlocks();
			
			// ���ѡ�з����б�
			selectedBlocks = null;
		}
		return super.onTouchEvent(event);
	}
	
	/** ����������ڽ���Handler���� */
	private Message packBlockMessages(ArrayList<Block> blockList, int what) {
		Bundle data = new Bundle();
		data.putSerializable("block", blockList);
		
		Message msg = new Message();
		msg.setData(data);
		msg.what = UpdateHandler.MESSAGE_DRAW_SELECTED_MARK;
		
		return msg;
	}
	
	/** ���봥��λ�ã�������Ӧλ�õķ��顣 */
	private Block getTouchedBlock(float posX, float posY) {
		// ����㵽����Ļ����Ĳ��֡�
		if (posY < getTopSidePosition()) {
			return null;
		}
		
		int i = ((int) posY - getTopSidePosition()) / getSideLengthOfBlock();
		int j = (int) posX / getSideLengthOfBlock();
		
		return blockList[i][j];
	}
	
	/** ���ܷ����б��������б�����еķ��顣*/
	private void destroyBlocks(ArrayList<Block> blockList) {
		Block blockToDestroy = null;
		
		for (int i = 0; i < blockList.size(); i++) {
			blockToDestroy = blockList.get(i);
			GameSurfaceView.blockList[blockToDestroy.getRow()][blockToDestroy.getColumn()] = null;
		}
		
		// ������֮���ػ滭�档
		updateHandler.sendEmptyMessage(UpdateHandler.MESSAGE_REDRAW_ALL);
	}
	
	/** ���շ���ȫ�����䡣*/
	private void dropBlocks() {
		// �����ң����µ��ϵر����ܷ����б�
		for (int column = 0; column < BLOCKS_PER_ROW; column++) {
			for (int row = BLOCKS_PER_COLUMN - 1; row >= 0; row--) {
				// �����ǰ�����ǿ�ֵ��
				if (blockList[row][column] == null) {
					// �ʹӵ�ǰλ�����ϱ�����
					for (int i = row-1; i >= 0; i--) {
						// ֱ���ҵ��˵�һ���ǿյ�λ�ã���
						if (blockList[i][column] != null) {
							// �����λ���ϵķ���ŵ��ղŷ��ֵĿ�λ���ϡ�
							blockList[row][column] = blockList[i][column];
							// Ȼ���������÷����λ�����ԡ�
							blockList[row][column].setRow(row);
							blockList[row][column].setColumn(column);
							// Ȼ������ѭ��������һ����λ�á�
							break;
						}
					}
				}
			}
		}
		
		// ������֮���ػ滭�档
		updateHandler.sendEmptyMessage(UpdateHandler.MESSAGE_REDRAW_ALL);
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getSideLengthOfBlock() {
		return sideLengthOfBlock;
	}

	public int getTopSidePosition() {
		return topSidePosition;
	}
}
