package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
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
    private final int MaxSpeed = 22;
    // private final int ScoreChanged = 10;
    private final int BonusScore = 22;


    private final int empval = 7;
    private final int tweetval = 5;
    private final int lizardval = 5;
    private final int boostval = 6;
    private final int oilval = 3;
    
    private final int muddamage = 1;
    private final int oildamage = 1;
    private final int walldamage = 2;
    // Hybrid: nothing accelerate decelerate turnright turnleft useboost uselizard // fix usetweet
    // Depan: useemp
    // Belakang: useoil

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
        // maxSpeedChange(myCar, Available, speed);
        bonusPoint(myCar, opponent, Available, speed);
        // scoreChange(myCar, Available, speed);

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
        // Belum pertimbangin collision ke mobil
        // int lane = myCar.position.lane;
        
        // int indexLane = lane - 1;

        // int currentSpeed = speed.getCurrentSpeed();
        // int curr_max_speed = speed.getMaxSpeed();
        // int speed_after_accelerate = speed.getAccelerate();
        // int speed_after_decelerate = speed.getDecelerate();
        // int start = this.startAll;
        // double curr1 = (double)currentSpeed * ShiftColumn;
        // double curr2 = (double)(currentSpeed - 1) * ShiftColumn;
        // double curr3 = (double)(curr_max_speed) * ShiftColumn;
        // double curr4 = (double)(speed_after_accelerate) * ShiftColumn;
        // double curr5 = (double)(speed_after_decelerate) * ShiftColumn;


        // /* Case 1: Gak use boost */
        // // Lurus
        // for(int i = start; i < (currentSpeed + start); i++) {
        //     if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
        //         truckStraight = i;
        //         truckStraight -= 6;
        //         truckStraight *= ShiftColumn;
        //         boolStraight = true;
        //         break;
        //     }
        // }
        // // Kanan -- Nanti digabung aja sama kiri
        // if(indexLane != 3) {
        //     for(int i = start; i < (currentSpeed + start - 1); i++) {
        //         if(available.get(indexLane + 1).get(i).OccupiedByCyberTruck) {
        //             truckRight = i;
        //             truckRight -= 6;
        //             truckRight *= ShiftColumn;
        //             boolRight = true;
        //             break;
        //         }
        //     }
        // }
        // // Kiri
        // if(indexLane != 0) {
        //     for(int i = start; i < (currentSpeed + start -1); i++) {
        //         if(available.get(indexLane - 1).get(i).OccupiedByCyberTruck) {
        //             truckLeft = i;
        //             truckLeft -= 6;
        //             truckLeft *= ShiftColumn;
        //             boolLeft = true;
        //             break;
        //         }
        //     }
        // }

        // /* Case 2: Pake use boost */
        // for(int i = start; i < (curr_max_speed + start); i++) {
        //     System.out.println(available.get(0).size());
        //     if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
        //         truckBoost = i;
        //         truckBoost -= 6;
        //         truckBoost *= ShiftColumn;
        //         boolBoost = true;
        //         break;
        //     }
        // }

        // /* Case 3: Pake accelerate */
        // for(int i = start; i < (speed_after_accelerate + start); i++) {
        //     if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
        //         truckAccelerate = i;
        //         truckAccelerate -= 6;
        //         truckAccelerate *= ShiftColumn;
        //         boolAccelerate = true;
        //         break;
        //     }
        // }

        // /* Case 4: Pake decelerate */
        // for(int i = start; i < (speed_after_decelerate + start); i++) {
        //     if(available.get(indexLane).get(i).OccupiedByCyberTruck) {
        //         truckDecelerate = i;
        //         truckDecelerate -= 6;
        //         truckDecelerate *= ShiftColumn;
        //         boolDecelerate = true;
        //         break;
        //     }
        // }
        
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            switch(temp){
                case "Nothing":
                    this.AllCommand.get(i).addValue(speed.getCurrentSpeed() * ShiftColumn);
                    break;
                    // if(boolStraight) {
                    //     this.AllCommand.get(i).addValue(truckStraight);
                    // } else {
                    //     this.AllCommand.get(i).addValue(curr1);
                    // }
                case "Accelerate":
                    this.AllCommand.get(i).addValue(speed.getAccelerate() * ShiftColumn);
                    break;
                    // if(boolAccelerate) {
                    //     this.AllCommand.get(i).addValue(truckAccelerate);
                    // } else {
                    //     this.AllCommand.get(i).addValue(curr4);
                    // }
                case "Decelerate":
                    // if(boolDecelerate) {
                    //     this.AllCommand.get(i).addValue(truckDecelerate);
                    // } else {
                    this.AllCommand.get(i).addValue(speed.getDecelerate() * ShiftColumn);
                    break;
                    // }
                case "Turn_Right":
                    // if(boolRight) {
                    //     this.AllCommand.get(i).addValue(truckRight);
                    // } else {
                    this.AllCommand.get(i).addValue((speed.getCurrentSpeed() - 1) * ShiftColumn);
                    break;
                    // }
                case "Turn_Left":
                    // if(boolLeft) {
                    //     this.AllCommand.get(i).addValue(truckLeft);
                    // } else {
                    this.AllCommand.get(i).addValue((speed.getCurrentSpeed() - 1) * ShiftColumn);
                    break;
                    // }
                case "Use_Boost":
                    // if(boolBoost) {
                    //     this.AllCommand.get(i).addValue(truckBoost);
                    // } else {
                    this.AllCommand.get(i).addValue(speed.getBoost() * ShiftColumn);
                    break;
                    // }
                case "Use_Lizard": // Nanti dibenerin, khasus khusus soalnya
                    // if(available.get(indexLane).get(currentSpeed + start).OccupiedByCyberTruck) {
                    //     this.AllCommand.get(i).addValue(curr2);
                    // } else {
                    this.AllCommand.get(i).addValue(speed.getCurrentSpeed() * ShiftColumn);
                    break;
                    // }
                case "Use_Oil":
                    // if(boolStraight) {
                    //     this.AllCommand.get(i).addValue(truckStraight);
                    // } else {
                    this.AllCommand.get(i).addValue(speed.getCurrentSpeed() * ShiftColumn);
                    break;
                    // }
                case "Use_EMP":
                    // if(boolStraight) {
                    //     this.AllCommand.get(i).addValue(truckStraight);
                    // } else {
                    this.AllCommand.get(i).addValue(speed.getCurrentSpeed() * ShiftColumn);
                    break;
                    // }
            }
        }
    }

    private void speedChange(Speed speed, Car myCar, ArrayList<ArrayList<Lane>> available){ // gery
        int indexLane = myCar.position.lane - 1;
        int curr_max_speed = speed.getMaxSpeed();
        int currentSpeed = speed.getCurrentSpeed();
        int start = this.startAll;
        int speedTempStraight, speedTempRight=0, speedTempLeft=0, speedTempBoost, speedTempAccelerate, speedTempDecelerate;
        
        speedTempStraight = itterateLane(available, indexLane, (currentSpeed + start), start, 3, speed);
        if(indexLane != 3) {
            speedTempRight = itterateLane(available, (indexLane + 1), (currentSpeed + start - 1), start, 3, speed);
        }
        if(indexLane != 0) {
            speedTempLeft = itterateLane(available, (indexLane - 1), (currentSpeed + start - 1), start, 3, speed);
        }
        speedTempBoost = itterateLane(available, indexLane, (curr_max_speed + start), start, 3, speed);
        speedTempAccelerate = itterateLane(available, indexLane, (speed.getAccelerate() + start), start, 3 , speed );
        speedTempDecelerate = itterateLane(available, indexLane, (speed.getDecelerate() + start), start, 3 , speed );


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
                    if(/*available.get(indexLane).get(currentSpeed + start - 1).OccupiedByCyberTruck ||*/ available.get(indexLane).get(currentSpeed + 5).terrain == Terrain.WALL) {
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
                2 -> maxSpeedchange
                3 -> speedChange
        */

        int count = 0;
        int speedcount = 0;
        boolean speedbreak = false;
        for(int j = startblock; j < finalblock; j ++){
            // if(available.get(lane).get(j).OccupiedByCyberTruck){
            //     if(aggregate == 2){
            //         count += truckdamage;
            //     }else if(aggregate == 3){
            //         count = 3.0;
            //         speedbreak = true;
            //     }
            //     break;
            // }
            Terrain curr = available.get(lane).get(j).terrain;
            switch(aggregate){
                case 1:
                    count += changepwScore(curr);
                    break;
                case 2:
                    count += obstaclesPenalty(curr);
                    break;
                case 3:
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

    private int obstaclesPenalty(Terrain curr){
        if(curr == Terrain.MUD){
            return muddamage;
        }else if(curr == Terrain.OIL_SPILL){
            return oildamage;
        }else if(curr == Terrain.WALL){
            return walldamage;
        }else{
            return 0;
        }
    }

    // private void maxSpeedChange(Car myCar, ArrayList<ArrayList<Lane>>available, Speed speed) {
    //     int start = this.startAll;
    //     for(int i = 0; i < AllCommand.size(); i ++){
    //         String temp = this.AllCommand.get(i).getCommand();

    //         int count = 0;
    //         switch (temp) {
    //             case "Nothing":
    //             case "Use_Oil":
    //             case "Use_EMP":
    //                 count = itterateLane(available, myCar.position.lane - 1, speed.getCurrentSpeed() + start, start, 2, speed);
    //                 break;
    //             case "Turn Right":
    //                 count = itterateLane(available, myCar.position.lane, speed.getCurrentSpeed() + start - 1, start, 2, speed);
    //                 break;
    //             case "Turn Left":
    //                 count  = itterateLane(available, myCar.position.lane - 2, speed.getCurrentSpeed() + start - 1, start, 2, speed);
    //                 break;
    //             case "Accelerate":
    //                 count = itterateLane(available, myCar.position.lane - 1, speed.getAccelerate() + start, start, 2, speed);
    //                 break;
    //             case "Decelerate":
    //                 count = itterateLane(available, myCar.position.lane - 1, speed.getDecelerate() + start, start, 2, speed);
    //                 break;
    //             case "Use_Boost":
    //                 count = itterateLane(available, myCar.position.lane - 1, speed.getMaxSpeed() + start, start, 2, speed);
    //                 break;
    //             case "Use_Lizard":
    //                 Lane curr = available.get(myCar.position.lane - 1).get(speed.getCurrentSpeed() + start - 1);
    //                 count = obstaclesPenalty(curr.terrain);

    //                 // if(curr.OccupiedByCyberTruck){
    //                 //     count += truckdamage;
    //                 // }
    //                 break;
    //         }
    //         if(count > 5){
    //             count  = 5;
    //         }
    //         AllCommand.get(i).addValue((-count) * MaxSpeed);
    //     }
    // }

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
                    if (myCar.damage == 0) {
                        this.AllCommand.get(i).addValue(BonusScore*(300));
                    }
                    if (myCar.damage == 1) {
                        this.AllCommand.get(i).addValue(BonusScore*(200));
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
                    if (many >= 4 && (available.get(myCar.position.lane-1).get(loc).terrain == Terrain.EMPTY || available.get(myCar.position.lane-1).get(loc).terrain == Terrain.OIL_POWER|| available.get(myCar.position.lane-1).get(loc).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(loc).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(loc).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(loc).terrain == Terrain.TWEET)) {
                        this.AllCommand.get(i).addValue(BonusScore*5);
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
                                if (available.get(myCar.position.lane).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane+2).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane+2).get(j).terrain == Terrain.WALL) {
                                    obstacles++;
                                }
                            }
                            numLane += 2;
                        }
                        for (int j = 0; j < available.get(myCar.position.lane-1).size(); j++) {
                            if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane-1).get(j).terrain == Terrain.WALL) /*|| available.get(myCar.position.lane-1).get(j).OccupiedByCyberTruck)*/ {
                                obstacles++;
                            }
                        }
                        numLane++;
                        if (numLane == 5) {
                            if (obstacles > 5) {
                                this.AllCommand.get(i).addValue(BonusScore*8);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*6);
                            }
                        }
                        else if (numLane == 4) {
                            if (obstacles > 5) {
                                this.AllCommand.get(i).addValue(BonusScore*7);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*5);
                            }
                        }
                        else if (numLane == 3) {
                            if (obstacles > 3) {
                                this.AllCommand.get(i).addValue(BonusScore*6);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*4);
                            }
                        }
                        else if (numLane == 2) {
                            if (obstacles > 3) {
                                this.AllCommand.get(i).addValue(BonusScore*5);
                            } else {
                                this.AllCommand.get(i).addValue(BonusScore*4);
                            }
                        }
                        else if (numLane == 1) {
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
                    }
                    break;
            }
        }
    }

    // private void scoreChange(Car myCar, ArrayList<ArrayList<Lane>> available, Speed speed){
    //     double getMud = -3.0;
    //     double getOil = -4.0;
    //     double boost = 4.0;
    //     int start = this.startAll;

    //     for(int i = 0; i < AllCommand.size(); i ++){
    //         String temp = this.AllCommand.get(i).getCommand();

    //         int checking = 0;
    //         double counting = 0;
    //         switch (temp) {
    //             case "Nothing":
    //                 checking = speed.getCurrentSpeed();
    //                 for (int j = start; j < start+checking; j++) {
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting);
    //                 break;
    //             case "Accelerate":
    //                 checking = speed.getAccelerate();
    //                 for (int j = start; j < start + checking; j++) {
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting);
    //                 break;
    //             case "Decelerate":
    //                 checking = speed.getDecelerate();
    //                 for (int j = start; j < start+checking; j++) {
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting);
    //                 break;
    //             case "Turn_Left":
    //                 checking = speed.getCurrentSpeed()-1;
    //                 for (int j = start; j < start+checking; j++) {
    //                     if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-2).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-2).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-2).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-2).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting);
    //                 break;
    //             case "Turn_Right":
    //                 checking = speed.getCurrentSpeed()-1;
    //                 for (int j = start; j < start+checking; j++) {
    //                     if (available.get(myCar.position.lane).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting);
    //                 break;
    //             case "Use_Boost":
    //                 checking = speed.getBoost();
    //                 for (int j = start; j < start+checking; j++) {
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*(counting+4));
    //                 break;
    //             case "Use_Oil":
    //                 checking = speed.getCurrentSpeed();
    //                 for (int j = start; j < start+checking; j++) {
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
    //                         counting += getMud;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
    //                         counting += getOil;
    //                     }
    //                     if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
    //                         counting += boost;
    //                     }
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting+4);
    //                 break;
    //             case "Use_Lizard":
    //                 checking = speed.getCurrentSpeed();
    //                 int j = start+checking - 1;
    //                 if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
    //                     counting += getMud;
    //                 }
    //                 if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
    //                     counting += getOil;
    //                 }
    //                 if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
    //                     counting += boost;
    //                 }
    //                 this.AllCommand.get(i).addValue(ScoreChanged*counting+4);
    //                 break;
    //         }
    //     }
    // }
}

