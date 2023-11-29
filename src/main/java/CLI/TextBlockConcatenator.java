package CLI;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TextBlockConcatenator {

    private final TextBlock leftTextBlock;
    private final TextBlock rightTextBlock;
    private static final String pad = " ";
    private final int spaceBetweenBlocks;


    public TextBlockConcatenator(String leftBlock, String rightBlock, int spaceBetweenBlocks) {
        this.leftTextBlock = new TextBlock(leftBlock);
        this.rightTextBlock = new TextBlock(rightBlock);
        this.spaceBetweenBlocks = spaceBetweenBlocks;
    }

    public String concatenate() {
        this.leftTextBlock.padOnTheRightWithSpaces();
        return IntStream.range(0, resultingBlockHeight())
                .mapToObj(this::resultingBlockLine)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String resultingBlockLine(int lineIndex) {
        String leftBlockFiller = pad.repeat(leftTextBlock.getWidth());
        String leftLine = lineIndex < leftTextBlock.getHeight() ? leftTextBlock.getLine(lineIndex) : leftBlockFiller;
        String rightLine = lineIndex < rightTextBlock.getHeight() ? rightTextBlock.getLine(lineIndex) : "";
        return leftLine + pad.repeat(spaceBetweenBlocks) + rightLine;
    }

    private int resultingBlockHeight() {
        return Math.max(leftTextBlock.getHeight(), rightTextBlock.getHeight());
    }

}
