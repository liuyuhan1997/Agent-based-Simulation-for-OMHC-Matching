import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Listener extends User implements Comparable<Listener> {
    ArrayList<Member> preferenceList;
    ArrayList<String> topicList;
    Member currentPartner;
    double decisionTime;
    boolean available;
    boolean full;

    Listener(int b, int g, int e, int t, int c, int i, ArrayList<String> topic) {
        super(b, g, e, t, c, i);
        Random random = new Random();
        this.decisionTime=Math.log(1-random.nextDouble())*(-0.8);
        this.available = true;
        this.full=false;
        this.topicList=topic;
        for (String value : AgentsFactory.allTopic) {
            Integer index = AgentsFactory.topicMapping.get(value);
            if (index != null) {
                userVector[index+7] = 1;
            }
        }
    }

    @Override
    public int compareTo(Listener o) {
        return this.createTimestamp - o.createTimestamp;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    /**
     * Method for listeners to "accept" offer
     * @param m member "proposing" to the listener
     * @return boolean for whether to "accept" the offer
     */
    public boolean listenerAccept(Member m) {
        /** listener is deciding whether to choose the recommend match and are not available to accept any offer **/
        if (!(this.available)) {
            return false;
        }
        /** if listener has not accepted any offer, accept this member's **/
        if (this.currentPartner == null) {
            m.setHolding(true);
            this.currentPartner = m;
            return true;
        } else {
            /**if the listener has already accepted an offer, compare the current one and the accept one **/
            int acceptedIndex = -1;
            int mIndex = -1;
            for (int i = 0; i < this.preferenceList.size(); i++) {
                if (this.preferenceList.get(i).equals(currentPartner)) {
                    acceptedIndex = i;
                }
                if (this.preferenceList.get(i).equals(m)) {
                    mIndex = i;
                }
            }
            /** if current member has higher preference than the accepted one, accept the offer, otherwise, reject the offer **/
            if (mIndex < acceptedIndex) {
                this.currentPartner.setHolding(false);
                this.currentPartner = m;
                m.setHolding(true);
                return true;
            } else {
                m.setHolding(false);
                return false;
            }
        }
    }

}
