package com.mlxy.poping;

import java.util.ArrayList;

public class Algorithm {
	/** �ѱ��ݹ���ķ��顣*/
	private ArrayList<Block> checkedBlocks;
	/** ��ͬ��ɫ׼�����صķ��顣*/
	private ArrayList<Block> blocksInSameColor;
	
	/** ��ȡ�����뵱ǰ������ɫ��ͬ��β������һƬ������б�*/
	public ArrayList<Block> getBlocksInSameColor(Block selectedBlock) {
		// ��ʼ�����������б�
		checkedBlocks = new ArrayList<Block>();
		blocksInSameColor = new ArrayList<Block>();
		
		// ִ�еݹ顣
		checkFourSides(selectedBlock);
		return blocksInSameColor;
	}
	
	/** �ݹ飬��ͣ������档*/
	private void checkFourSides(Block block) {
		// �����ǰ�����ѱ���������������ǰ�ݹ顣
		if (isChecked(block)) {
			return;
		}
		
		// ��ǵ�ǰ����Ϊ�ѱ�������
		checkedBlocks.add(block);
		// ����ǰ�������ͬɫ�б�
		blocksInSameColor.add(block);
		
		// ����������ҵķ��顣
		checkUp(block);
		checkDown(block);
		checkLeft(block);
		checkRight(block);
	}
	
	private void checkUp(Block block) {
		// Խ��(�Ϸ������ڷ���)��������
		if ((block.getRow()-1) < 0) {
			return;
		}
		
		// ��ȡ�Ϸ����顣
		Block blockUpside = GameSurfaceView.blockList[block.getRow()-1][block.getColumn()];
		
		// ������鲻���ڻ�����ɫ��ͬ��������
		if (blockUpside == null || blockUpside.getColor() != block.getColor()) {
			return;
		}
		
		// ��һ�εݹ顣
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
	
	/** ��������������������÷֡�*/
	public long calcDestroyScore(int destroyedBlocks) {
		long score = destroyedBlocks * destroyedBlocks * 5;
		return score;
	}
	
	/** ����ʣ�෽��������÷֡�*/
	public long calcRemainedScore(int remainedBlocks) {
		long score = 2000 - remainedBlocks * 100;
		if (score < 0) {
			return 0;
		} else {
			return score;
		}
	}
	
	/** ���ݹؿ��������������÷֡�*/
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
