package CLI;

import java.util.stream.Collectors;

class TextBlock {

    private String text;
    private final int height;
    private int width;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void padOnTheRightWithSpaces() {
        this.text = text.lines()
                .map(line -> String.format("%-" + width + "s", line))
                .collect(Collectors.joining(System.lineSeparator()));
        this.width = computeBlockWidth();
    }

    public String getLine(int index) {
        return text.lines().toList().get(index);
    }

}
