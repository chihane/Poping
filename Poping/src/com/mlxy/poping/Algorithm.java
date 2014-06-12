package com.mlxy.poping;

import java.util.ArrayList;

import android.util.Log;

public class Algorithm {
	private GameSurfaceView surfaceView;
	/** 总方块列表。*/
	private Block[][] blockList;
	
	/** 已被递归过的方块。*/
	private ArrayList<Block> checkedBlocks;
	/** 相同颜色准备返回的方块。*/
	private ArrayList<Block> blocksInSameColor;
	
	public Algorithm(GameSurfaceView surfaceView) {
		this.surfaceView = surfaceView;
		blockList = GameSurfaceView.blockList;
	}
	
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
		// 三种情况跳出当前递归：当前递归的位置超出边界，当前递归的方块已经被检查过，当前递归的方块不存在。
//		if (block.getRow() == 0 || block.getRow()  == GameSurfaceView.BLOCKS_PER_ROW - 1 ||
//		    block.getColumn() == 0 || block.getColumn() == GameSurfaceView.BLOCKS_PER_COLUMN - 1 ||
//		    this.isChecked(block) || block == null) {
//			return;
//		}
		if (this.isChecked(block) || block == null) {
			return;
		}
		
		// 标记当前方块为已被检查过。
		checkedBlocks.add(block);
		
		// TODO: 递归内容。
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
		
		// 颜色不同就跳出。
		if (blockUpside.getColor() != block.getColor()) {
			return;
		}
		
		blocksInSameColor.add(blockUpside);
		
		// 下一次递归。
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
