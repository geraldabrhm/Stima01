package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.Terrain;

import java.util.*;

import static java.lang.Math.max;

public class Bot {

    private static final int maxSpeed = 9;
    private List<Integer> directionList = new ArrayList<>();

    private Random random;
    private GameState gameState;
    private Car opponent;
    private Car myCar;
    private final static Command FIX = new FixCommand();

    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;

        directionList.add(-1);
        directionList.add(1);
    }

    public Command run() {
        if(isLeading()){
            Command command = keepLeading();
            return command;
        }else{
            Command command = goChase();
            return command;
        }

        // List<Object> blocks = getBlocksInFront(myCar.position.lane, myCar.position.block);
        // if (myCar.damage >= 5) {
        //     return new FixCommand();
        // }
        // if (blocks.contains(Terrain.MUD)) {
        //     int i = random.nextInt(directionList.size());
        //     return new ChangeLaneCommand(directionList.get(i));
        // }
        // return new AccelerateCommand();
    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Object> getBlocksInFront(int lane, int block) {
        List<Lane[]> map = gameState.lanes;
        List<Object> blocks = new ArrayList<>();
        int startBlock = map.get(0)[0].position.block;

        Lane[] laneList = map.get(lane - 1);
        for (int i = max(block - startBlock, 0); i <= block - startBlock + Bot.maxSpeed; i++) {
            if (laneList[i] == null || laneList[i].terrain == Terrain.FINISH) {
                break;
            }

            blocks.add(laneList[i].terrain);

        }
        return blocks;
    }

    private boolean isLeading(){
        if(myCar.position.block > opponent.position.block){
            return true;
        }
        return false;
    }

    private Command keepLeading(){
        return new AccelerateCommand();
    }

    private Command goChase(){
        return new AccelerateCommand();
    }

    private boolean checkTurnValid(int direction){
        //Check if turn Valid
        if(direction == -1 && myCar.position.lane == 0){
            //Check if turn left valid
            return false;
        }else if(direction == 1 && myCar.position.lane == 4){
            //Check if turn right valid
            return false;
        }else{
            return true;
        }
    }

}
