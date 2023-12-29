package Utils.CLI;

import Heckmeck.Components.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SummaryTable {

    private final StringBuilder table;

    private final List<Player> playersList;

    private static final String newLine = System.lineSeparator();

    private enum column{
        PLAYER_NAME("Player"), LAST_PICKED_TILE("Top tile"), NUMBER_OF_TOTAL_WORM_COLLECTED("Worms");

        private final String name;

        column(String name) {
            this.name = name;
        }

        public String title() {
            return name;
        }

    }

    private static final String COLUMN_SEPARATOR = "|";

    private static final String ROW_SEPARATOR = "-";


    public SummaryTable(List<Player> playerList) {
        this.table = new StringBuilder();
        this.playersList = playerList;
    }

    public SummaryTable createHeader() {
        String columnTitlesRow = alignToColumn(column.PLAYER_NAME.title(), column.PLAYER_NAME) + COLUMN_SEPARATOR +
                alignToColumn(column.LAST_PICKED_TILE.title(), column.LAST_PICKED_TILE) + COLUMN_SEPARATOR +
                alignToColumn(column.NUMBER_OF_TOTAL_WORM_COLLECTED.title(), column.NUMBER_OF_TOTAL_WORM_COLLECTED);
        String headerAndBodySeparator = ROW_SEPARATOR.repeat(columnTitlesRow.length());
        table.append(columnTitlesRow)
                .append(newLine)
                .append(headerAndBodySeparator)
                .append(newLine);
        return this;
    }

    private String alignToColumn(String entry, column column) {
        int columnWidth = computeColumnWidth(column);
        int padding = (columnWidth - entry.length())/2;
        int paddingToAlignColumnDivider = columnWidth - 2*padding - entry.length();
        return String.format("%" + padding + "s%s%" + (padding+paddingToAlignColumnDivider) + "s",
                "", entry, "");
    }

    private int computeColumnWidth(column column) {
        return switch (column) {
            case PLAYER_NAME -> getLongestEntryInPlayerNameColumn().length() + 2;
            case LAST_PICKED_TILE, NUMBER_OF_TOTAL_WORM_COLLECTED -> column.title().length() + 2;
        };
    }

    private String getLongestEntryInPlayerNameColumn() {
        Stream<String> playerNames = playersList.stream().map(Player::getName);
        return Stream.concat(playerNames, Stream.of(column.PLAYER_NAME.title()))
                .max(Comparator.comparingInt(String::length))
                .orElse(column.PLAYER_NAME.title());
    }

    public SummaryTable fillWithPlayersData() {
        for (var player : playersList) {
            table.append(getPlayerInfoRow(player))
                    .append(newLine);
        }
        return this;
    }

    //TODO Aggiungere punteggio vermi del giocatore corrente nella tabella
    private String getPlayerInfoRow(Player player) {
        return alignToColumn(player.getName(), column.PLAYER_NAME) + COLUMN_SEPARATOR +
                alignToColumn(player.getTopTileInfo(), column.LAST_PICKED_TILE) + COLUMN_SEPARATOR +
                alignToColumn(String.valueOf(player.getWormScore()), column.NUMBER_OF_TOTAL_WORM_COLLECTED);
    }

    @Override
    public String toString() {
        return table.toString();
    }

}
