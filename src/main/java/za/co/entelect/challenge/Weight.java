package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.Value;

import java.util.*;

public class Weight{
    private ArrayList<Value> AllCommand;

    private final double ShiftColumn = 22.5;
    private final double speedChange = 25.0;
    private final double PowerUp = 20.0;
    private final double MaxSpeed = 22.5;
    private final double ScoreChanged = 10.0;
    private final double BonusScore = 22.5;


    private final double empval = 4.0;
    private final double tweetval = 4.0;
    private final double lizardval = 3.0;
    private final double boostval = 7.0;
    private final double oilval = 2.0;

    // Hybrid: nothing accelerate decelerate turnright turnleft useboost uselizard // fix usetweet
    // Depan: useemp
    // Belakang: useoil

    //Leading -> 1
    //Behind -> 2
    public Weight(ArrayList<Value>WeightList){
        this.AllCommand = WeightList;
    }

    public String bestCommand(Car myCar, Car opponent, ArrayList<ArrayList<Lane>>Available, int startBlock){
        // * * Each Lane -> Visible Lane 
        // * * For example Lane 1 -> Visible Lane in Lane 1 (Top One), from behind car (5 Block) until achieveable block in this round

        shiftColumn((double)myCar.speed, myCar.position.lane, Available);
        speedChange(myCar.position.lane);
        powerUp(myCar, Available);
        maxSpeedChange(myCar.damage, myCar.position.lane, myCar.position.block, Available);
        bonusPoint(myCar.damage, myCar.position.lane, Available, opponent.position.lane, myCar.position.block);
        scoreChange();

        Collections.sort(AllCommand, (a, b)->{
            return Double.compare(a.getValue(), b.getValue());
        });

        return AllCommand.get(0).getCommand();
    }

    private void shiftColumn(double currentSpeed, int lane, ArrayList<ArrayList<Lane>> available){
        // Belum pertimbangin collision ke mobil
        int indexLane = lane - 1;
        double truckStraight = 0, truckRight = 0, truckLeft = 0;
        boolean boolStraight = false, boolRight = false, boolLeft = false;
        double curr1 = currentSpeed * ShiftColumn;
        double curr2 = (currentSpeed - 1) * ShiftColumn;

        // Lurus
        for(int i = 6; i < (currentSpeed + 6); i++) {
            if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
                truckStraight = i;
                truckStraight -= 6;
                truckStraight *= ShiftColumn;
                boolStraight = true;
                break;
            }
        }
        // Kanan
        for(int i = 6; i < (currentSpeed + 5); i++) {
            if(available.get(indexLane + 1).get(i).OccupiedByCyberTruck) {
                truckRight = i;
                truckRight -= 6;
                truckRight *= ShiftColumn;
                boolRight = true;
                break;
            }
        }
        // Kiri
        for(int i = 6; i < (currentSpeed + 5); i++) {
            if(available.get(indexLane - 1).get(i).OccupiedByCyberTruck) {
                truckLeft = i;
                truckLeft -= 6;
                truckLeft *= ShiftColumn;
                boolLeft = true;
                break;
            }
        }
        
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            switch(temp){
                case "Nothing":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
                case "Accelerate":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
                case "Decelerate":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
                case "Turn_Right":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckRight);
                    } else {
                        this.AllCommand.get(i).addValue(curr2);
                    }
                case "Turn_Left":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckLeft);
                    } else {
                        this.AllCommand.get(i).addValue(curr2);
                    }
                case "Use_Boost":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
                case "Use_Lizard": // Nanti dibenerin, khasus khusus soalnya
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
                case "Use_Oil":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
                case "Use_EMP":
                    if(boolStraight) {
                        this.AllCommand.get(i).addValue(truckStraight);
                    } else {
                        this.AllCommand.get(i).addValue(curr1);
                    }
            }
        }
    }

    // ACCELERATE
    // MINIMUM_SPEED = 0
    // SPEED_STATE_1 = 3
    // INITIAL_SPEED = 5
    // SPEED_STATE_2 = 6
    // SPEED_STATE_3 = 8
    // MAXIMUM_SPEED = 9
    // BOOST_SPEED = 15

    /* Faktor mempercepat kecepatan
    1. Mempercepat:
        - Use_Boost
        - Accelerate
    2. Memperlambat:
        - 
    */
    private void speedChange(int lane){ // gery
        int indexLane = lane - 1;
        int affectSpeed = 0;

        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            switch(temp){
                case "Nothing": 

                case "Accelerate":

                case "Decelerate":

                case "Turn_Right":
                    
                case "Turn_Left":
                    
                case "Use_Boost":
                    
                case "Use_Lizard":
                    
                case "Use_Oil":
                    
                case "Use_EMP":
                    
            }
        }
    }

    private void powerUp(Car myCar, ArrayList<ArrayList<Lane>>available){ 
        for(int i = 0; i < AllCommand.size(); i ++){
            double count = 0.0;
            switch(AllCommand.get(i).getCommand()){
                case "Nothing":
                case "Accelerate":
                case "Decelerate":
                case "Use_Oil":
                case "Use_EMP":
                case "Use_Boost":
                    count = pwScoreinLane(available, myCar.position.lane - 1, myCar.speed +5, 6);
                    break;
                case "Turn Right":
                    count = pwScoreinLane(available, myCar.position.lane, myCar.speed + 4, 6);
                    break;
                case "Turn Left":
                    count  = pwScoreinLane(available, myCar.position.lane - 2, myCar.speed + 4, 6);
                    break;
                case "Use_Lizard":
                    Terrain curr = available.get(myCar.position.lane - 1).get(myCar.speed + 5).terrain;
                    if(curr == Terrain.OIL_POWER){
                        count = oilval;
                    }else if(curr== Terrain.BOOST){
                        count = boostval;
                    }else if(curr == Terrain.EMP){
                        count = empval;
                    }else if(curr == Terrain.LIZARD){
                        count = lizardval;
                    }else if(curr == Terrain.TWEET){
                        count = tweetval;
                    }
                    break;
            }
            AllCommand.get(i).addValue(count);
        }
    }

    private double pwScoreinLane(ArrayList<ArrayList<Lane>>available, int lane, int finalblock, int startblock){
        double count = 0.0;
        for(int j = startblock; j < finalblock; j ++){
            Terrain curr = available.get(lane).get(j).terrain;
            if(curr == Terrain.OIL_POWER){
                count += oilval;
            }else if(curr== Terrain.BOOST){
                count += boostval;
            }else if(curr == Terrain.EMP){
                count += empval;
            }else if(curr == Terrain.LIZARD){
                count += lizardval;
            }else if(curr == Terrain.TWEET){
                count += tweetval;
            }
        }
        return count;
    }

    private void maxSpeedChange(int damage, int lane, int block, ArrayList<ArrayList<Lane>>available) {
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            int countdamage = 0;
            switch (temp) {
                case "Nothing":
                    for (int j = block; j < available.get(lane-1).size(); j++) {
                        if (available.get(lane-1).get(j).terrain == Terrain.MUD || available.get(lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(lane-1).get(j).terrain == Terrain.WALL) {
                            countdamage++;
                        }
                    }
                    break;
                case "Accelerate":
                    for (int j = block; j < available.get(lane-1).size(); j++) {
                        if (available.get(lane-1).get(j).terrain == Terrain.MUD || available.get(lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(lane-1).get(j).terrain == Terrain.WALL) {
                            countdamage++;
                        }
                    }
                    break;
            }
        }
    }

    private void bonusPoint(int damage, int lane, ArrayList<ArrayList<Lane>>available, int enemyLane, int block){
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
                        this.AllCommand.get(i).addValue(BonusScore*(-1000.0));
                        break;
                    }
                case "Turn_Right":
                    this.AllCommand.get(i).addValue(0);
                    break;
                case "Turn_Left":
                    this.AllCommand.get(i).addValue(0);
                    break;
                case "Use_Boost":
                    if (damage == 0) {
                        this.AllCommand.get(i).addValue(BonusScore*(300.0));
                    }
                    if (damage == 1) {
                        this.AllCommand.get(i).addValue(BonusScore*(200.0));
                    }
                    break;
                case "Use_Lizard":
                    int many = 0;
                    for (int j = block; j < available.get(lane-1).size(); j++) {
                        if (available.get(lane-1).get(j).terrain == Terrain.MUD || available.get(lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(lane-1).get(j).terrain == Terrain.WALL) {
                            many++;
                        }
                    }
                    int loc = available.get(lane-1).size();
                    if (many >= 4 && (available.get(lane-1).get(loc).terrain == Terrain.EMPTY || available.get(lane-1).get(loc).terrain == Terrain.OIL_POWER|| available.get(lane-1).get(loc).terrain == Terrain.BOOST || available.get(lane-1).get(loc).terrain == Terrain.LIZARD || available.get(lane-1).get(loc).terrain == Terrain.EMP || available.get(lane-1).get(loc).terrain == Terrain.TWEET)) {
                        this.AllCommand.get(i).addValue(BonusScore*5);
                    }
                    break;
                case "Use_Oil":
                    if (lane == enemyLane) {
                        int numLane = 0;
                        int obstacles = 0;
                        if (lane - 1 != 0) {
                            for (int j = 0; j < available.get(lane-2).size(); j++) {
                                if (available.get(lane-2).get(j).terrain == Terrain.MUD || available.get(lane-2).get(j).terrain == Terrain.OIL_SPILL || available.get(lane-2).get(j).terrain == Terrain.WALL) {
                                    obstacles++;
                                }
                            }
                            numLane += 2;
                        }
                        if (lane + 1 != 5) {
                            for (int j = 0; j < available.get(lane-2).size(); j++) {
                                if (available.get(lane+2).get(j).terrain == Terrain.MUD || available.get(lane+2).get(j).terrain == Terrain.OIL_SPILL || available.get(lane+2).get(j).terrain == Terrain.WALL) {
                                    obstacles++;
                                }
                            }
                            numLane += 2;
                        }
                        for (int j = 0; j < available.get(lane-2).size(); j++) {
                            if (available.get(lane-1).get(j).terrain == Terrain.MUD || available.get(lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(lane-1).get(j).terrain == Terrain.WALL || available.get(lane-1).get(j).OccupiedByCyberTruck) {
                                obstacles++;
                            }
                        }
                        numLane++;
                        if (numLane == 5) {
                            if (obstacles > 5) {
                                this.AllCommand.get(i).addValue(BonusScore*8.0);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*6.0);
                            }
                        }
                        else if (numLane == 4) {
                            if (obstacles > 5) {
                                this.AllCommand.get(i).addValue(BonusScore*7.0);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*5.0);
                            }
                        }
                        else if (numLane == 3) {
                            if (obstacles > 3) {
                                this.AllCommand.get(i).addValue(BonusScore*6.0);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*4.0);
                            }
                        }
                        else if (numLane == 2) {
                            if (obstacles > 3) {
                                this.AllCommand.get(i).addValue(BonusScore*5.0);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*4.0);
                            }
                        }
                        else if (numLane == 1) {
                            if (obstacles > 3) {
                                this.AllCommand.get(i).addValue(BonusScore*4.0);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*2.0);
                            }
                        }
                        else {
                            this.AllCommand.get(i).addValue(0);
                        }
                    }
                    break;
                case "Use_EMP": 
                    if (lane == enemyLane) {
                        this.AllCommand.get(i).addValue(BonusScore*9);
                    }
                    break;
            }
        }
    }

    private void scoreChange(){
        
    }
}

