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
    
    private final double muddamage = 1.0;
    private final double oildamage = 1.0;
    private final double walldamage = 2.0;
    private final double truckdamage = 2.0;
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

        shiftColumn(speed, myCar, Available);
        speedChange(speed, myCar, Available);
        powerUp(myCar, Available, speed);
        maxSpeedChange(myCar, Available, speed);
        bonusPoint(myCar, opponent, Available, speed);
        scoreChange(myCar, Available, speed);

        Collections.sort(AllCommand, (a, b)->{
            return Double.compare(a.getValue(), b.getValue());
        });

        return AllCommand.get(0).getCommand();
    }

    private void shiftColumn(Speed speed, Car myCar, ArrayList<ArrayList<Lane>> available){
        System.out.println("Column");
        System.out.println(available.size());
        System.out.println(available.get(0).size());
        // Belum pertimbangin collision ke mobil
        int lane = myCar.position.lane;
        
        int indexLane = lane - 1;
        double truckStraight = 0, truckRight = 0, truckLeft = 0, truckBoost = 0, truckAccelerate = 0, truckDecelerate = 0;
        boolean boolStraight = false, boolRight = false, boolLeft = false, boolBoost = false, boolAccelerate = false, boolDecelerate = false;

        int currentSpeed = speed.getCurrentSpeed();
        int curr_max_speed = speed.getMaxSpeed();
        int speed_after_accelerate = speed.getAccelerate();
        int speed_after_decelerate = speed.getDecelerate();

        double curr1 = (double)currentSpeed * ShiftColumn;
        double curr2 = (double)(currentSpeed - 1) * ShiftColumn;
        double curr3 = (double)(curr_max_speed) * ShiftColumn;
        double curr4 = (double)(speed_after_accelerate) * ShiftColumn;
        double curr5 = (double)(speed_after_decelerate) * ShiftColumn;

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
        if(indexLane != 3) {
            for(int i = 6; i < (currentSpeed + 5); i++) {
                if(available.get(indexLane + 1).get(i).OccupiedByCyberTruck) {
                    truckRight = i;
                    truckRight -= 6;
                    truckRight *= ShiftColumn;
                    boolRight = true;
                    break;
                }
            }
        }
        // Kiri
        if(indexLane != 0) {
            for(int i = 6; i < (currentSpeed + 5); i++) {
                if(available.get(indexLane - 1).get(i).OccupiedByCyberTruck) {
                    truckLeft = i;
                    truckLeft -= 6;
                    truckLeft *= ShiftColumn;
                    boolLeft = true;
                    break;
                }
            }
        }

        /* Case 2: Pake use boost */
        for(int i = 6; i < (curr_max_speed + 6); i++) {
            System.out.println(available.get(0).size());
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

    private void speedChange(Speed speed, Car myCar, ArrayList<ArrayList<Lane>> available){ // gery
        int indexLane = myCar.position.lane - 1;
        int curr_max_speed = speed.getMaxSpeed();
        int currentSpeed = speed.getCurrentSpeed();
        double speedTempStraight, speedTempRight, speedTempLeft, speedTempBoost, speedTempAccelerate, speedTempDecelerate;
        
        speedTempStraight = itterateLane(available, indexLane, (currentSpeed + 6), 6, 3, speed);
        speedTempRight = itterateLane(available, (indexLane + 1), (currentSpeed + 5), 6, 3, speed);
        speedTempLeft = itterateLane(available, (indexLane - 1), (currentSpeed + 5), 6, 3, speed);
        speedTempBoost = itterateLane(available, indexLane, (curr_max_speed + 6), 6, 3, speed);
        speedTempAccelerate = itterateLane(available, indexLane, (speed.getAccelerate() + 6), 6, 3 , speed );
        speedTempDecelerate = itterateLane(available, indexLane, (speed.getDecelerate() + 6), 6, 3 , speed );


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
                    if(available.get(indexLane).get(currentSpeed + 5).OccupiedByCyberTruck || available.get(indexLane).get(currentSpeed + 5).terrain == Terrain.WALL) {
                        AllCommand.get(i).addValue((3 - currentSpeed) * speedChange);
                    } else if (available.get(indexLane).get(currentSpeed + 5).terrain == Terrain.MUD || available.get(indexLane).get(currentSpeed + 5).terrain == Terrain.OIL_SPILL) {
                        AllCommand.get(i).addValue((speed.getAccelerate() - currentSpeed) * speedChange);
                    } else {
                        AllCommand.get(i).addValue(speedTempStraight * speedChange);
                    }
            }
        }
    }

    private void powerUp(Car myCar, ArrayList<ArrayList<Lane>>available, Speed speed){ 
        for(int i = 0; i < AllCommand.size(); i ++){
            double count = 0.0;
            switch(AllCommand.get(i).getCommand()){
                case "Nothing":
                case "Use_Oil":
                case "Use_EMP":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getCurrentSpeed() +6, 6, 1, speed);
                    break;
                case "Turn Right":
                    count = itterateLane(available, myCar.position.lane, speed.getCurrentSpeed() + 5, 6, 1, speed);
                    break;
                case "Turn Left":
                    count  = itterateLane(available, myCar.position.lane - 2, speed.getCurrentSpeed() + 5, 6, 1, speed);
                    break;
                case "Accelerate":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getAccelerate() + 6, 6, 1, speed);
                    break;
                case "Decelerate":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getDecelerate() + 6, 6, 1, speed);
                    break;
                case "Use_Boost":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getMaxSpeed() + 6, 6, 1, speed);
                    break;
                case "Use_Lizard":
                    Terrain curr = available.get(myCar.position.lane - 1).get(speed.getCurrentSpeed() + 5).terrain;
                    count = changepwScore(curr);
                    break;
            }
            AllCommand.get(i).addValue(count * PowerUp);
        }
    }

    private double itterateLane(ArrayList<ArrayList<Lane>>available, int lane, int finalblock, int startblock, int aggregate, Speed speed){
        /* 
            Agrregate:
                1 -> powerup
                2 -> maxSpeedchange
                3 -> speedChange
        */

        double count = 0.0;
        int speedcount = 0;
        boolean speedbreak = false;
        for(int j = startblock; j < finalblock; j ++){
            if(available.get(lane).get(j).OccupiedByCyberTruck){
                if(aggregate == 2){
                    count += truckdamage;
                }else if(aggregate == 3){
                    count = 3.0;
                    speedbreak = true;
                }
                break;
            }
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
                        count = 3.0;
                        speedbreak = true;
                    }
                    break;
            }

            if(aggregate == 3 && speedbreak){
                break;
            }
        }
        
        if(!speedbreak && aggregate == 3){
            count = (double)speed.getMultipleDecelerate(speedcount);
        }
        return count;
    }

    private double changepwScore(Terrain curr){
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
            return 0.0;
        }
    }

    private double obstaclesPenalty(Terrain curr){
        if(curr == Terrain.MUD){
            return muddamage;
        }else if(curr == Terrain.OIL_SPILL){
            return oildamage;
        }else if(curr == Terrain.WALL){
            return walldamage;
        }else{
            return 0.0;
        }
    }

    private void maxSpeedChange(Car myCar, ArrayList<ArrayList<Lane>>available, Speed speed) {
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            double count = 0.0;
            switch (temp) {
                case "Nothing":
                case "Use_Oil":
                case "Use_EMP":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getCurrentSpeed() + 6, 6, 2, speed);
                    break;
                case "Turn Right":
                    count = itterateLane(available, myCar.position.lane, speed.getCurrentSpeed() + 5, 6, 2, speed);
                    break;
                case "Turn Left":
                    count  = itterateLane(available, myCar.position.lane - 2, speed.getCurrentSpeed() + 5, 6, 2, speed);
                    break;
                case "Accelerate":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getAccelerate() + 6, 6, 2, speed);
                    break;
                case "Decelerate":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getDecelerate() + 6, 6, 2, speed);
                    break;
                case "Use_Boost":
                    count = itterateLane(available, myCar.position.lane - 1, speed.getMaxSpeed() + 6, 5, 2, speed);
                    break;
                case "Use_Lizard":
                    Lane curr = available.get(myCar.position.lane - 1).get(speed.getCurrentSpeed() + 5);
                    count = obstaclesPenalty(curr.terrain);

                    if(curr.OccupiedByCyberTruck){
                        count += truckdamage;
                    }
                    break;
            }
            if(count > 5){
                count  = 5;
            }
            AllCommand.get(i).addValue((-count) * MaxSpeed);
        }
    }

    private void bonusPoint(Car myCar, Car opponent, ArrayList<ArrayList<Lane>>available, Speed speed){
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
                    if (myCar.damage == 0) {
                        this.AllCommand.get(i).addValue(BonusScore*(300.0));
                    }
                    if (myCar.damage == 1) {
                        this.AllCommand.get(i).addValue(BonusScore*(200.0));
                    }
                    break;
                case "Use_Lizard":
                    int many = 0;
                    int lookOpp = speed.getCurrentSpeed();
                    for (int j = myCar.position.block; j < available.get(myCar.position.lane-1).size(); j++) {
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
                            if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL || available.get(myCar.position.lane-1).get(j).terrain == Terrain.WALL || available.get(myCar.position.lane-1).get(j).OccupiedByCyberTruck) {
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
                    if (myCar.position.lane == opponent.position.lane) {
                        this.AllCommand.get(i).addValue(BonusScore*9);
                    }
                    break;
            }
        }
    }

    private void scoreChange(Car myCar, ArrayList<ArrayList<Lane>> available, Speed speed){
        double getMud = 3*(-1);
        double getOil = 4*(-1);
        double boost = 4;

        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();

            int checking = 0;
            double counting = 0;
            switch (temp) {
                case "Nothing":
                    checking = speed.getCurrentSpeed();
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting);
                    break;
                case "Accelerate":
                    checking = speed.getAccelerate();
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting);
                    break;
                case "Decelerate":
                    checking = speed.getDecelerate();
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting);
                    break;
                case "Turn_Left":
                    checking = speed.getCurrentSpeed()-1;
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane-2).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-2).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-2).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-2).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-2).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting);
                    break;
                case "Turn_Right":
                    checking = speed.getCurrentSpeed()-1;
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting);
                    break;
                case "Use_Boost":
                    checking = speed.getBoost();
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*(counting+4));
                    break;
                case "Use_Oil":
                    checking = speed.getCurrentSpeed();
                    for (int j = myCar.position.block+1; j <= myCar.position.block+checking; j++) {
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
                            counting += getMud;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
                            counting += getOil;
                        }
                        if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
                            counting += boost;
                        }
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting+4);
                    break;
                case "Use_Lizard":
                    checking = speed.getCurrentSpeed();
                    int j = myCar.position.block+checking;
                    if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.MUD) {
                        counting += getMud;
                    }
                    if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_SPILL) {
                        counting += getOil;
                    }
                    if (available.get(myCar.position.lane-1).get(j).terrain == Terrain.BOOST || available.get(myCar.position.lane-1).get(j).terrain == Terrain.OIL_POWER || available.get(myCar.position.lane-1).get(j).terrain == Terrain.LIZARD || available.get(myCar.position.lane-1).get(j).terrain == Terrain.EMP || available.get(myCar.position.lane-1).get(j).terrain == Terrain.TWEET) {
                        counting += boost;
                    }
                    this.AllCommand.get(i).addValue(ScoreChanged*counting+4);
                    break;
            }
        }
    }
}

