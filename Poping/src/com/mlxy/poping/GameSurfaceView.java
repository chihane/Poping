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

/** 游戏界面类。 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	/** 每行的方块数。 */
	public final static int BLOCKS_PER_ROW = 10;
	/** 每列的方块数。 */
	public final static int BLOCKS_PER_COLUMN = 10;

	private int screenWidth;
	private int screenHeight;

	/** 一个方块的边长。 */
	private int sideLengthOfBlock;

	/** 顶边的纵坐标。 */
	private int topSidePosition;

	/** 存放所有方块的二维数组。 */
	public final static Block[][] blockList = new Block[BLOCKS_PER_ROW][BLOCKS_PER_COLUMN];

	/** 绘图用画笔。 */
	private Paint paint;
	
	/** 更新内容用句柄。 */
	private UpdateHandler updateHandler;
	
	/** 被点击的方块。*/
	private Block selectedBlock;
	/** 被选中的方块组。*/
	private ArrayList<Block> selectedBlocks;

	/** 构造函数。 */
	public GameSurfaceView(Context context) {
		super(context);
		this.getHolder().addCallback(this);
		
		paint = new Paint();
		updateHandler = new UpdateHandler(this);
		
		selectedBlock = null;
		selectedBlocks = null;
		
		initBlockList();
	}
	
	/** 初始化方块列表。 */
	private void initBlockList() {
		int id = 0;
		for (int i = 0; i < BLOCKS_PER_ROW; i++) {
			for (int j = 0; j < BLOCKS_PER_COLUMN; j++) {
				// 给方块随机抽取一个颜色塞入当前遍历位置。
				blockList[i][j] = new Block(id, getRandomColor(), i, j);
				id++;
			}
		}
	}

	/** 从Block的颜色列表里随机抽取一个颜色。 */
	private int getRandomColor() {
		int index = (int) (Math.random() * Block.COLOR_LIST.length);
		return Block.COLOR_LIST[index];
	}

	/** Surface的初始化。 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 获取屏幕分辨率。
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

	/** 绘制所有内容。 */
	public void redraw(Canvas canvas) {
		// 清空面板。
		canvas.drawColor(Color.WHITE);

		// 重新依次画出所有方块。
		for (int i = 0; i < BLOCKS_PER_ROW; i++) {
			for (int j = 0; j < BLOCKS_PER_COLUMN; j++) {
				if (blockList[i][j] == null) {
					continue;
				}
				drawBlock(canvas, blockList[i][j]);
			}
		}
	}

	/** 绘制出一个给定的方块。 */
	private void drawBlock(Canvas canvas, Block block) {
		// 设置画笔颜色。
		paint.setColor(block.getColor());

		// 取方块左边缘和上边缘坐标，确定矩形对象。
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate,
				topCoordinate,
				leftCoordinate + sideLengthOfBlock,
				topCoordinate + sideLengthOfBlock);

		// 绘制方块。
		canvas.drawRect(r, paint);
	}
	
	/** 在指定的方块上绘制选中标志。 */
	public void drawSelectedMark(Canvas canvas, Block block) {
		paint.setColor(Color.BLACK);
		
		// 取方块左边缘和上边缘坐标。
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		
		canvas.drawCircle(leftCoordinate + sideLengthOfBlock / 2, topCoordinate + sideLengthOfBlock / 2,
				sideLengthOfBlock / 6, paint);
	}
	
	/** 点击屏幕事件。*/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		selectedBlock = getTouchedBlock(event.getX(), event.getY());
		
		// 对应位置没有方块就直接返回。
		if (selectedBlock == null) {
			return super.onTouchEvent(event);
		}
		
		// 如果已选方块组是空的，或者被点击的方块不在已选方块组里。
		if (selectedBlocks == null || !selectedBlocks.contains(selectedBlock)) {
			// 获取同色方块列表，打包发送给Handler更新画面。
			selectedBlocks = Algorithm.getBlocksInSameColor(selectedBlock);
			Message msg = packBlockMessages(selectedBlocks, UpdateHandler.MESSAGE_DRAW_SELECTED_MARK);
			updateHandler.sendMessage(msg);
		} else {
			// TODO 消除逻辑
			destroyBlocks(selectedBlocks);
			//dropBlocks();
			
			// 清空选中方块列表。
			selectedBlocks = null;
		}
		return super.onTouchEvent(event);
	}
	
	/** 打包方块用于交给Handler处理。 */
	private Message packBlockMessages(ArrayList<Block> blockList, int what) {
		Bundle data = new Bundle();
		data.putSerializable("block", blockList);
		
		Message msg = new Message();
		msg.setData(data);
		msg.what = UpdateHandler.MESSAGE_DRAW_SELECTED_MARK;
		
		return msg;
	}
	
	/** 输入触摸位置，返回相应位置的方块。 */
	private Block getTouchedBlock(float posX, float posY) {
		// 如果点到了屏幕上面的部分。
		if (posY < getTopSidePosition()) {
			return null;
		}
		
		int i = ((int) posY - getTopSidePosition()) / getSideLengthOfBlock();
		int j = (int) posX / getSideLengthOfBlock();
		
		return blockList[i][j];
	}
	
	/** 从总方块列表里消除列表参数中的方块。*/
	private void destroyBlocks(ArrayList<Block> blockList) {
		Block blockToDestroy = null;
		
		for (int i = 0; i < blockList.size(); i++) {
			blockToDestroy = blockList.get(i);
			GameSurfaceView.blockList[blockToDestroy.getRow()][blockToDestroy.getColumn()] = null;
		}
		
		// 消除完之后重绘画面。
		updateHandler.sendEmptyMessage(UpdateHandler.MESSAGE_REDRAW_ALL);
	}
	
	/** 悬空方块全部下落。*/
	private void dropBlocks() {
		// 从左到右，从下到上地遍历总方块列表。
		for (int column = 0; column < BLOCKS_PER_ROW; column++) {
			for (int row = BLOCKS_PER_COLUMN - 1; row >= 0; row--) {
				// 如果当前方块是空值，
				if (blockList[row][column] == null) {
					// 就从当前位置向上遍历。
					for (int i = row-1; i >= 0; i--) {
						// 直到找到了第一个非空的位置，则
						if (blockList[i][column] != null) {
							// 把这个位置上的方块放到刚才发现的空位置上。
							blockList[row][column] = blockList[i][column];
							// 然后重新设置方块的位置属性。
							blockList[row][column].setRow(row);
							blockList[row][column].setColumn(column);
							// 然后跳出循环，找下一个空位置。
							break;
						}
					}
				}
			}
		}
		
		// 下落完之后重绘画面。
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
