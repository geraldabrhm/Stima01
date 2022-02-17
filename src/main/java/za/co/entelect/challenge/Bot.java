package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.Weight;

import java.util.*;


import static java.lang.Math.max;

public class Bot {


    // private List<Command> directionList = new ArrayList<>();
    private Random random;
    private GameState gameState;


    private Car opponent;
    private Car myCar;
    private int maxtravel;
    
    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;
    
        if(myCar.damage == 0){
            maxtravel = 15;
        }else if(myCar.damage == 1){
            maxtravel = 9;
        }else if(myCar.damage == 2){
            maxtravel = 8;
        }else if(myCar.damage == 3){
            maxtravel = 6;
        }else if(myCar.damage == 4){
            maxtravel = 3;
        }else if(myCar.damage == 5){
            maxtravel = 0;
        }
    }

    public Command run() {
        //* *If we get damage at least 2, just fix our car
        if(myCar.damage >= 2){
            return new FixCommand();
        }
                
        // * *If we have tweet command, just use it
        //ToDo: We haven't discuss about row and column for tweet
        
        ArrayList<ArrayList<Lane>> available = getAvailableBlock(myCar.position.block, myCar.powerups);
        if(checkPowerUps(PowerUps.TWEET, myCar.powerups)){

            int multiply;
            if(checkPowerUps(PowerUps.BOOST, opponent.powerups) && opponent.damage <= 3){
                multiply = 15;
            }else{
                multiply = 9;
            }

            int bestblock = opponent.position.block+ 2 * multiply;

            if(bestblock >= 1500){
                bestblock = 1499;
            }

            return new TweetCommand(opponent.position.lane, bestblock);
        }

        ArrayList<Value> WeightList = new ArrayList<Value>();

        if(isLeading()){
            WeightList = createWeightList(1);
        }else{
            WeightList = createWeightList(2);
        }

        Weight tobetested = new Weight(WeightList);

        String bestCommand = tobetested.bestCommand(myCar, opponent, available, gameState.lanes.get(0)[0].position.block);
        
        switch(bestCommand){
            case "Nothing": // Copy
                return new DoNothingCommand();
            case "Accelerate": 
                return new AccelerateCommand();
            case "Decelerate":
                return new DecelerateCommand();
            case "Turn_Right":
                return new ChangeLaneCommand(1);
            case "Turn_Left":
                return new ChangeLaneCommand(-1);
            case "Use_Boost":
                return new BoostCommand();
            case "Use_Lizard":
                return new LizardCommand();
            case "Use_Oil":
                return new OilCommand();
            case "Use_EMP":
                return new EmpCommand();
            default:
                return new DoNothingCommand();
        }

    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private ArrayList<ArrayList<Lane>> getAvailableBlock(int block, PowerUps[] power) {
        List<Lane[]> map = gameState.lanes;
        ArrayList<ArrayList<Lane>> blocks = new ArrayList<ArrayList<Lane>>();
        int startBlock = map.get(0)[0].position.block;

        if(maxtravel == 15 && !checkPowerUps(PowerUps.BOOST, power) || maxtravel == 15 && !myCar.boosting){
            maxtravel = 9;
        }

        for(int i = 0; i < 4; i ++){
            ArrayList<Lane> in = new ArrayList<Lane>();
            Lane[] each = map.get(i);
            for (int j = max(block - startBlock, 0); j <= block - startBlock + maxtravel; j++) {
                if (each[j] == null || each[j].terrain == Terrain.FINISH) {
                    break;
                }
    
                in.add(each[j]);
            }
            blocks.add(in);

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

    private int getSpeedApprox(Car myCar, String command) {
        int speed0 = 0;
        int speed1 = 3;
        int speed2 = 5;
        int speed3 = 6;
        int speed4 = 8;
        int speed5 = 9;
        int speed6 = 15;
        int[] speedArr = new int[]{speed0, speed1, speed2, speed3, speed4, speed5, speed6};
        
        int speednow = 5;
        int indexSpeed = 0;

        switch (myCar.speed) {
            case 0:
                indexSpeed = 0;
                break;
            case 3:
                indexSpeed = 1;
                break;
            case 5:
                indexSpeed = 2;
                break;
            case 6:
                indexSpeed = 3;
                break;
            case 8:
                indexSpeed = 4;
                break;
            case 9:
                indexSpeed = 5;
                break;
            case 15:
                indexSpeed = 6;
                break;
        }

        switch (command) {
            case "Nothing":
                speednow = indexSpeed;
                break;
            case "Accelerate":
                speednow = indexSpeed+1;
                if (speednow == 6) {
                    speednow = 5;
                }
                break;
            case "Decelerate":
                speednow = indexSpeed-1;
                if (speednow == -1) {
                    speednow = 0;
                }
                break;
            case "Turn_Left":
                speednow = indexSpeed;
                break;
            case "Turn_Right":
                speednow = indexSpeed;
                break;
            case "Use_Boost":
                speednow = 6;
                break;
            case "Use_Oil":
                speednow = indexSpeed;
                break;
        }
        int optSpeed = speedArr[speednow];
        return optSpeed;
    }

    private int damageSpeed(Car myCar) {
        int maks = 15;
        switch (myCar.damage) {
            case 1:
                maks = 9;
                break;
            case 2:
                maks = 8;
                break;
            case 3:
                maks = 6;
                break;
            case 4:
                maks = 3;
                break;
            case 5:
                maks = 0;
                break;
        }

        return maks;
    }

}
