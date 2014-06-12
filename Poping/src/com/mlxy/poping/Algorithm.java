package com.mlxy.poping;

import java.util.ArrayList;

public class Algorithm {
	/** �ѱ��ݹ���ķ��顣*/
	private static ArrayList<Block> checkedBlocks;
	/** ��ͬ��ɫ׼�����صķ��顣*/
	private static ArrayList<Block> blocksInSameColor;
	
	/** ��ȡ�����뵱ǰ������ɫ��ͬ��β������һƬ������б�*/
	public static ArrayList<Block> getBlocksInSameColor(Block selectedBlock) {
		// ��ʼ�����������б�
		checkedBlocks = new ArrayList<Block>();
		blocksInSameColor = new ArrayList<Block>();
		
		// ִ�еݹ顣
		checkFourSides(selectedBlock);
		
		return blocksInSameColor;
	}
	
	/** �ݹ飬��ͣ������档*/
	private static void checkFourSides(Block block) {
		// �����ǰ�����ѱ���������������ǰ�ݹ顣
		if (isChecked(block)) {
			return;
		}
		
		// ��ǵ�ǰ����Ϊ�ѱ�������
		checkedBlocks.add(block);
		
		// ����������ҵķ��顣
		checkUp(block);
		checkDown(block);
		checkLeft(block);
		checkRight(block);
	}
	
	private static void checkUp(Block block) {
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
		
		blocksInSameColor.add(blockUpside);
		
		// ��һ�εݹ顣
		checkFourSides(blockUpside);
	}
	
	private static void checkDown(Block block) {
		if ((block.getRow()+1) >= GameSurfaceView.BLOCKS_PER_COLUMN) {
			return;
		}
		
		Block blockDownside = GameSurfaceView.blockList[block.getRow()+1][block.getColumn()];
		
		if (blockDownside == null || blockDownside.getColor() != block.getColor()) {
			return;
		}
		
		blocksInSameColor.add(blockDownside);
		
		checkFourSides(blockDownside);
	}

	private static void checkLeft(Block block) {
		if ((block.getColumn()-1) < 0) {
			return;
		}
		
		Block blockLeftside = GameSurfaceView.blockList[block.getRow()][block.getColumn()-1];
		
		if (blockLeftside == null || blockLeftside.getColor() != block.getColor()) {
			return;
		} 
		
		blocksInSameColor.add(blockLeftside);
		
		checkFourSides(blockLeftside);
	}

	private static void checkRight(Block block) {
		if ((block.getColumn()+1) >= GameSurfaceView.BLOCKS_PER_ROW) {
			return;
		}
		
		Block blockRightside = GameSurfaceView.blockList[block.getRow()][block.getColumn()+1];
		
		if (blockRightside == null || blockRightside.getColor() != block.getColor()) {
			return;
		}
		
		blocksInSameColor.add(blockRightside);
		
		checkFourSides(blockRightside);
	}
	
	private static boolean isChecked(Block block) {
		return checkedBlocks.contains(block);
	}
}
