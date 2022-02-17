package za.co.entelect.challenge;

public class Value{
    private
        String command;
        int score;

    
    public Value(String command, int score){
        this.command = command;
        this.score = score;
    }

    public Value(String command){
        this.command = command;
        this.score = 0;
    }
    
    public int getValue(){
        return this.score;
    }

    public void addValue(int newScore){
        this.score += newScore;
    }

    public String getCommand(){
        return this.command;
    }

}

