package chihane.poping;

import java.util.ArrayList;

public class GamePad {
    public final static int BLOCKS_PER_ROW = 10;
    public final static int BLOCKS_PER_COLUMN = 10;

    public Block[][] blockMatrix = new Block[BLOCKS_PER_COLUMN][BLOCKS_PER_ROW];
    private ArrayList<Block> selectedBlocks;

    public GamePad() {
        initBlockMatrix();
    }

    private void initBlockMatrix() {
        int id = 0;
        for (int column = 0; column < BLOCKS_PER_ROW; column++) {
            for (int row = 0; row < BLOCKS_PER_COLUMN; row++) {
                // 给方块随机抽取一个颜色塞入当前遍历位置。
                blockMatrix[row][column] = new Block(id, getRandomColor(), row, column);
                id++;
            }
        }
    }

    private int getRandomColor() {
        int index = (int) (Math.random() * Block.COLOR_LIST.length);
        return Block.COLOR_LIST[index];
    }

    public int destroySelectedBlocks() {
        Block blockToDestroy;
        for (int i = 0; i < selectedBlocks.size(); i++) {
            blockToDestroy = selectedBlocks.get(i);
            blockMatrix[blockToDestroy.getRow()][blockToDestroy.getColumn()] = null;
        }

        int destroyedBlocks = selectedBlocks.size();
        selectedBlocks.clear();
        return destroyedBlocks;
    }

    private void dropBlocks() {
        // 从左到右，从下到上地遍历总方块列表。
        for (int column = 0; column < BLOCKS_PER_ROW; column++) {
            for (int row = BLOCKS_PER_COLUMN - 1; row >= 0; row--) {
                // 如果当前方块是空值，
                if (blockMatrix[row][column] == null) {
                    // 就从当前位置向上遍历。
                    for (int i = row-1; i >= 0; i--) {
                        // 直到找到了第一个非空的位置，则
                        if (blockMatrix[i][column] != null) {
                            // 把这个位置上的方块移动到刚才发现的空位置上。
                            blockMatrix[row][column] = blockMatrix[i][column];
                            blockMatrix[i][column] = null;
                            // 然后重新设置方块的位置属性。
                            blockMatrix[row][column].setRow(row);
                            blockMatrix[row][column].setColumn(column);
                            // 然后跳出循环，找下一个空位置。
                            break;
                        }
                    }
                }
            }
        }
    }

    private void alignLeft() {
        int numOfEmptyColumn = 0;
        int firstEmptyColumn = -1;
        boolean hasEmptyColumn = false;

        // 统计出两片非空列中空列的数量。
        outerFor: for (int column = 0; column < GamePad.BLOCKS_PER_ROW - 1; column++) {
            // 找到第一个空列。
            if (blockMatrix[GamePad.BLOCKS_PER_COLUMN-1][column] == null) {
                firstEmptyColumn = column;
                for (int c = column; c < GamePad.BLOCKS_PER_ROW - 1; c++) {
                    // 统计空列数量。
                    if (blockMatrix[GamePad.BLOCKS_PER_COLUMN-1][c] == null) {
                        numOfEmptyColumn++;
                    }
                    // 找到后面的非空列。
                    if (blockMatrix[GamePad.BLOCKS_PER_COLUMN-1][c] != null) {
                        hasEmptyColumn = true;
                        break outerFor;
                    }
                }
            }
        }

        // 如果没必要左移就直接跳出方法。
        if (!hasEmptyColumn) {
            return;
        }

        // 从空列开始，依次向空列数之后的列要方块。
        for (int column = firstEmptyColumn; column < GamePad.BLOCKS_PER_ROW - numOfEmptyColumn; column++) {
            for (int row = 0; row < GamePad.BLOCKS_PER_COLUMN; row++) {
                // 如果后面的也是空位置，那两边都是空位置，直接跳过。
                if (blockMatrix[row][column + numOfEmptyColumn] == null) {
                    continue;
                }

                // 把后面的方块移动到前面来。
                blockMatrix[row][column] = blockMatrix[row][column + numOfEmptyColumn];
                blockMatrix[row][column + numOfEmptyColumn] = null;
                // 更新方块属性。
                blockMatrix[row][column].setRow(row);
                blockMatrix[row][column].setColumn(column);
            }
        }
    }

    public boolean isDead() {
        ArrayList<Block> listToCheck;
        for (int column = 0; column < GamePad.BLOCKS_PER_ROW-1; column++) {
            for (int row = 0; row < GamePad.BLOCKS_PER_COLUMN; row++) {
                if (blockMatrix[row][column] == null) {
                    continue;
                }

                listToCheck = Algorithm.getBlocksInSameColor(blockMatrix, blockMatrix[row][column]);
                if (listToCheck.size() > 1){
                    return false;
                }
            }
        }

        return true;
    }

    public int countRemainedBlocks() {
        int count = 0;

        for (int i = 0; i < GamePad.BLOCKS_PER_COLUMN; i++) {
            for (int j = 0; j < GamePad.BLOCKS_PER_ROW; j++) {
                if (blockMatrix[i][j] != null) {
                    count++;
                }
            }
        }

        return count;
    }

    public boolean noBlockSelected() {
        return selectedBlocks == null || selectedBlocks.size() == 0;
    }

    public ArrayList<Block> selectBlockInSameColor(Block rootBlock) {
        ArrayList<Block> blocksInSameColor = Algorithm.getBlocksInSameColor(blockMatrix, rootBlock);
        if (blocksInSameColor.size() > 1) {
            selectedBlocks = blocksInSameColor;
        } else {
            selectedBlocks = new ArrayList<>();
        }
        return selectedBlocks;
    }

    public void sortBlocks() {
        dropBlocks();
        alignLeft();
    }
}
