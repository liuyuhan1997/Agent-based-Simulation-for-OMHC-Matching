public abstract class User{
    int birthyear;
    int gender;
    int experience;
    int tenure;
    int createTimestamp;
    int waitTime;
    boolean adult;
    int id;
    double[] userVector;
    boolean chatting;
    boolean gm;

    User(int b, int g, int e, int t, int c, int i) {
        birthyear = b;
        gender = g;
        experience = e;
        tenure = t;
        createTimestamp = c;
        id=i;
        waitTime=0;
        chatting=false;
        if (birthyear > 2002) {
            adult = false;
        } else {
            adult = true;
        }
        userVector=new double[7+AgentsFactory.allTopic.size()];
        userVector[0]=(b*1.0-1924.0)/83.0; //age
        userVector[6]=t*1.0/79.0; //exp
        for(int j=1;j<=5;j++) {
            if(g==j-1){
                userVector[j]=1;
            }
            else {
                userVector[j]=0;
            }
        }
        for(int n=7;n<AgentsFactory.allTopic.size()+7;n++) {
            userVector[n]=0.0;
        }



        if(gender>1) {
            gm=true;
        } else {
            gm=false;
        }
    }

    public void setChatting(boolean chatting) {
        this.chatting = chatting;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
