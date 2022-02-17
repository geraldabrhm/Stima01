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

        shiftColumn(myCar.speed, myCar.position.lane, Available, myCar.damage, myCar.boosting);
        speedChange(myCar.position.lane, myCar.speed, Available);
        powerUp(myCar, Available);
        maxSpeedChange(myCar.damage, myCar.position.lane, myCar.position.block, Available);
        bonusPoint(myCar.damage, myCar.position.lane, Available, opponent.position.lane, myCar.position.block);
        scoreChange();

        Collections.sort(AllCommand, (a, b)->{
            return Double.compare(a.getValue(), b.getValue());
        });

        return AllCommand.get(0).getCommand();
    }

    private void shiftColumn(int currentSpeed, int lane, ArrayList<ArrayList<Lane>> available, int damage, boolean boosting){
        // Belum pertimbangin collision ke mobil
        int indexLane = lane - 1, curr_max_speed = 0, speed_after_accelerate = 0, speed_after_decelerate = 0;
        double truckStraight = 0, truckRight = 0, truckLeft = 0, truckBoost = 0, truckAccelerate = 0, truckDecelerate = 0;
        boolean boolStraight = false, boolRight = false, boolLeft = false, boolBoost = false, boolAccelerate = false, boolDecelerate = false;

        switch (damage) {
            case 0:
                curr_max_speed = 15;
            case 1:
                curr_max_speed = 9;
            case 2:
                curr_max_speed = 8;
            case 3:
                curr_max_speed = 6;
            case 4:
                curr_max_speed = 3;
            case 5:
                curr_max_speed = 0;
        }

        switch (currentSpeed) {
            case 0:
                speed_after_accelerate = 3;
                speed_after_decelerate = 0;
            case 3:
                speed_after_accelerate = 5;
                speed_after_decelerate = 0;
            case 5:
                speed_after_accelerate = 6;
                speed_after_decelerate = 3;
            case 6:
                speed_after_accelerate = 8;
                speed_after_decelerate = 5;
            case 8:
                speed_after_accelerate = 9;
                speed_after_decelerate = 6;
            case 9:
                speed_after_accelerate = 9;
                speed_after_decelerate = 8;
            case 15:
                if(boosting) {
                    speed_after_accelerate = 15;
                    speed_after_decelerate = 9;
                } else {
                    speed_after_accelerate = 9;
                    speed_after_decelerate = 9;
                }
        }

        if (speed_after_accelerate > curr_max_speed && !boosting) {
            speed_after_accelerate = curr_max_speed;
        }

        double curr1 = currentSpeed * ShiftColumn;
        double curr2 = (currentSpeed - 1) * ShiftColumn;
        double curr3 = (curr_max_speed) * ShiftColumn;
        double curr4 = (speed_after_accelerate) * ShiftColumn;
        double curr5 = (speed_after_decelerate) * ShiftColumn;

        /* Case 1: Gak use boost */
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
        // Kanan -- Nanti digabung aja sama kiri
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

        /* Case 2: Pake use boost */
        for(int i = 6; i < (curr_max_speed + 6); i++) {
            if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
                truckBoost = i;
                truckBoost -= 6;
                truckBoost *= ShiftColumn;
                boolBoost = true;
                break;
            }
        }

        /* Case 3: Pake accelerate */
        for(int i = 6; i < (speed_after_accelerate + 6); i++) {
            if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
                truckAccelerate = i;
                truckAccelerate -= 6;
                truckAccelerate *= ShiftColumn;
                boolAccelerate = true;
                break;
            }
        }

        /* Case 4: Pake decelerate */
        for(int i = 6; i < (speed_after_decelerate + 6); i++) {
            if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
                truckDecelerate = i;
                truckDecelerate -= 6;
                truckDecelerate *= ShiftColumn;
                boolDecelerate = true;
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
                    if(boolAccelerate) {
                        this.AllCommand.get(i).addValue(truckAccelerate);
                    } else {
                        this.AllCommand.get(i).addValue(curr4);
                    }
                case "Decelerate":
                    if(boolDecelerate) {
                        this.AllCommand.get(i).addValue(truckDecelerate);
                    } else {
                        this.AllCommand.get(i).addValue(curr5);
                    }
                case "Turn_Right":
                    if(boolRight) {
                        this.AllCommand.get(i).addValue(truckRight);
                    } else {
                        this.AllCommand.get(i).addValue(curr2);
                    }
                case "Turn_Left":
                    if(boolLeft) {
                        this.AllCommand.get(i).addValue(truckLeft);
                    } else {
                        this.AllCommand.get(i).addValue(curr2);
                    }
                case "Use_Boost":
                    if(boolBoost) {
                        this.AllCommand.get(i).addValue(truckBoost);
                    } else {
                        this.AllCommand.get(i).addValue(curr3);
                    }
                case "Use_Lizard": // Nanti dibenerin, khasus khusus soalnya
                    if(available.get(indexLane).get(currentSpeed + 5).OccupiedByCyberTruck) {
                        this.AllCommand.get(i).addValue(curr2);
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

    /* Mud */
    // SPEED_STATE_1 => SPEED_STATE_1
    // INITIAL_SPEED => SPEED_STATE_1
    // SPEED_STATE_2 => SPEED_STATE_1
    // SPEED_STATE_3 => SPEED_STATE_2
    // MAXIMUM_SPEED => SPEED_STATE_3
    // BOOST_SPEED => MAXIMUM_SPEED

    /* Wall */ // Langsung ke SPEED_STATE_1


    /* Faktor mempercepat kecepatan
    1. Mempercepat:
        - Use_Boost
        - Accelerate
    2. Memperlambat:
        - Oil
        - Mud
        - Wall
        - Collision dengan opponent car
    */
    private void speedChange(int lane, int currentSpeed, ArrayList<ArrayList<Lane>> available){ // gery
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

    private void maxSpeedChange(Car myCar, int damage, int lane, int block, ArrayList<ArrayList<Lane>>available) {
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            int countdamage = 0;
            int speed;
            switch (temp) {
                case "Nothing":
                    speed = getSpeedApprox(myCar.speed, temp);
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

    // private int getSpeedApprox(Car myCar, String command) {
    //     int speed0 = 0;
    //     int speed1 = 3;
    //     int speed2 = 5;
    //     int speed3 = 6;
    //     int speed4 = 8;
    //     int speed5 = 9;
    //     int speed6 = 15;
    //     int[] speedArr = new int[]{speed0, speed1, speed2, speed3, speed4, speed5, speed6};
        
    //     int speednow = 5;
    //     int indexSpeed = 0;

    //     switch (myCar.speed) {
    //         case 0:
    //             indexSpeed = 0;
    //             break;
    //         case 3:
    //             indexSpeed = 1;
    //             break;
    //         case 5:
    //             indexSpeed = 2;
    //             break;
    //         case 6:
    //             indexSpeed = 3;
    //             break;
    //         case 8:
    //             indexSpeed = 4;
    //             break;
    //         case 9:
    //             indexSpeed = 5;
    //             break;
    //         case 15:
    //             indexSpeed = 6;
    //             break;
    //     }

    //     switch (command) {
    //         case "Nothing":
    //             speednow = indexSpeed;
    //             break;
    //         case "Accelerate":
    //             speednow = indexSpeed+1;
    //             if (speednow == 6) {
    //                 speednow = 5;
    //             }
    //             break;
    //         case "Decelerate":
    //             speednow = indexSpeed-1;
    //             if (speednow == -1) {
    //                 speednow = 0;
    //             }
    //             break;
    //         case "Turn_Left":
    //             speednow = indexSpeed;
    //             break;
    //         case "Turn_Right":
    //             speednow = indexSpeed;
    //             break;
    //         case "Use_Boost":
    //             speednow = 6;
    //             break;
    //         case "Use_Oil":
    //             speednow = indexSpeed;
    //             break;
    //     }
    //     int optSpeed = speedArr[speednow];
    //     return optSpeed;
    // }

    // private int damageSpeed(Car myCar) {
    //     int maks = 15;
    //     switch (myCar.damage) {
    //         case 1:
    //             maks = 9;
    //             break;
    //         case 2:
    //             maks = 8;
    //             break;
    //         case 3:
    //             maks = 6;
    //             break;
    //         case 4:
    //             maks = 3;
    //             break;
    //         case 5:
    //             maks = 0;
    //             break;
    //     }

        return maks;
    }
}

