import java.util.ArrayList;
import java.util.Arrays;

class Node {
    int[][] moveOptions =
            {
                    {1, 0},
                    {-1, 0},
                    {0, 1},
                    {0, -1}
            }; //list of the possible moves that can be made on the board
    int board[][]; //declare all variables we will need
    int zeroPosX;
    int zeroPosY;
    Node parent;
    int pathVal;
    int heuristic;
    int cost;


    public Node(Node parent, int board[][]) {
        this.parent = parent;
        this.board = board;
        //if this is the start node, find the X and Y positions of 0 (the space) and the heuristic of each space of the board
        if (parent == null) {
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int n = board[x][y];
                    if (n == 0) {
                        zeroPosX = x;
                        zeroPosY = y;
                    } else {
                        int x1 = (n - 1) / 3;
                        int y1 = (n - 1) % 3;
                        this.heuristic += Math.abs(x - x1) + Math.abs(y - y1);
                    }
                }
            }
        }
    }

    public ArrayList<Node> possibleMoves() {
        ArrayList<Node> moves = new ArrayList<>(); //create a new array list of possible moves we can take
        for (int i = 0; i < 4; i++) {
            if (validMove(zeroPosX + moveOptions[i][0], zeroPosY + moveOptions[i][1])) {  //if the current move applied is a valid move
                int number1 = board[zeroPosX + moveOptions[i][0]][zeroPosY + moveOptions[i][1]]; //create variable of the number in that position
                int distance1 = distanceFromGoal(zeroPosX + moveOptions[i][0], (number1 - 1) / 3, zeroPosY + moveOptions[i][1], (number1 - 1) % 3); //save the distance of that number from its goal position
                makeMove(zeroPosX, zeroPosY, zeroPosX + moveOptions[i][0], zeroPosY + moveOptions[i][1]); //make the move by swapping the numbers on the board
                int boardCopy[][] = new int[3][3]; //copy this move on to a new board
                for (int j = 0; j < 3; j++) {
                    boardCopy[j] = board[j].clone();
                }
                int x2 = (number1 - 1) / 3; //find the positions of the new x and y coordinate we want
                int y2 = (number1 - 1) % 3;
                Node move = new Node(this, boardCopy); //create a new node to place this move data on
                move.zeroPosX = this.zeroPosX + moveOptions[i][0];
                move.zeroPosY = this.zeroPosY + moveOptions[i][1];
                int distance2 = distanceFromGoal(zeroPosX, x2, zeroPosY, y2); //calculate the new distance from the goal position
                int distanceChange; // it can only change by +1 or -1, as we can only make one move at a time
                if (distance1 < distance2) {
                    distanceChange = 1;
                } else {
                    distanceChange = -1;
                }
                move.heuristic = this.heuristic + distanceChange; //add that distance change to the heuristic value of this move node
                moves.add(move); //add this move to the possible moves list
                makeMove(zeroPosX, zeroPosY, zeroPosX + moveOptions[i][0], zeroPosY + moveOptions[i][1]); //finally revert the change for next move
            }
        }
        return moves;
    }

    public int distanceFromGoal(int x1, int x2, int y1, int y2) {
        int value = Math.abs(x1 - x2) + Math.abs(y1 - y2);  //calculate the distance between two points
        return value;
    }

    private void makeMove(int x1, int y1, int x2, int y2) {
        int tempNumber = board[x1][y1];  // make a move on the board, by using a temp number
        board[x1][y1] = board[x2][y2];
        board[x2][y2] = tempNumber;
    }

    private boolean validMove(int x, int y) { // check if the
        return (x >= 0 && y >= 0 && x < 3 && y < 3);
    }

    @Override
    public boolean equals(Object o) {
        Node node = (Node) o;
        return Arrays.deepEquals(board, node.board);
    }
}
