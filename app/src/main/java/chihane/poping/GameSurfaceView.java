package chihane.poping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private int sideLengthOfBlock;
	private int topSidePosition;

	private Paint paint;
	private Level level;
	private GamePad gamePad;

	public GameSurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
		paint = new Paint();
		level = new Level();
		gamePad = new GamePad();
	}

	private void newLevel() {
		level.next();
		gamePad = new GamePad();
		redraw();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Rect surfaceFrame = holder.getSurfaceFrame();
		sideLengthOfBlock = surfaceFrame.width() / GamePad.BLOCKS_PER_ROW;
		topSidePosition = surfaceFrame.height() - sideLengthOfBlock * GamePad.BLOCKS_PER_COLUMN;
		redraw();
	}

	private void redraw() {
		Canvas canvas = getHolder().lockCanvas();
		redraw(canvas);
		getHolder().unlockCanvasAndPost(canvas);
	}

	private void redraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);

		for (int column = 0; column < GamePad.BLOCKS_PER_ROW; column++) {
			for (int row = 0; row < GamePad.BLOCKS_PER_COLUMN; row++) {
				if (gamePad.blockMatrix[row][column] == null) {
					continue;
				}
				drawBlock(canvas, gamePad.blockMatrix[row][column]);
			}
		}

		paint.setColor(Color.BLACK);
		paint.setTextSize(40);
		canvas.drawText("Level: " + level.getLevel(), 50, 50, paint);
		canvas.drawText("Requirement: " + level.getRequiredScore(), 50, 100, paint);
		canvas.drawText("Score: " + level.getScore(), 50, 150, paint);
		paint.reset();
	}

	private void drawBlock(Canvas canvas, Block block) {
		paint.setColor(block.getColor());

		// 取方块左边缘和上边缘坐标，确定矩形对象。
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate,
				topCoordinate,
				leftCoordinate + sideLengthOfBlock,
				topCoordinate + sideLengthOfBlock);

		canvas.drawRect(r, paint);

		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);

		canvas.drawRect(r, paint);

		paint.reset();
	}

	private void drawSelectedMarks(ArrayList<Block> selectedBlocks) {
		Canvas canvas = getHolder().lockCanvas();
		redraw(canvas);
		for (Block block: selectedBlocks) {
			drawSelectedMark(canvas, block);
		}
		getHolder().unlockCanvasAndPost(canvas);
	}

	private void drawSelectedMark(Canvas canvas, Block block) {
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);

		// 取方块左边缘和上边缘坐标，确定矩形对象。
		int leftCoordinate = block.getColumn() * sideLengthOfBlock;
		int topCoordinate = topSidePosition + block.getRow() * sideLengthOfBlock;
		Rect r = new Rect(leftCoordinate,
				topCoordinate,
				leftCoordinate + sideLengthOfBlock,
				topCoordinate + sideLengthOfBlock);

		canvas.drawRect(r, paint);

		paint.reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onBlockTouched(getTouchedBlock(event.getX(), event.getY()));
		return super.onTouchEvent(event);
	}

	private void onBlockTouched(Block touchedBlock) {
		if (touchedBlock == null) {
			return;
		}

		if (gamePad.noBlockSelected()) {
			ArrayList<Block> blocksSelected = gamePad.selectBlockInSameColor(touchedBlock);
			drawSelectedMarks(blocksSelected);
		} else {
			int blocksDestroyed = gamePad.destroySelectedBlocks();
			level.gainScore(Algorithm.calcDestroyScore(blocksDestroyed));
			gamePad.sortBlocks();
			redraw();

			if (gamePad.isDead()) {
				int remainedBlocks = gamePad.countRemainedBlocks();
				level.gainScore(Algorithm.calcRemainedScore(remainedBlocks));

				if (level.hasEnoughScore()) {
					gameOver();
				} else {
					newLevel();
					Toast.makeText(this.getContext(),
							"剩余方块" + remainedBlocks + "个，得分" + level.getScore() + "，进入第" + level.getLevel() + "关",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private Block getTouchedBlock(float posX, float posY) {
		if (posY < topSidePosition) {
			return null;
		}

		int i = ((int) posY - topSidePosition) / sideLengthOfBlock;
		int j = (int) posX / sideLengthOfBlock;

		return gamePad.blockMatrix[i][j];
	}

	private void gameOver() {
		Activity parent = (Activity) this.getContext();

		Intent intent  = new Intent(parent, GameOverActivity.class);
		intent.putExtra("score", level.getScore());
		intent.putExtra("level", level.getLevel());
		parent.startActivity(intent);

		parent.finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
}
