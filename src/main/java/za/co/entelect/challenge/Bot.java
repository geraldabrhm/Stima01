package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.Weight;

import java.util.*;


import static java.lang.Math.max;
import java.lang.Float;

public class Bot {

    private static final int maxSpeed = 9;

    private List<Command> directionList = new ArrayList<>();
    private Random random;
    private GameState gameState;

    private Car opponent;
    private Car myCar;

    private final static Command ACCELERATE = new AccelerateCommand();
    private final static Command DECELERATE = new DecelerateCommand();
    private final static Command TURN_RIGHT = new ChangeLaneCommand(1);
    private final static Command TURN_LEFT = new ChangeLaneCommand(-1);
    private final static Command NOTHING = new DoNothingCommand();
    private final static Command LIZARD = new LizardCommand();
    private final static Command OIL = new OilCommand();
    private final static Command BOOST = new BoostCommand();
    private final static Command EMP = new EmpCommand();
    private final static Command FIX = new FixCommand();
    
    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;

        directionList.add(TURN_LEFT);
        directionList.add(TURN_RIGHT);
    }

    public Command run() {
        //* *If we get damage at least 2, just fix our car
        if(myCar.damage >= 2){
                return FIX;
        }
                
        // * *If we have tweet command, just use it
        //ToDo: We haven't discuss about row and column for tweet
        List<Terrain>Lane1 = getBlocksInFront(1, myCar.position.block);
        List<Terrain>Lane2 = getBlocksInFront(2, myCar.position.block);
        List<Terrain>Lane3 = getBlocksInFront(3, myCar.position.block);
        List<Terrain>Lane4 = getBlocksInFront(4, myCar.position.block);
        
        if(checkPowerUps(PowerUps.TWEET, myCar.powerups)){
            int opplane = opponent.position.lane;
            int oppblock = opponent.position.block;

            int bestblock = oppblock + maxSpeed;
            return new TweetCommand(opplane, bestblock);
        }

        ArrayList<Value> WeightList = new ArrayList<Value>();
        if(isLeading()){
            WeightList = createWeightList(1);
        }else{
            WeightList = createWeightList(2);
        }

        Weight tobetested = new Weight(WeightList);

        
        Command bestCommand = tobetested.bestCommand(myCar.speed, myCar.damage, Lane1, Lane2, Lane3, Lane4);
        
        return bestCommand;

    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private List<Terrain> getBlocksInFront(int lane, int block) {
        List<Lane[]> map = gameState.lanes;
        List<Terrain> blocks = new ArrayList<Terrain>();
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
        //Check if our car is leading 
        if(myCar.position.block > opponent.position.block){
            return true;
        }
        return false;
    }
    
    private boolean checkTurnValid(int direction, int lane){

        //Check if turn Valid
        if(direction == -1 && lane == 1){
            //Check if turn left valid
            return false;   
        }else if(direction == 1 && lane== 4){
            //Check if turn right valid
            return false;
        }else{
            return true;
        }
    }

    private Boolean checkPowerUps(PowerUps powerUpToCheck, PowerUps[] available) {
        for (PowerUps powerUp: available) {
            if (powerUp.equals(powerUpToCheck)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Value> createWeightList(int condition){
        ArrayList<Value>AllCommand = new ArrayList<Value>();
        int lane = myCar.position.lane;
        PowerUps[] available = myCar.powerups;

        // ? Still not sure, should this method become constructor in weight class or not
        Value accelerate = new Value("Accelerate");
        Value nothing = new Value("Nothing");
        AllCommand.add(accelerate);
        AllCommand.add(nothing);
        
        if(!myCar.boosting){
            Value decelerate = new Value("Decelerate");
            AllCommand.add(decelerate);
        }

        if(checkTurnValid(1, lane)){
            Value turnright = new Value("Turn_Right");
            AllCommand.add(turnright);
        }

        if(checkTurnValid(2, lane)){
            Value turnleft = new Value("Turn_Left");
            AllCommand.add(turnleft);            
        }

        if(checkPowerUps(PowerUps.BOOST, available)){
            Value useboost = new Value("Use_Boost");
            AllCommand.add(useboost);
        }
        
        if(checkPowerUps(PowerUps.LIZARD, available)){
            Value uselizard = new Value("Use_Lizard");
            AllCommand.add(uselizard);
        }
        
        // Usetweet and Fix excluded
        
        if(condition == 1){
            if(checkPowerUps(PowerUps.OIL, available)){
                Value useoil = new Value("Use_Oil");
                AllCommand.add(useoil);
            }
        }else{
            if(checkPowerUps(PowerUps.EMP, available)){
                Value useemp = new Value("Use_EMP");
                AllCommand.add(useemp);
            }
        }

        return AllCommand;
    }

}
