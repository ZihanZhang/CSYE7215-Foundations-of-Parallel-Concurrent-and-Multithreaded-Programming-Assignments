package com.csye7215.puzzleSolver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This file needs to hold your solver to be tested. 
 * You can alter the class to extend any class that extends PuzzleSolver.
 * It must have a constructor that takes in a Puzzle.
 * It must have a solve() method that returns the datatype List<Direction>
 *   which will either be a reference to a list of steps to take or will
 *   be null if the puzzle cannot be solved.
 */

//The main idea is to parallel the different choices that a node can choose
//But in case of the puzzle being too large which means too many tasks being created
//According to the formula, I decided to use mxn(puzzle size) / (N(CPU number) + 1)
//When the tasks are too small we try single threaded it
//We spawn one task in every choices
//In Every node with more than 1 choices, we spawn tasks for every choice.

//Invariant: SolutionNode should be inside the muzzle and should not be into the wall
//           there is only one executor
//           SEQUENCIALSIZETARGET cannot be modified
public class StudentMultiPuzzleSolver extends SkippingPuzzleSolver
{
//    Border of parallel and sequential execution
//    private final int SEQUENCIALSIZETARGET = mxn(puzzle size) / (N(CPU number) + 1)

//    Has to be synchronized
//    private int numOfThreads;

//    Has to be synchronized
//    private List<Direction> solutionPath

//    Object numOfThreadsLock = new Object()
//    Object solutionPathLock = new Object()

    public class SolutionNode
    {
        public SolutionNode parent;
        public Choice choice;

        public SolutionNode(SolutionNode parent, Choice choice)
        {
            this.parent = parent;
            this.choice = choice;
        }
    }


    public StudentMultiPuzzleSolver(Puzzle puzzle)
    {
        super(puzzle);
    }

    //Precondition:None
    //Postcondition:return the list of directions of the solution
    //Exception:None
    private final Executor exec = Executors.newCachedThreadPool();
    public List<Direction> solve()
    {
        //Initialize the first choice to start position
        //for each choices spawn a task to search for it
        //      spawnTask()
        //      each task take care of the subtrees from that node
        //      task: if exit found throw exception
        //              follow()
        //              spawn tasks for it's subnodes to do the same thing
        //catch exception: return pathToFullPath()

        // TODO: Implement your code here
        throw new RuntimeException("Not yet implemented!");
    }

    //Precondition: None
    //Postcondition: None
    //Exception: None
    private void spawnTask(SolutionNode node){
        //IF exit found {
        //    throw exception
        //}
        //IF numOfThreads > SEQUENCIALSIZETARGET {
        //      STPuzzleSolverRec()                           This solution is best for small puzzle in sequential part
        //}
        //ELSE {
        //  For getChild(node)
        //      new Runnable() {
        //          SolutionNode nxtNode = follow()
        //          spawnTask(nextNode)
        //          synchronized(numberOfThreadsLock) {
        //              numOfThread++;
        //          }
        //      }
        //  End for
        //END IF
        //catch exception:
        //      synchronized(solutionPathLock) {
        //          solutionPath = pathToFullPath()
        //      }
        //END
    }
}
