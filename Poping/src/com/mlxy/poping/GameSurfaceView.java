package com.mlxy.poping;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

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
	
	/** ������*/
	private long score;
	/** �ؿ���*/
	private long level;

	/** ���캯���� */
	public GameSurfaceView(Context context) {
		super(context);
		this.getHolder().addCallback(this);
		
		paint = new Paint();
		updateHandler = new UpdateHandler(this);
		
		selectedBlock = null;
		selectedBlocks = null;
		
		score = 0;
		level = 0;
	}
	
	/** ��ʼ�µ�һ�ء�*/
	private void newLevel() {
		this.level++;
		initBlockList();
		updateHandler.sendEmptyMessage(UpdateHandler.MESSAGE_REDRAW_ALL);
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

		// ��ʼ��һ�ء�
		newLevel();
		Log.v("asdf", this.level+"");
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
		
		// ����������
		paint.setColor(Color.BLACK);
		paint.setTextSize(40);
		canvas.drawText("Score: " + this.score, 50, 100, paint);
		canvas.drawText("Level: " + this.level, 50, 50, paint);
		paint = new Paint();
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
		
		// �������û��ʷ��
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		
		// ���Ʒ���߿�
		canvas.drawRect(r, paint);
		
		// ��ʼ��������ʽ��
		paint = new Paint();
	}
	
	/** ��ָ���ķ����ϻ���ѡ�б�־�� */
	public void drawSelectedMark(Canvas canvas, Block block) {
		// ���û��ʷ��
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		
		// ȡ�������Ե���ϱ�Ե���꣬ȷ�����ζ���
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate,
				topCoordinate,
				leftCoordinate + sideLengthOfBlock,
				topCoordinate + sideLengthOfBlock);
		
		// ���Ʒ���߿�
		canvas.drawRect(r, paint);
		
		// �ָ�Ĭ�ϻ��ʷ��
		paint = new Paint();
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
			selectedBlocks = new Algorithm().getBlocksInSameColor(selectedBlock);
			
			// �б���ֻ��һ������Ļ������ӵ���
			if (selectedBlocks.size() <= 1) {
				return super.onTouchEvent(event);
			}
			
			Message msg = packBlockMessages(selectedBlocks, UpdateHandler.MESSAGE_DRAW_SELECTED_MARK);
			updateHandler.sendMessage(msg);
		} else {
			// �ƻ����顣
			destroyBlocks(selectedBlocks);
			// ���������ռ䡣
			dropBlocks();
			// �����������С�
			alignLeft();
			// �����жϡ�
			if (isDead()) {
				// �������ֵ÷֡�
				int remainedBlocks = countRemainedBlocks();
				long score = new Algorithm().calcRemainedScore(remainedBlocks);
				this.score += score;
				
				// ������һ�ء�
				newLevel();
				Toast.makeText(this.getContext(),
						"ʣ�෽��" + remainedBlocks + "�����÷�" + score + "�������" + this.level + "��",
						Toast.LENGTH_SHORT).show();
			}
			
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
		
		// ���·�����
		this.score += new Algorithm().calcDestroyScore(blockList.size());
		
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
							// �����λ���ϵķ����ƶ����ղŷ��ֵĿ�λ���ϡ�
							blockList[row][column] = blockList[i][column];
							blockList[i][column] = null;
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
	
	/** ���������������С�*/
	private void alignLeft() {
		int numOfEmptyColumn = 0;
		int firstEmptyColumn = -1;
		boolean hasEmptyColumn = false;
		
		// ͳ�Ƴ���Ƭ�ǿ����п��е�������
		outerFor: for (int column = 0; column < BLOCKS_PER_ROW - 1; column++) {
			// �ҵ���һ�����С�
			if (blockList[BLOCKS_PER_COLUMN-1][column] == null) {
				firstEmptyColumn = column;
				for (int c = column; c < BLOCKS_PER_ROW - 1; c++) {
					// ͳ�ƿ���������
					if (blockList[BLOCKS_PER_COLUMN-1][c] == null) {
						numOfEmptyColumn++;
					}
					// �ҵ�����ķǿ��С�
					if (blockList[BLOCKS_PER_COLUMN-1][c] != null) {
						hasEmptyColumn = true;
						break outerFor;
					}
				}
			}
		}
		
		// ���û��Ҫ���ƾ�ֱ������������
		if (!hasEmptyColumn) {
			return;
		}
		
		// �ӿ��п�ʼ�������������֮�����Ҫ���顣
		for (int column = firstEmptyColumn; column < BLOCKS_PER_ROW - numOfEmptyColumn; column++) {
			for (int row = 0; row < BLOCKS_PER_COLUMN; row++) {
				// ��������Ҳ�ǿ�λ�ã������߶��ǿ�λ�ã�ֱ��������
				if (blockList[row][column + numOfEmptyColumn] == null) {
					continue;
				}
				
				// �Ѻ���ķ����ƶ���ǰ������
				blockList[row][column] = blockList[row][column + numOfEmptyColumn];
				blockList[row][column + numOfEmptyColumn] = null;
				// ���·������ԡ�
				blockList[row][column].setRow(row);
				blockList[row][column].setColumn(column);
			}
		}
		// ������֮���ػ滭�档
		updateHandler.sendEmptyMessage(UpdateHandler.MESSAGE_REDRAW_ALL);
	}
	
	/** ���ּ�顣*/
	private boolean isDead() {
		// ���ڼ��ķ��顣
		ArrayList<Block> listToCheck;
		
		// �����ܷ����б�
		for (int column = 0; column < BLOCKS_PER_ROW-1; column++) {
			for (int row = 0; row < BLOCKS_PER_COLUMN; row++) {
				// ������ֵ��������
				if (blockList[row][column] == null) {
					continue;
				}
				
				// ����ͬɫ��飬��һ�������б�������������֡�
				listToCheck = new Algorithm().getBlocksInSameColor(blockList[row][column]);
				if (listToCheck.size() > 1){
					return false;
				}
			}
		}
		
		return true;
	}
	
	/** ͳ���б���ʣ�෽�顣*/
	private int countRemainedBlocks() {
		int count = 0;
		
		for (int i = 0; i < BLOCKS_PER_COLUMN; i++) {
			for (int j = 0; j < BLOCKS_PER_ROW; j++) {
				if (blockList[i][j] != null) {
					count++;
				}
			}
		}
		
		return count;
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
