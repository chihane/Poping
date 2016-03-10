package chihane.poping;

import java.util.ArrayList;

public class Algorithm {
	private static ArrayList<Block> checkedBlocks;
	private static ArrayList<Block> blocksInSameColor;

	public static ArrayList<Block> getBlocksInSameColor(Block[][] blockList, Block selectedBlock) {
		checkedBlocks = new ArrayList<>();
		blocksInSameColor = new ArrayList<>();

		checkFourSides(blockList, selectedBlock);
		return blocksInSameColor;
	}

	private static void checkFourSides(Block[][] blockList, Block block) {
		// 如果当前方块已被检查过，则跳出当前递归。
		if (checkedBlocks.contains(block)) {
			return;
		}

		// 标记当前方块为已被检查过。
		checkedBlocks.add(block);
		// 将当前方块加入同色列表。
		blocksInSameColor.add(block);

		// 检查上下左右的方块。
		checkUp(blockList, block);
		checkDown(blockList, block);
		checkLeft(blockList, block);
		checkRight(blockList, block);
	}

	private static void checkUp(Block[][] blockList, Block block) {
		// 越界(上方不存在方块)则跳出。
		if ((block.getRow()-1) < 0) {
			return;
		}

		// 获取上方方块。
		Block blockUpside = blockList[block.getRow()-1][block.getColumn()];

		// 如果方块不存在或者颜色不同就跳出。
		if (blockUpside == null || blockUpside.getColor() != block.getColor()) {
			return;
		}

		// 下一次递归。
		checkFourSides(blockList, blockUpside);
	}

	private static void checkDown(Block[][] blockList, Block block) {
		if ((block.getRow()+1) >= GamePad.BLOCKS_PER_COLUMN) {
			return;
		}

		Block blockDownside = blockList[block.getRow()+1][block.getColumn()];

		if (blockDownside == null || blockDownside.getColor() != block.getColor()) {
			return;
		}

		checkFourSides(blockList, blockDownside);
	}

	private static void checkLeft(Block[][] blockList, Block block) {
		if ((block.getColumn()-1) < 0) {
			return;
		}

		Block blockLeftside = blockList[block.getRow()][block.getColumn()-1];

		if (blockLeftside == null || blockLeftside.getColor() != block.getColor()) {
			return;
		}

		checkFourSides(blockList, blockLeftside);
	}

	private static void checkRight(Block[][] blockList, Block block) {
		if ((block.getColumn()+1) >= GamePad.BLOCKS_PER_ROW) {
			return;
		}

		Block blockRightside = blockList[block.getRow()][block.getColumn()+1];

		if (blockRightside == null || blockRightside.getColor() != block.getColor()) {
			return;
		}

		checkFourSides(blockList, blockRightside);
	}

	public static long calcDestroyScore(int destroyedBlocks) {
		return destroyedBlocks * destroyedBlocks * 5;
	}

	public static long calcRemainedScore(int remainedBlocks) {
		long score = 2000 - remainedBlocks * 100;
		if (score < 0) {
			return 0;
		} else {
			return score;
		}
	}

	public static long calcRequiredScore(long level) {
		long score;

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
