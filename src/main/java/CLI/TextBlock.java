package CLI;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


class TextBlock {

    private String text;
    private int height;
    private int width;
    private static final String singleSpaceCharacter = " ";

    public TextBlock(String text) {
        this.text = text;
        this.width = computeBlockWidth();
        this.height = computeBlockHeight();
    }

    private int computeBlockWidth() {
        return this.text.lines().mapToInt(String::length).max().orElse(0);
    }

    private int computeBlockHeight() {
        return (int) this.text.lines().count();
    }

    public void padOnTheRightWithSpaces() {
        this.text = text.lines()
                .map(line -> String.format("%-" + width + "s", line))
                .collect(Collectors.joining(System.lineSeparator()));
        this.width = computeBlockWidth();
    }

    public TextBlock concatenateWith(TextBlock other, int spaceBetweenBlocks) {
        this.padOnTheRightWithSpaces();
        this.text= IntStream.range(0, resultingHeightFromConcatenationWith(other))
                .mapToObj(lineIndex -> resultingLineFromConcatenationWith(other, lineIndex, spaceBetweenBlocks))
                .collect(Collectors.joining(System.lineSeparator()));
        this.width = computeBlockWidth();
        this.height = computeBlockHeight();
        return this;
    }

    private String resultingLineFromConcatenationWith(TextBlock other, int lineIndex, int spaceBetweenBlocks) {
        String whiteSpaceFiller = singleSpaceCharacter.repeat(this.width);
        String leftLine = lineIndex < this.height ? this.getLine(lineIndex) : whiteSpaceFiller;
        String rightLine = lineIndex < other.height ? other.getLine(lineIndex) : "";
        return leftLine + singleSpaceCharacter.repeat(spaceBetweenBlocks) + rightLine;
    }

    private int resultingHeightFromConcatenationWith(TextBlock other) {
        return Math.max(this.height, other.height);
    }

    public String getLine(int index) {
        return text.lines().toList().get(index);
    }

    @Override
    public String toString() {
        return text;
    }
    
}
