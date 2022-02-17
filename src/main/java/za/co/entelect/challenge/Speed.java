package za.co.entelect.challenge;

public class Speed {
    private final static int[] speedArr = new int[]{0, 3, 5, 6, 8, 9, 15};
    private int damage;
    private int currentSpeed;
    private int maxSpeed;
    private boolean haveBoost;

    Speed(int damage, int currentSpeed, boolean haveBoost){
        this.damage = damage;
        this.currentSpeed = currentSpeed;
        this.haveBoost = haveBoost;

        if(this.damage == 0){
            maxSpeed = 15;
        }else if(this.damage == 1){
            maxSpeed = 9;
        }else if(this.damage == 2){
            maxSpeed = 8;
        }else if(this.damage == 3){
            maxSpeed = 6;
        }else if(this.damage == 4){
            maxSpeed = 3;
        }else if(this.damage == 5){
            maxSpeed = 0;
        }
    }

    public int getMaxSpeed(){
        if(!haveBoost && maxSpeed == 15){
            return 9;
        }
        return this.maxSpeed;
    }

    public int getCurrentSpeed(){
        return this.currentSpeed;
    }

    public int getAccelerate(){
        int ind = getInd();
        if(ind < 5){
            ind += 1;
        }
        
        return speedArr[ind];
    }

    public int getDecelerate(){
        int ind = getInd();
        if(ind > 0){
            ind --;
        }

        return speedArr[ind];
    }

    public int getMultipleDecelerate(int count){
        int ind = getInd();
        ind -= count;

        if(ind < 0){
            ind = 0;
        }
        return speedArr[ind];
    }

    public int getBoost(){
        return this.maxSpeed;
    }

    private int getInd(){        
        for(int i = 0; i < 7; i ++){
            if(speedArr[i] == this.currentSpeed){
                return i;    
            }
        }
        return -1;
    }


}
