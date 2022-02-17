package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.Weight;
import za.co.entelect.challenge.Speed;

import java.util.*;


import static java.lang.Math.max;

import java.text.NumberFormat.Style;

public class Bot {


    // private List<Command> directionList = new ArrayList<>();
    private Random random;
    private GameState gameState;


    private Car opponent;
    private Car myCar;
    private Speed speed;
    
    public Bot(Random random, GameState gameState) {
        this.random = random;
        this.gameState = gameState;
        this.myCar = gameState.player;
        this.opponent = gameState.opponent;

        boolean haveBoost = (checkPowerUps(PowerUps.BOOST, this.myCar.powerups) || this.myCar.boosting);
        this.speed = new Speed(myCar.damage, myCar.speed, haveBoost);
    }

    public Command run() {
        if(this.myCar.position.block >= 1485){
            if(myCar.speed == 0){
                return new FixCommand();
            }else{
                return new AccelerateCommand();
            }
        }

        //* *If we get damage at least 2, just fix our car
        if(myCar.damage >= 2){
            return new FixCommand();
        }
                
        // * *If we have tweet command, just use it
        //ToDo: We haven't discuss about row and column for tweet
        
        ArrayList<ArrayList<Lane>> available = getAvailableBlock(myCar.position.block, myCar.powerups, this.gameState);
        if(checkPowerUps(PowerUps.TWEET, this.myCar.powerups)){

            int multiply = 15;

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

        String bestCommand = tobetested.bestCommand(myCar, opponent, available, gameState.lanes.get(0)[0].position.block, speed);
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
                return new AccelerateCommand();
        }

    }

    /**
     * Returns map of blocks and the objects in the for the current lanes, returns the amount of blocks that can be
     * traversed at max speed.
     **/
    private ArrayList<ArrayList<Lane>> getAvailableBlock(int block, PowerUps[] power, GameState gameState) {
        List<Lane[]> map = gameState.lanes;
        ArrayList<ArrayList<Lane>> blocks = new ArrayList<ArrayList<Lane>>();
        int startBlock = map.get(0)[0].position.block;
        int maxtravel = speed.getMaxSpeed();


        for(int i = 0; i < 4; i ++){
            ArrayList<Lane> in = new ArrayList<Lane>();
            Lane[] each = map.get(i);
            for (int j = 0; j <= maxtravel + 5; j++) {
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
        PowerUps[] available = this.myCar.powerups;

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

        if(checkTurnValid(-1, lane)){
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
