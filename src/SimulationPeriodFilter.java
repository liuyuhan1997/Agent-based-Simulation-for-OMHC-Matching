import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimulationPeriodFilter {
    int index;
    ArrayList<Member> activeMemberPool;
    ArrayList<Member> activeMinorityMemberPool;
    ArrayList<Member> activeTeenMemberPool;
    ArrayList<Listener> activeListenerPool;
    ArrayList<Listener> activeMinorityListenerPool;
    ArrayList<Listener> activeTeenListenerPool;
    int listenerCount;
    int memberCount;
    ArrayList<Pair> currentMatchedPairs;

    SimulationPeriodFilter(int i, int currentMemberCount, int currentListenerCount) {
        /**creating new members and listeners to make pool consistent with user distribution **/
        this.index = i;
        listenerCount = currentListenerCount;
        memberCount = currentMemberCount;
        activeMemberPool = new ArrayList<Member>();
        activeTeenMemberPool=new ArrayList<Member>();
        activeMinorityMemberPool=new ArrayList<Member>();
        activeListenerPool = new ArrayList<Listener>();
        activeTeenListenerPool=new ArrayList<Listener>();
        activeMinorityListenerPool=new ArrayList<Listener>();
        /**for member-listener pairs, go offline if the chat ends**/
        for (Pair p : Machi.matchedPairs) {
            if (p.end) {
                continue;
            }
            if (p.length == (this.index - p.createIndex)) {
                Machi.memberPool.remove(p.member);
                Machi.listenerPool.remove(p.listener);
                p.setEnd(true);
            }
        }
        /**create new agents **/
        int listenerDiff = listenerCount - Machi.listenerPool.size();
        Random random = new Random();
        for (int j = 0; j < Math.abs(listenerDiff); j++) {
            if (listenerDiff > 0) {
                Machi.listenerPool.add(AgentsFactory.CreateListeners(this.index));
            }
        }
        int memberDiff = memberCount - Machi.memberPool.size();
        for (int j = 0; j < Math.abs(memberDiff); j++) {
            if (memberDiff > 0) {
                Machi.memberPool.add(AgentsFactory.CreateMembers(this.index));
            }
        }
        for (Listener l : Machi.listenerPool) {
            if (!(l.chatting)) {
                if(l.gm) {
                    activeMinorityListenerPool.add(l);
                    continue;
                }
                if (!(l.adult)) {
                    activeTeenListenerPool.add(l);
                    continue;
                }
                activeListenerPool.add(l);
            }
        }
        for (Member m : Machi.memberPool) {
            if (!(m.chatting)) {
                m.setWaitTime(this.index - m.createTimestamp);
                m.setHolding(false);
                if(m.gm) {
                    activeMinorityMemberPool.add(m);
                    continue;
                }
                if(!(m.adult)) {
                    activeTeenMemberPool.add(m);
                }
                activeMemberPool.add(m);
            }
        }
        currentMatchedPairs = new ArrayList<Pair>();
    }

    /**
     * Match agents using acceptance-deferred algorithm
     */
    public void Match() {
        for (Listener l : activeListenerPool) {
            Collections.shuffle(activeMemberPool);
            for (Member availableMember : activeMemberPool) {
                if (!(availableMember.chatting)) {
                    CreateNewPair(l, availableMember);
                    break;
                }
            }
        }
        for (Listener l : activeTeenListenerPool) {
            Collections.shuffle(activeTeenMemberPool);
            for (Member availableMember : activeTeenMemberPool) {
                if (!(availableMember.chatting)) {
                    CreateNewPair(l, availableMember);
                    break;
                }
            }
        }
        for (Listener l : activeMinorityListenerPool) {
            Collections.shuffle(activeMinorityMemberPool);
            for (Member availableMember : activeMinorityMemberPool) {
                if (!(availableMember.chatting)) {
                    CreateNewPair(l, availableMember);
                    break;
                }
            }
        }
    }

    /**
     * Record matches and quit members
     */
    public void Record() {
        /** for members waited for too long, go offline **/
        for (Member m : activeMemberPool) {
            if ((m.waitTime > m.patience) && !(m.chatting)) {
                Machi.memberPool.remove(m);
                m.setQuit(true);
                writeQuitMembers(m);
                Machi.totalFailedMatches++;
            }
        }
        for (Member m : activeTeenMemberPool) {
            if ((m.waitTime > m.patience) && !(m.chatting)) {
                Machi.memberPool.remove(m);
                m.setQuit(true);
                writeQuitMembers(m);
                Machi.totalFailedMatches++;
            }
        }
        for (Member m : activeMinorityMemberPool) {
            if ((m.waitTime > m.patience) && !(m.chatting)) {
                Machi.memberPool.remove(m);
                m.setQuit(true);
                writeQuitMembers(m);
                Machi.totalFailedMatches++;
            }
        }
        for (Pair p : currentMatchedPairs) {
            writeMatchedPairs(p);
            Machi.totalMatches++;
        }
        writeCurrentMin();
    }

    /**
     * Creating new member-listener pairs
     * @param l listener
     * @param m member
     */
    public void CreateNewPair(Listener l, Member m) {
        Pair mlPair = new Pair(l, m, this.index);
        l.setChatting(true);
        l.setAvailable(false);
        m.setChatting(true);
        currentMatchedPairs.add(mlPair);
        Machi.matchedPairs.add(mlPair);
    }

    /**
     * Write matched pairs into file
     * @param p pair
     */
    public void writeMatchedPairs(Pair p) {
        int listener_expertise_match=-1;
        if(p.listener.topicList.contains(p.member.topic)) {
            listener_expertise_match = 1;
        } else {
            listener_expertise_match=0;
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("filter/Matched.csv", true));
            out.write(this.index + "," + p.member.id + "," + p.member.birthyear + "," + p.member.gender + "," + p.member.tenure + "," + p.member.experience + "," + p.listener.id + "," + p.listener.birthyear + "," + p.listener.gender + "," + p.listener.tenure + "," + p.listener.experience + "," + p.length + "," + p.rating + "," + p.blocking + "," + (p.member.waitTime) + "," + p.listener.decisionTime +"," + listener_expertise_match + "," + p.cosineSimilarity+"\n");
            out.close();
        } catch (IOException e) {
        }
    }

    /**
     * Write quit members into file
     * @param member
     */
    public void writeQuitMembers(Member member) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("filter/UnmatchedMembers.csv", true));
            out.write(this.index + "," + member.id + "," + member.birthyear + "," + member.gender + "," + member.tenure + "," + member.experience + "," + member.patience + "," + member.waitTime + "," + member.topic +"\n");
            out.close();
        } catch (IOException e) {
        }

    }

    /**
     * Write agents online in this minute
     */
    public void writeCurrentMin() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("filter/Mins.csv", true));
            out.write(this.index + "," + this.memberCount + "," + this.listenerCount + "," + Machi.memberPool.size() + "," + Machi.listenerPool.size() + "," + activeMemberPool.size() + "," + activeListenerPool.size() + "\n");
            out.close();
        } catch (IOException e) {
        }
    }
}
