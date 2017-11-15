package com.csye7215.puzzleSolver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * This file needs to hold your solver to be tested. 
 * You can alter the class to extend any class that extends PuzzleSolver.
 * It must have a constructor that takes in a Puzzle.
 * It must have a solve() method that returns the datatype List<Direction>
 *   which will either be a reference to a list of steps to take or will
 *   be null if the puzzle cannot be solved.
 */
public class StudentMultiPuzzleSolver1 extends SkippingPuzzleSolver
{
    static ForkJoinPool fjPool = new ForkJoinPool();
    List<Direction> solutionPath = new LinkedList<>();

    class Node {
        List<Direction> curSolutionPath = new LinkedList<>();
        Boolean onPath;

        public Node(List<Direction> curSolutionPath, Boolean onPath) {
            this.curSolutionPath = curSolutionPath;
            this.onPath = onPath;
        }
    }

    public StudentMultiPuzzleSolver1(Puzzle puzzle)
    {
        super(puzzle);
    }

    public List<Direction> solve()
    {
        // TODO: Implement your code here

        Position firstPosition = puzzle.getStart();

        Choice firstChoice = getFirstChoice();
        Node resultNode = fjPool.invoke(new searchTask(firstChoice, firstChoice.from));

//        for (int i = 0; i < resultNode.curSolutionPath.size(); i++) {
//            System.out.println(resultNode.curSolutionPath.get(i).name());
//        }
//        return resultNode.curSolutionPath;
        return pathToFullPath(resultNode.curSolutionPath);
//        throw new RuntimeException("Not yet implemented!");
    }

    private Choice getFirstChoice() {
        Choice firstChoice = null;
        if (puzzle.getMoves(puzzle.getStart()).size() == 1) {
            try {
                firstChoice = follow(puzzle.getStart(), puzzle.getMoves(puzzle.getStart()).getFirst());
                return firstChoice;
            } catch (SolutionFound solutionFound) {
                solutionFound.printStackTrace();
            }
        }
        else {
            firstChoice = new Choice(puzzle.getStart(), null, puzzle.getMoves(puzzle.getStart()));
        }
        return firstChoice;
    }

    private class searchTask extends RecursiveTask<Node> {
        Choice choice;
        Direction curDirection;
        Node thisNode = new Node(new LinkedList<>(), false);
        private List<searchTask> taskList = new LinkedList<>();
        public searchTask(Choice choice, Direction direction) {
            this.choice = choice;
            curDirection = direction;
        }
        public Node compute() {
            for (Direction d: choice.choices) {
                try {
                    Choice newChoice = follow(choice.at, d);
                    searchTask st = new searchTask(newChoice, d);
                    st.fork();
                    taskList.add(st);
                } catch (SolutionFound solutionFound) {
                    thisNode.onPath = true;
                    thisNode.curSolutionPath.add(d);
                    return thisNode;
                }
            }

            for (searchTask s: taskList) {
                Node newNode = (Node)s.join();
                if (newNode.onPath) {
                    thisNode.curSolutionPath = newNode.curSolutionPath;
                    thisNode.curSolutionPath.add(0, s.curDirection);
                    thisNode.onPath = true;
                    return thisNode;
                }
            }
            return thisNode;
        }
    }
}
