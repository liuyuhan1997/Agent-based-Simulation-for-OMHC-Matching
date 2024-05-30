import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Member extends User implements Comparable<Member>{
    int patience;
    ArrayList<Listener> preferenceList;
    boolean holding;
    boolean quit;
    String topic;

    Member(int b, int g, int e, int t, int c, int i,int p, String topic) {
        super(b, g, e, t, c, i);
        patience = p;
        holding = false;
        quit=false;
        this.topic=topic;
        Integer index = AgentsFactory.topicMapping.get(topic);
        if (index != null) {
            this.userVector[index+7] = 1;
        }
    }



    public void setHolding(boolean holding) {
        this.holding = holding;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    @Override
    public int compareTo(Member m) {
        return this.createTimestamp - m.createTimestamp;
    }

    public boolean equals(Member m) {
        if (this.id == m.id) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * member "propose" to listener
     */
    public void memberPropose() {
        /** while there are still listeners in this member's preference list and the member is still not accepted by any listener, keep proposing **/
        while ((!(this.preferenceList.isEmpty())) && (!(this.preferenceList.get(0).listenerAccept(this)))) {
            preferenceList.remove(0);
        }
        if (this.preferenceList.isEmpty()) {
            this.holding = true;
        }
    }


}
