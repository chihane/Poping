package com.mlxy.poping;

import java.util.ArrayList;

import android.util.Log;

public class Algorithm {
	private GameSurfaceView surfaceView;
	/** �ܷ����б�*/
	private Block[][] blockList;
	
	/** �ѱ��ݹ���ķ��顣*/
	private ArrayList<Block> checkedBlocks;
	/** ��ͬ��ɫ׼�����صķ��顣*/
	private ArrayList<Block> blocksInSameColor;
	
	public Algorithm(GameSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		blockList = GameSurfaceView.blockList;
	}
	
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
		// �������������ǰ�ݹ飺��ǰ�ݹ��λ�ó����߽磬��ǰ�ݹ�ķ����Ѿ�����������ǰ�ݹ�ķ��鲻���ڡ�
//		if (block.getRow() == 0 || block.getRow()  == GameSurfaceView.BLOCKS_PER_ROW - 1 ||
//		    block.getColumn() == 0 || block.getColumn() == GameSurfaceView.BLOCKS_PER_COLUMN - 1 ||
//		    this.isChecked(block) || block == null) {
//			return;
//		}
		if (this.isChecked(block) || block == null) {
			return;
		}
		
		// ��ǵ�ǰ����Ϊ�ѱ�������
		checkedBlocks.add(block);
		
		// TODO: �ݹ����ݡ�
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
		
		// ��ɫ��ͬ��������
		if (blockUpside.getColor() != block.getColor()) {
			return;
		}
		
		blocksInSameColor.add(blockUpside);
		
		// ��һ�εݹ顣
		checkFourSides(blockUpside);
	}
	
	private void checkDown(Block block) {
		if ((block.getRow()+1) >= GameSurfaceView.BLOCKS_PER_COLUMN) {
			return;
		}
		
		Block blockDownside = GameSurfaceView.blockList[block.getRow()+1][block.getColumn()];
		
		if (blockDownside.getColor() != block.getColor()) {
			return;
		}
		
		blocksInSameColor.add(blockDownside);
		
		checkFourSides(blockDownside);
	}

	private void checkLeft(Block block) {
		if ((block.getColumn()-1) < 0) {
			return;
		}
		
		Block blockLeftside = GameSurfaceView.blockList[block.getRow()][block.getColumn()-1];
		
		if (blockLeftside.getColor() != block.getColor()) {
			return;
		} 
		
		blocksInSameColor.add(blockLeftside);
		
		checkFourSides(blockLeftside);
	}

	private void checkRight(Block block) {
		if ((block.getColumn()+1) >= GameSurfaceView.BLOCKS_PER_ROW) {
			return;
		}
		
		Block blockRightside = GameSurfaceView.blockList[block.getRow()][block.getColumn()+1];
		
		if (blockRightside.getColor() != block.getColor()) {
			return;
		}
		
		blocksInSameColor.add(blockRightside);
		
		checkFourSides(blockRightside);
	}
	
	private boolean isChecked(Block block) {
		return checkedBlocks.contains(block);
	}
}
