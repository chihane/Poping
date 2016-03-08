package chihane.poping;

import java.util.ArrayList;

public class Algorithm {
	/** 已被递归过的方块。*/
	private ArrayList<Block> checkedBlocks;
	/** 相同颜色准备返回的方块。*/
	private ArrayList<Block> blocksInSameColor;

	/** 获取存有与当前方块颜色相同首尾相连的一片方块的列表。*/
	public ArrayList<Block> getBlocksInSameColor(Block selectedBlock) {
		// 初始化两个缓存列表。
		checkedBlocks = new ArrayList<Block>();
		blocksInSameColor = new ArrayList<Block>();

		// 执行递归。
		checkFourSides(selectedBlock);
		return blocksInSameColor;
	}

	/** 递归，不停检查四面。*/
	private void checkFourSides(Block block) {
		// 如果当前方块已被检查过，则跳出当前递归。
		if (isChecked(block)) {
			return;
		}

		// 标记当前方块为已被检查过。
		checkedBlocks.add(block);
		// 将当前方块加入同色列表。
		blocksInSameColor.add(block);

		// 检查上下左右的方块。
		checkUp(block);
		checkDown(block);
		checkLeft(block);
		checkRight(block);
	}

	private void checkUp(Block block) {
		// 越界(上方不存在方块)则跳出。
		if ((block.getRow()-1) < 0) {
			return;
		}

		// 获取上方方块。
		Block blockUpside = GameSurfaceView.blockList[block.getRow()-1][block.getColumn()];

		// 如果方块不存在或者颜色不同就跳出。
		if (blockUpside == null || blockUpside.getColor() != block.getColor()) {
			return;
		}

		// 下一次递归。
		checkFourSides(blockUpside);
	}

	private void checkDown(Block block) {
		if ((block.getRow()+1) >= GameSurfaceView.BLOCKS_PER_COLUMN) {
			return;
		}

		Block blockDownside = GameSurfaceView.blockList[block.getRow()+1][block.getColumn()];

		if (blockDownside == null || blockDownside.getColor() != block.getColor()) {
			return;
		}

		checkFourSides(blockDownside);
	}

	private void checkLeft(Block block) {
		if ((block.getColumn()-1) < 0) {
			return;
		}

		Block blockLeftside = GameSurfaceView.blockList[block.getRow()][block.getColumn()-1];

		if (blockLeftside == null || blockLeftside.getColor() != block.getColor()) {
			return;
		}

		checkFourSides(blockLeftside);
	}

	private void checkRight(Block block) {
		if ((block.getColumn()+1) >= GameSurfaceView.BLOCKS_PER_ROW) {
			return;
		}

		Block blockRightside = GameSurfaceView.blockList[block.getRow()][block.getColumn()+1];

		if (blockRightside == null || blockRightside.getColor() != block.getColor()) {
			return;
		}

		checkFourSides(blockRightside);
	}

	private boolean isChecked(Block block) {
		return checkedBlocks.contains(block);
	}

	/** 根据消除方块数量计算得分。*/
	public long calcDestroyScore(int destroyedBlocks) {
		long score = destroyedBlocks * destroyedBlocks * 5;
		return score;
	}

	/** 根据剩余方块数计算得分。*/
	public long calcRemainedScore(int remainedBlocks) {
		long score = 2000 - remainedBlocks * 100;
		if (score < 0) {
			return 0;
		} else {
			return score;
		}
	}

	/** 根据关卡数计算过关所需得分。*/
	public long calcRequiredScore(long level) {
		long score = 0;

		if (level < 11) {
			score = level * 2000;
		} else if (level < 21) {
			score = 20000 + (level-10) * 3000;
		} else {
			score = 50000 + (level-20) * 4000;
		}

		return score;
	}
}
