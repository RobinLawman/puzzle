import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Comparator;

public class Solver {
    //initialize the board
    final int board[][] = {
        {8, 7, 6},
        {5, 4, 3},
        {2, 1, 0}
    };

    public static void main(String[] args) throws FileNotFoundException {
        File outFile = new File("output.txt");
        PrintWriter output = new PrintWriter(outFile);
        new Solver(output);
        output.close();
    }

    public Solver(PrintWriter output) {
        solve(output);
    }

    private void solve(PrintWriter output) {
        Node rootNode = new Node(null, board);
        Comparator<Node> nodeComparison = new compareNodes();
        ArrayList<Node> unexpanded = new ArrayList<>();
        PriorityQueue<Node> expanded = new PriorityQueue<>(nodeComparison);
        expanded.add(rootNode);

        while (expanded.size() > 0) { //while all nodes havent been expanded
            Node n = expanded.poll(); //poll the element at the front of the queue, as its been priority queued it will be the best possible node
            unexpanded.add(n);
            if (n.heuristic == 0) { //if the total heuristic is 0 then the solution has been found
                //print the expanded and unexpanded array list sizes, return number of moves
                output.println(findSolution(n, output) + " Moves");
                output.println(expanded.size() + " Expanded Nodes");
                output.println(unexpanded.size() + " Unexpanded Nodes");
                break;
            }
            ArrayList<Node> moveList = n.possibleMoves(); //otherwise create an array list ready for possible moves to take
            for (Node move : moveList) { //for the next move, in either the expanded or unexpanded list if the move cost is higher than the current cost skip it.
                if (unexpanded.contains(move) || expanded.contains(move) && move.cost >= n.cost) {
                    continue;
                }
                //add 1 to the current moves cost, and give it its new overall path value (cost+heuristic value)
                move.cost = n.cost + 1;
                move.pathVal = move.cost + move.heuristic;
                if(expanded.contains(move)){ // refresh the current moves values in the expanded tree (if its in it)
                    expanded.remove(move);
                    expanded.add(move);
                }else { //if its not just add it, its the first time weve seen this possible move node.
                    expanded.add(move);
                }
            }
        }
    }
    class compareNodes implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.pathVal, n2.pathVal);
        }

    }

    private int findSolution(Node n, PrintWriter output) {
        int count = 0;
        Stack<Node> solution = new Stack<>();
        //set the count to 0 and create a new stack for the solution
        solution.push(n);
        while (n.parent != null) {
            //if this node isnt at the root node yet, push it onto the solution stack so the final move is at the bottom
            n = n.parent;
            solution.push(n);
            count++;
        }
        while (solution.size() > 0) {
            //while there is still something in the stack, pop it and display it.
            Node x = solution.pop();
            printSolution(x.board, output);
        }
        return count;
    }
    //the methods to print the solution to output.txt file

    private void printSolution(int[][] board, PrintWriter output) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String string = "";
                if (board[i][j]==0){
                    output.print(" ");
                } else {
                    output.print(string + board[i][j] + "");
                }
            }
        }
        output.println("");
    }
}