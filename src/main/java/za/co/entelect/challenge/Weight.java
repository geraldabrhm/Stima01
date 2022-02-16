package za.co.entelect.challenge;

import za.co.entelect.challenge.command.*;
import za.co.entelect.challenge.entities.*;
import za.co.entelect.challenge.enums.PowerUps;
import za.co.entelect.challenge.enums.Terrain;
import za.co.entelect.challenge.Value;

import java.util.*;

public class Weight{
    private ArrayList<Value> AllCommand;

    private final Float ShiftColumn = 22.5f;
    private final Float speedChange = 25.0f;
    private final Float PowerUp = 20.0f;
    private final Float MaxSpeed = 22.5f;
    private final Float ScoreChanged = 10.0f;
    private final Float BonusScore = 22.5f;
    // Hybrid: nothing accelerate decelerate turnright turnleft useboost uselizard // fix usetweet
    // Depan: useemp
    // Belakang: useoil

    //Leading -> 1
    //Behind -> 2
    public Weight(ArrayList<Value>WeightList){
        this.AllCommand = WeightList;
    }

    public Command bestCommand(int currentSpeed, int damage, ArrayList<ArrayList<Terrain>>Available){
        // * * Each Lane -> Visible Lane 
        // * * For example Lane 1 -> Visible Lane in Lane 1 (Top One), from behind car (5 Block) until achieveable block in this round
        
        shiftColumn(currentSpeed);
        speedChange();
        powerUp();
        maxSpeedChange(damage);
        bonusPoint(damage);
        return new DoNothingCommand();
    }

    private void shiftColumn(int currentSpeed){
        float cast_currentSpeed = currentSpeed;
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();
            
            switch(temp){
                case "Nothing": // Copy
                    this.AllCommand.get(i).addValue(cast_currentSpeed);
                    // CurrentSpeed - Akumulasi of degradasi speed oleh rintangan
                case "Accelerate": 
                    // Current speed + (perubahan speed karena kenaikan speed_state) - Akumulasi of degr ...
                case "Decelerate":
                    // current speed - sama kek atas
                case "Turn_Right":
                    // current speed - Akumulasi of degradasi di lane sebela - 1
                case "Turn_Left":
                case "Use_Boost":
                case "Use_Lizard":
                case "Use_Oil":
                case "Use_EMP":
            }
        }
    }

    private void speedChange(){

    }

    private void powerUp(){
        
    }

    private void maxSpeedChange(int damage) {
        
    }

    private void bonusPoint(int damage){
        for(int i = 0; i < AllCommand.size(); i ++){
            String temp = this.AllCommand.get(i).getCommand();
            
            Float bonus = 0.0f;
            switch(temp) {
                case "Nothing":
                    break;
                case "Accelerate":
                    break;
                case "Decelerate":
                    if (temp == "Use_Boost") {
                        bonus = -1000.0f;
                        break;
                    }
                case "Turn_Right":
                    break;
                case "Turn_Left":
                    break;
                case "Use_Boost":
                    if (damage == 0) {
                        bonus = 300.0f;
                    }
                    if (damage == 1) {
                        bonus = 200.f;
                    }
                    break;
                case "Use_Lizard":
                    break;
                case "Use_Oil":
                    break;
                case "Use_EMP": 
                    break;
            }
        }
    }

    private void scoreChange(){

    }
}