package CLI;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TextBlockConcatenator {

    private final List<String> listOfLeftBlockLines;
    private final int heightOfLeftBlock;
    private final int widthOfLeftBlock;
    private final List<String> listOfRightBlockLines;
    private final int heightOfRightBlock;
    private final String pad;
    private final int spaceBetweenBlocks;


    public TextBlockConcatenator(String leftBlock, String rightBlock, String pad, int spaceBetweenBlocks) {
        this.listOfLeftBlockLines = leftBlock.lines().toList();
        this.heightOfLeftBlock = listOfLeftBlockLines.size();
        this.widthOfLeftBlock = leftBlock.lines().mapToInt(String::length).max().orElse(0);
        this.listOfRightBlockLines = rightBlock.lines().toList();
        this.heightOfRightBlock = listOfRightBlockLines.size();
        this.pad = pad;
        this.spaceBetweenBlocks = spaceBetweenBlocks;
    }

    public String concatenate() {
        return IntStream.range(0, resultingBlockHeight())
                .mapToObj(lineIndex -> resultingBlockLine(lineIndex, pad))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String resultingBlockLine(int lineIndex, String pad) {
        String leftLine = lineIndex < heightOfLeftBlock ? listOfLeftBlockLines.get(lineIndex) : pad;
        String rightLine = lineIndex < heightOfRightBlock ? listOfRightBlockLines.get(lineIndex) : pad;
        return justifyLineOfLeftBlock(leftLine) + pad.repeat(spaceBetweenBlocks) + rightLine;
    }

    private int resultingBlockHeight() {
        return Math.max(heightOfLeftBlock, heightOfRightBlock);
    }

    private String justifyLineOfLeftBlock(String line) {
        if (listOfLeftBlockLines.isEmpty()) {
            return "";
        } else {
            return String.format("%-" + widthOfLeftBlock + "s", line);
        }
    }
}