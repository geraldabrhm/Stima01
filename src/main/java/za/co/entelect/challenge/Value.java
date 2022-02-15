package za.co.entelect.challenge;

public class Value{
    private
        String command;
        int value;

    
    public Value(String command, int value){
        this.command = command;
        this.value = value;
    }

    public Value(String command){
        this.command = command;
        this.value = 0;
    }
    
    public int getValue(){
        return this.value;
    }

    public void addValue(int newValue){
        this.value = newValue;
    }
    
}
