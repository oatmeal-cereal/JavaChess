import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    Scanner scanner;

    Tile[][] board = new Tile[8][];

    Player black = new Player("B");
    Player white = new Player("W");

    Player currentPlayer = white;

    public Game(Scanner scanner) {
        this.CreateBoard();
        this.scanner = scanner;
    }

    public void SwapTiles(Tile tilesource, Tile tiledest) {
        System.out.printf("Source player before: %s %d %d\n", tilesource.getPiece().getPlayer().player, tilesource.getPiece().getX(), tilesource.getPiece().getY());
        System.out.printf("Destination player before: %s %d %d\n", tiledest.getPiece(), tiledest.x, tiledest.y);
        //if the destination tile has an opponent's piece overwrite it and decrease their no. pieces
        if (tiledest.getPiece() != null) {
            if (!(tiledest.getPiece().getPlayer().equals(this.currentPlayer))) {
                tiledest.getPiece().getPlayer().decPieces();
            }
        }
        this.board[tiledest.x][tiledest.y].setPiece(tilesource.getPiece());
        this.board[tilesource.x][tilesource.y].setPiece(null);
        System.out.printf("Source player after: %s %d %d\n", tilesource.getPiece(), tilesource.x, tilesource.y);
        System.out.printf("Destination player after: %s %d %d\n", tiledest.getPiece().getPlayer().player, tiledest.getPiece().getX(), tiledest.getPiece().getY());
    }

    public Tile ChooseSourceTile() {
        boolean validPiece = false;
        String letters = "ABCDEFGH";
        int pieceRow = 0;
        int pieceCol = 0;
        Tile selectedTile;
        while (!validPiece) {
            System.out.println("Choose a piece to move:");
            String piece = this.scanner.nextLine();
            if (piece.length() != 2) {
                continue;
            }
            
            if (letters.contains(String.valueOf(piece.charAt(0)))) {
                pieceCol = letters.indexOf(piece.charAt(0));
                try {
                    //seems to operate in ascii... 0 = 48, 7 = 55
                    pieceRow = (int) piece.charAt(1) - 48;
                    selectedTile = board[pieceRow][pieceCol];
                    //System.out.printf("selected tile piece position: %d %d\n", selectedTile.getPiece().getX(), selectedTile.getPiece().getY());
                    if (pieceRow >= 0 && pieceRow < 8) {
                        if (!(selectedTile.getPiece() == null)) {
                            if (selectedTile.getPiece().getPlayer().player.equals(this.currentPlayer.player)) {
                                selectedTile = board[pieceRow][pieceCol];
                                validPiece = true;
                            } else {
                                System.out.println("Not your piece.");
                            }
                        } else {
                            System.out.println("That's an empty tile.");
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        selectedTile = board[pieceRow][pieceCol];
        return selectedTile;
    }

    public Tile ChooseDestTile(Tile selectedTile) {
        ArrayList<Coords> moves = selectedTile.getPiece().getMoves();
        String[] movesString = new String[moves.size()];
        String letters = "ABCDEFGH";
        for (int c = 0; c < moves.size(); c++) {
            String tileString = String.format("%s%d", letters.charAt(moves.get(c).y), moves.get(c).x);
            movesString[c] = tileString;
        }
        boolean validTile = false;
        Coords selectedCoords = new Coords(0, 0);
        while (!validTile) {
            System.out.println("Select a tile to move to:");
            for (String move : movesString) {
                System.out.println(move);
            }
            String tile = this.scanner.nextLine();
            if (Arrays.asList(movesString).contains(tile)) {
                selectedCoords = moves.get(Arrays.asList(movesString).indexOf(tile));
                validTile = true;
            } else {
                System.out.printf("move not part of %s", Arrays.asList(movesString));
            }
        }
        return this.board[selectedCoords.x][selectedCoords.y];
    }

    public void PlayGame() {
        boolean running = true;
        while (running) {
            this.DrawBoard();
            System.out.printf("Current player: %s\n", this.currentPlayer.player);
            Tile sourceTile = this.ChooseSourceTile();
            Tile destTile = this.ChooseDestTile(sourceTile);
            this.SwapTiles(sourceTile, destTile);
            if (this.currentPlayer.equals(white)) {
                this.currentPlayer = black;
            } else {
                this.currentPlayer = white;
            }
        }
        this.scanner.close();
    }

    public void CreateBoard() {
        String[] pieceOrder07 = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
        this.board[0] = new Tile[8];
        for (int r1 = 0; r1 < 8; r1++) {
            this.board[0][r1] = new Tile(pieceOrder07[r1], black, 0, r1);
        }
        this.board[1] = new Tile[8];
        for (int p = 0; p < 8; p++) {
            this.board[1][p] = new Tile("Pawn", black, 1, p);
        }
        for (int n = 2; n < 6; n++) {
            this.board[n] = new Tile[8];
            for (int m = 0; m < 8; m++) {
                this.board[n][m] = new Tile("None", null, n, m);
            }
        }
        this.board[6] = new Tile[8];
        for (int p = 0; p < 8; p++) {
            this.board[6][p] = new Tile("Pawn", white, 6, p);
        }
        this.board[7] = new Tile[8];
        for (int r8 = 0; r8 < 8; r8++) {
            this.board[7][r8] = new Tile(pieceOrder07[r8], white, 7, r8);
        }
    }

    public void DrawBoard() {
        System.out.println("  A  B  C  D  E  F  G  H");
        System.out.println("  -------------------------");
        for (int i = 0; i < 8; i++) {
            System.out.print(i);
            for (int j = 0; j < 8; j++) {
                System.out.print("|");
                String symbol;
                if (this.board[i][j].getPiece() != null) {
                    symbol = String.format("%s%s", this.board[i][j].getPiece().getSymbol(), this.board[i][j].getPiece().getPlayer().player);
                } else {
                    symbol = "  ";
                }
                System.out.print(symbol);
            }
            System.out.println("|");
            System.out.println("  -------------------------");
        }
        System.out.println("  A  B  C  D  E  F  G  H");
    }

    public class Coords {
        int x, y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public class Player {
        int piecesLeft;
        int score;
        String player;
        public Player(String player) {
            this.player = player;
            this.score = 0;
            this.piecesLeft = 16;
        }
        
        public void incScore() {
            this.score++;
        }

        public void decPieces() {
            this.piecesLeft--;
        }
    }

    public ArrayList<Coords> getLeftMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int j = y-1;
        while (j >= 0) {
            Piece piece = board[x][j].getPiece();
            if (piece == null) {
                moves.add(new Coords(x, j));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(x, j));
                break;
            }
            j--;
        }
        return moves;
    }

    public ArrayList<Coords> getRightMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int j = y+1;
        while (j < 8) {
            Piece piece = board[x][j].getPiece();
            if (piece == null) {
                moves.add(new Coords(x, j));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(x, j));
                break;
            }
            j--;
        }
        return moves;
    }

    public ArrayList<Coords> getUpMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int i = x-1;
        while (i >= 0) {
            Piece piece = this.board[i][y].getPiece();
            if (piece == null) {
                moves.add(new Coords(i, y));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else if (!(piece.getPlayer().player.equals(this.currentPlayer.player))) {
                moves.add(new Coords(i, y));
                break;
            }
            i--;
        }
        return moves;
    }

    public ArrayList<Coords> getDownMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int i = x+1;
        while (i < 8) {
            Piece piece = this.board[i][y].getPiece();
            if (piece == null) {
                moves.add(new Coords(i, y));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(i, y));
                break;
            }
            i++;
        }
        return moves;
    }

    public ArrayList<Coords> getUpLeftMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int i = x-1;
        int j = y-1;
        while (i >= 0 && j >= 0) {
            Piece piece = this.board[i][j].getPiece();
            if (piece == null) {
                moves.add(new Coords(i, j));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(i, j));
                break;
            }
            i--;
            j--;
        }
        return moves;
    }

    public ArrayList<Coords> getDownRightMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int i = x+1;
        int j = y+1;
        while (i < 8 && j < 8) {
            Piece piece = this.board[i][j].getPiece();
            if (piece == null) {
                moves.add(new Coords(i, j));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(i, j));
                break;
            }
            i++;
            j++;
        }
        return moves;
    }

    public ArrayList<Coords> getUpRightMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int i = x-1;
        int j = y+1;
        while (i >= 0 && j < 8) {
            Piece piece = this.board[i][j].getPiece();
            if (piece == null) {
                moves.add(new Coords(i, j));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(i, j));
                break;
            }
            i--;
            j++;
        }
        return moves;
    }

    public ArrayList<Coords> getDownLeftMoves(int x, int y) {
        ArrayList<Coords> moves = new ArrayList<Coords>();
        int i = x+1;
        int j = y-1;
        while (i < 8 && j >= 0) {
            Piece piece = this.board[i][j].getPiece();
            if (piece == null) {
                moves.add(new Coords(i, j));
            } else if (piece.getPlayer().player.equals(this.currentPlayer.player)) {
                break;
            } else {
                moves.add(new Coords(i, j));
                break;
            }
            i++;
            j--;
        }
        return moves;
    }

    public class Tile {
        Piece piece;
        int x, y;
        public Tile(String piecetype, Player player, int x, int y) {
            this.x = x;
            this.y = y;
            if (!(piecetype.equals("None"))) {
                if (piecetype.equals("Rook")) {
                    this.piece = new Rook(player, this);
                } else if (piecetype.equals("Knight")) {
                    this.piece = new Knight(player, this);
                } else if (piecetype.equals("Bishop")) {
                    this.piece = new Bishop(player, this);
                } else if (piecetype.equals("King")) {
                    this.piece = new King(player, this);
                } else if (piecetype.equals("Queen")) {
                    this.piece = new Queen(player, this);
                } else if (piecetype.equals("Pawn")) {
                    this.piece = new Pawn(player, this);
                }
            } else {
                this.piece = null;
            }
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
            if (piece != null) {
                this.piece.setTile(this);
            }
        }

        public Piece getPiece() {
            return this.piece;
        }
    }
    
    public class Piece {
        Player player;
        Tile tile;
        String symbol;

        public Piece(Player player, Tile tile) {
            this.player = player;
            this.tile = tile;
        }

        public int getX() {
            return this.tile.x;
        }
        public int getY() {
            return this.tile.y;
        }
        public ArrayList<Coords> getMoves() {
            return new ArrayList<Coords>();
        }
        public Player getPlayer() {
            return this.player;
        }
        public String getSymbol() {
            return this.symbol;
        }
        public void setTile(Tile tile) {
            this.tile = tile;
        }
    }

    public class Rook extends Piece {
        String symbol;
        Player player;
        Tile tile;

        public Rook(Player player, Tile tile) {
            super(currentPlayer, tile);
            this.symbol = "R";
            this.player = player;
            this.tile = tile;
        }

        @Override 
        public String getSymbol() {
            return this.symbol;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public ArrayList<Coords> getMoves() {
            ArrayList<Coords> moves = new ArrayList<Coords>();
            //moves to the left
            moves.addAll(getLeftMoves(this.getX(), this.getY()));
            //moves to the right
            moves.addAll(getRightMoves(this.getX(), this.getY()));
            //moves up
            moves.addAll(getUpMoves(this.getX(), this.getY()));
            //moves down
            moves.addAll(getDownMoves(this.getX(), this.getY()));
            return moves;
        }
    }

    public class Knight extends Piece {
        String symbol;
        Player player;
        Tile tile;

        public Knight(Player player, Tile tile) {
            super(player, tile);
            this.symbol = "N";
            this.player = player;
            this.tile = tile;
        }

        @Override 
        public String getSymbol() {
            return this.symbol;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public ArrayList<Coords> getMoves() {
            ArrayList<Coords> potential_moves = new ArrayList<Coords>();
            potential_moves.add(new Coords(this.getX()-2, this.getY()-1));
            potential_moves.add(new Coords(this.getX()-2, this.getY()+1));
            potential_moves.add(new Coords(this.getX()-1, this.getY()-2));
            potential_moves.add(new Coords(this.getX()+1, this.getY()-2));
            potential_moves.add(new Coords(this.getX()+2, this.getY()-1));
            potential_moves.add(new Coords(this.getX()+2, this.getY()+1));
            potential_moves.add(new Coords(this.getX()+1, this.getY()+2));
            potential_moves.add(new Coords(this.getX()-1, this.getY()+2));
            ArrayList<Coords> moves = new ArrayList<Coords>();
            for (Coords coord : potential_moves) {
                if (coord.x >= 0 && coord.x < 8 && coord.y >= 0 && coord.y < 8) {
                    if (board[coord.x][coord.y].getPiece() == null) {
                        moves.add(coord);
                    } else {
                        if (!(board[coord.x][coord.y].getPiece().getPlayer().equals(this.getPlayer()))) {
                            moves.add(coord);
                        }
                    }
                }
            }
            return moves;
        }
    }

    public class Bishop extends Piece {
        String symbol;
        Player player;
        Tile tile;

        public Bishop(Player player, Tile tile) {
            super(player, tile);
            this.symbol = "B";
            this.player = player;
            this.tile = tile;
        }

        @Override 
        public String getSymbol() {
            return this.symbol;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public ArrayList<Coords> getMoves() {
            ArrayList<Coords> moves = new ArrayList<Coords>();
            moves.addAll(getUpLeftMoves(this.getX(), this.getY()));
            moves.addAll(getDownRightMoves(this.getX(), this.getY()));
            moves.addAll(getUpRightMoves(this.getX(), this.getY()));
            moves.addAll(getDownLeftMoves(this.getX(), this.getY()));
            return moves;
        }
    }

    public class King extends Piece {
        String symbol;
        Player player;
        Tile tile;

        public King(Player player, Tile tile) {
            super(player, tile);
            this.symbol = "K";
            this.player = player;
            this.tile = tile;
        }

        @Override 
        public String getSymbol() {
            return this.symbol;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public ArrayList<Coords> getMoves() {
            ArrayList<Coords> moves = new ArrayList<Coords>();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i >= 0 && i < 8 && j >= 0 && j < 8) {
                        if (!(i == this.getX() && j == this.getY())) {
                            moves.add(new Coords(i, j));
                        }
                    }
                }
            }
            return moves;
        }
    }

    public class Queen extends Piece {
        String symbol;
        Player player;
        Tile tile;

        public Queen(Player player, Tile tile) {
            super(player, tile);
            this.symbol = "Q";
            this.player = player;
            this.tile = tile;
        }

        @Override 
        public String getSymbol() {
            return this.symbol;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public ArrayList<Coords> getMoves() {
            ArrayList<Coords> moves = new ArrayList<Coords>();
            moves.addAll(getLeftMoves(this.getX(), this.getY()));
            moves.addAll(getRightMoves(this.getX(), this.getY()));
            moves.addAll(getUpMoves(this.getX(), this.getY()));
            moves.addAll(getDownMoves(this.getX(), this.getY()));
            moves.addAll(getUpLeftMoves(this.getX(), this.getY()));
            moves.addAll(getDownRightMoves(this.getX(), this.getY()));
            moves.addAll(getUpRightMoves(this.getX(), this.getY()));
            moves.addAll(getDownLeftMoves(this.getX(), this.getY()));
            return moves;
        }
    }

    public class Pawn extends Piece {
        String symbol;
        Player player;
        Tile tile;

        public Pawn(Player player, Tile tile) {
            super(player, tile);
            this.player = player;
            this.tile = tile;
            this.symbol = "P";
        }

        @Override 
        public String getSymbol() {
            return this.symbol;
        }

        public Player getPlayer() {
            return this.player;
        }

        @Override
        public ArrayList<Coords> getMoves() {
            ArrayList<Coords> moves = new ArrayList<Coords>();
            if (this.player.player.equals("B")) {
                if (this.getX()+1 < 8) {
                    moves.add(new Coords(this.getX()+1, this.getY()));
                }
            } else if (this.player.player.equals("W")) {
                if (this.getX()-1 >= 0) {
                    moves.add(new Coords(this.getX()-1, this.getY()));
                }
            }
            return moves;
        }
    }
}