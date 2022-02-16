package za.co.entelect.challenge;

public class Value{
    private
        String command;
        double score;

    
    public Value(String command, double score){
        this.command = command;
        this.score = score;
    }

    public Value(String command){
        this.command = command;
        this.score = 0f;
    }
    
    public double getValue(){
        return this.score;
    }

    public void addValue(double newScore){
        this.score += newScore;
    }

    public String getCommand(){
        return this.command;
    }

}

