package za.co.entelect.challenge;
import java.lang.Float;

public class Value{
    private
        String command;
        Float score;

    
    public Value(String command, Float score){
        this.command = command;
        this.score = score;
    }

    public Value(String command){
        this.command = command;
        this.score = 0f;
    }
    
    public Float getValue(){
        return this.score;
    }

    public void addValue(Float newScore){
        this.score = newScore;
    }
}
