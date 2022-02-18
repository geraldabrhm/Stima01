package za.co.entelect.challenge;

import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.Value;
import za.co.entelect.challenge.Speed;

import java.util.*;

public class Weight{
    private ArrayList<Value> AllCommand;
    private int startAll;

    private final int ShiftColumn = 22;
    private final int speedChange = 25;
    private final int PowerUp = 20;
    private final int BonusScore = 22;


    private final int empval = 7;
    private final int tweetval = 5;
    private final int lizardval = 5;
    private final int boostval = 6;
    private final int oilval = 3;

    //Leading -> 1
    //Behind -> 2
    public Weight(ArrayList<Value>WeightList){
        this.AllCommand = WeightList;
    }

    public String bestCommand(Car myCar, Car opponent, ArrayList<ArrayList<Lane>>Available, int startBlock, Speed speed){
        // * * Each Lane -> Visible Lane 
        // * * For example Lane 1 -> Visible Lane in Lane 1 (Top One), from behind car (5 Block) until achieveable block in this round
        this.startAll = getStart(myCar, startBlock);
        
        shiftColumn(speed, myCar, Available);
        speedChange(speed, myCar, Available);
        powerUp(myCar, Available, speed);
        bonusPoint(myCar, opponent, Available, speed);

        String valuereturn = AllCommand.get(0).getCommand();
        int max =  AllCommand.get(0).getValue();

        for(int i = 1; i < AllCommand.size(); i ++){
            if(AllCommand.get(i).getValue() > max){
                max = AllCommand.get(i).getValue();
                valuereturn = AllCommand.get(i).getCommand();
            }
        }
        return valuereturn;
    }

    private int getStart(Car mycar, int startBlock){
        int start = (mycar.position.block - startBlock);
        return start + 1;
    }

    private void shiftColumn(Speed speed, Car myCar, ArrayList<ArrayList<Lane>> available){
        
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            switch(temp){
                case "Nothing":
                    this.AllCommand.get(i).addValue(speed.getCurrentSpeed() * ShiftColumn);
                    break;
                case "Accelerate":
                    this.AllCommand.get(i).addValue(speed.getAccelerate() * ShiftColumn);
                    break;
                case "Decelerate":
                    this.AllCommand.get(i).addValue(speed.getDecelerate() * ShiftColumn);
                    break;
                case "Turn_Right":
                    this.AllCommand.get(i).addValue((speed.getCurrentSpeed() - 1) * ShiftColumn);
                    break;
                case "Turn_Left":
                    this.AllCommand.get(i).addValue((speed.getCurrentSpeed() - 1) * ShiftColumn);
                    break;
                case "Use_Boost":
                    this.AllCommand.get(i).addValue(speed.getBoost() * ShiftColumn);
                    break;
                case "Use_Lizard": 
                case "Use_Oil":
                case "Use_EMP":
                    this.AllCommand.get(i).addValue(speed.getCurrentSpeed() * ShiftColumn);
                    break;
            }
        }
    }

    private void speedChange(Speed speed, Car myCar, ArrayList<ArrayList<Lane>> available){ // gery
        int indexLane = myCar.position.lane - 1;
        int curr_max_speed = speed.getMaxSpeed();
        int currentSpeed = speed.getCurrentSpeed();
        int start = this.startAll;
        int speedTempStraight, speedTempRight=0, speedTempLeft=0, speedTempBoost, speedTempAccelerate, speedTempDecelerate;
        
        speedTempStraight = itterateLane(available, indexLane, (currentSpeed + start), start, 2, speed);
        if(indexLane != 2) {
            speedTempRight = itterateLane(available, (indexLane + 1), (currentSpeed + start - 1), start, 2, speed);
        }
        if(indexLane != 0) {
            speedTempLeft = itterateLane(available, (indexLane - 1), (currentSpeed + start - 1), start, 2, speed);
        }
        speedTempBoost = itterateLane(available, indexLane, (curr_max_speed + start), start, 2, speed);
        speedTempAccelerate = itterateLane(available, indexLane, (speed.getAccelerate() + start), start, 2 , speed );
        speedTempDecelerate = itterateLane(available, indexLane, (speed.getDecelerate() + start), start, 2 , speed );


        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            switch(temp){
                case "Nothing": 
                case "Use_Oil":
                case "Use_EMP":
                    AllCommand.get(i).addValue((speedTempStraight - currentSpeed) * speedChange);
                    break;    
                case "Accelerate":
                    AllCommand.get(i).addValue((speedTempAccelerate - currentSpeed) * speedChange);
                    break;    
                case "Decelerate":
                    AllCommand.get(i).addValue((speedTempDecelerate - currentSpeed) * speedChange);
                    break;    
                case "Turn_Right":
                    AllCommand.get(i).addValue((speedTempRight - currentSpeed) * speedChange);
                    break;
                case "Turn_Left":
                    AllCommand.get(i).addValue((speedTempLeft - currentSpeed) * speedChange);
                    break;
                case "Use_Boost":
                    AllCommand.get(i).addValue((speedTempBoost - currentSpeed) * speedChange);
                    break;
                case "Use_Lizard":
                    if( available.get(indexLane).get(currentSpeed + 5).terrain == Terrain.WALL) {
                        AllCommand.get(i).addValue((3 - currentSpeed) * speedChange);
                    } else if (available.get(indexLane).get(currentSpeed + start - 1).terrain == Terrain.MUD || available.get(indexLane).get(currentSpeed + 5).terrain == Terrain.OIL_SPILL) {
                        AllCommand.get(i).addValue((speed.getAccelerate() - currentSpeed) * speedChange);
                    } else {
                        AllCommand.get(i).addValue(speedTempStraight * speedChange);
                    }
            }
        }
    }

    private void powerUp(Car myCar, ArrayList<ArrayList<Lane>>available, Speed speed){ 
        int start = this.startAll;
        for(int i = 0; i < AllCommand.size(); i ++){
            int count = 0;
            switch(AllCommand.get(i).getCommand()){
                case "Nothing":
                case "Use_Oil":
                case "Use_EMP":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getCurrentSpeed() + start, start, 1, speed);
                    break;
                case "Turn Right":
                    count = itterateLane(available, myCar.position.lane, speed.getCurrentSpeed() + start - 1, start, 1, speed);
                    break;
                case "Turn Left":
                    count  = itterateLane(available, myCar.position.lane - 2, speed.getCurrentSpeed() + start - 1, start, 1, speed);
                    break;
                case "Accelerate":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getAccelerate() + start, start, 1, speed);
                    break;
                case "Decelerate":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getDecelerate() + start , start, 1, speed);
                    break;
                case "Use_Boost":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getMaxSpeed() + start, start, 1, speed);
                    break;
                case "Use_Lizard":
                    Terrain curr = available.get(myCar.position.lane - 1).get(speed.getCurrentSpeed() + start - 1).terrain;
                    count = changepwScore(curr);
                    break;
            }
            AllCommand.get(i).addValue(count * PowerUp);
        }
    }

    private int itterateLane(ArrayList<ArrayList<Lane>>available, int lane, int finalblock, int startblock, int aggregate, Speed speed){
        /* 
            Agrregate:
                1 -> powerup
                2 -> speedChange
        */

        int count = 0;
        int speedcount = 0;
        boolean speedbreak = false;
        for(int j = startblock; j < finalblock; j ++){

            Terrain curr = available.get(lane).get(j).terrain;
            switch(aggregate){
                case 1:
                    count += changepwScore(curr);
                    break;
                case 2:
                    if(curr == Terrain.MUD || curr == Terrain.OIL_SPILL){
                        speedcount ++;
                    }else if(curr == Terrain.WALL){
                        count = 3;
                        speedbreak = true;
                    }
                    break;
            }

            if(aggregate == 3 && speedbreak){
                break;
            }
        }
        
        if(!speedbreak && aggregate == 3){
            count = speed.getMultipleDecelerate(speedcount);
        }
        return count;
    }

    private int changepwScore(Terrain curr){
        if(curr == Terrain.OIL_POWER){
            return oilval;
        }else if(curr== Terrain.BOOST){
            return boostval;
        }else if(curr == Terrain.EMP){
            return empval;
        }else if(curr == Terrain.LIZARD){
            return lizardval;
        }else if(curr == Terrain.TWEET){
            return tweetval;
        }else{
            return 0;
        }
    }

    private void bonusPoint(Car myCar, Car opponent, ArrayList<ArrayList<Lane>>available, Speed speed){
        int start = this.startAll;
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();
            switch(temp) {
                case "Nothing":
                    this.AllCommand.get(i).addValue(0);
                    break;
                case "Accelerate":
                    this.AllCommand.get(i).addValue(0);
                    break;
                case "Decelerate":
                    if (temp == "Use_Boost") {
                        this.AllCommand.get(i).addValue(BonusScore*(-1000));
                        break;
                    }
                case "Turn_Right":
                    this.AllCommand.get(i).addValue(0);
                    break;
                case "Turn_Left":
                    this.AllCommand.get(i).addValue(0);
                    break;
                case "Use_Boost":
                    if(myCar.speed == speed.getMaxSpeed()){
                        this.AllCommand.get(i).addValue(BonusScore * (-100));
                    }else{
                        if (myCar.damage == 0) {
                            this.AllCommand.get(i).addValue(BonusScore*(10));
                        }
                        if (myCar.damage == 1) {
                            this.AllCommand.get(i).addValue(BonusScore*(9));
                        }
                    }
                    break;
                case "Use_Lizard":
                    int many = 0;
                    int lookOpp = speed.getCurrentSpeed();
                    for (int j = start; j < available.get(myCar.position.lane-1).size(); j++) {
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane-1).get(j).terrain == Terrain.WALL || (myCar.position.block+lookOpp <= opponent.position.block && myCar.position.lane == opponent.position.lane)) {
                            many++;
                        }
                    }
                    int loc = available.get(myCar.position.lane-1).size();
                    if (many >= 4 && (available.get(myCar.position.lane-1).get(loc - 1).terrain == Terrain.EMPTY || available.get(myCar.position.lane-1).get(loc - 1).terrain == Terrain.OIL_POWER|| available.get(myCar.position.lane-1).get(loc - 1).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(loc - 1).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(loc - 1).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(loc - 1).terrain == Terrain.TWEET)) {
                        this.AllCommand.get(i).addValue(BonusScore*7);
                    }
                    break;
                case "Use_Oil":
                    if (myCar.position.lane == opponent.position.lane) {
                        int numLane = 0;
                        int obstacles = 0;
                        if (myCar.position.lane - 1 != 0) {
                            for (int j = 0; j < available.get(myCar.position.lane-1).size(); j++) {
                                if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane-2).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane-2).get(j).terrain == Terrain.WALL) {
                                    obstacles++;
                                }
                            } 
                            numLane += 2;
                        }
                        if (myCar.position.lane + 1 != 5) {
                            for (int j = 0; j < available.get(myCar.position.lane-1).size(); j++) {
                                if (available.get(myCar.position.lane).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane).get(j).terrain == Terrain.WALL) {
                                    obstacles++;
                                }
                            }
                            numLane += 2;
                        }
                        for (int j = 0; j < available.get(myCar.position.lane-1).size(); j++) {
                            if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane-1).get(j).terrain == Terrain.WALL){
                                obstacles++;
                            }
                        }
                        if (numLane == 4) {
                            if (obstacles > 5) {
                                this.AllCommand.get(i).addValue(BonusScore*6);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*4);
                            }
                        }
                        else if (numLane == 2) {
                            if (obstacles > 3) {
                                this.AllCommand.get(i).addValue(BonusScore*4);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*2);
                            }
                        }
                        else {
                            this.AllCommand.get(i).addValue(0);
                        }
                    }
                    break;
                case "Use_EMP": 
                    if (myCar.position.lane == opponent.position.lane) {
                        this.AllCommand.get(i).addValue(BonusScore*9);
                    }else if(myCar.position.lane == opponent.position.lane - 1 || myCar.position.lane ==  opponent.position.lane + 1){
                        this.AllCommand.get(i).addValue(BonusScore * 7);
                    }
                    break;
            }
        }
    }
}

