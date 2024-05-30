import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SimulationPeriodBaseline {
    int index;
    ArrayList<Member> activeMemberPool;
    ArrayList<Listener> activeListenerPool;
    int listenerCount;
    int memberCount;
    ArrayList<Pair> currentMatchedPairs;

    SimulationPeriodBaseline(int i, int currentMemberCount, int currentListenerCount) {
        /**creating new members and listeners to make pool consistent with user distribution **/
        this.index = i;
        listenerCount = currentListenerCount;
        memberCount = currentMemberCount;
        activeMemberPool = new ArrayList<Member>();
        activeListenerPool = new ArrayList<Listener>();
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
                activeListenerPool.add(l);
            }
        }
        for (Member m : Machi.memberPool) {
            if (!(m.chatting)) {
                m.setWaitTime(this.index - m.createTimestamp);
                activeMemberPool.add(m);
                m.setHolding(false);
            }
        }
        currentMatchedPairs = new ArrayList<Pair>();
    }

    public void Baseline() {
        for (Member m : activeMemberPool) {
            Collections.shuffle(activeListenerPool);
            m.preferenceList = (ArrayList<Listener>) activeListenerPool.clone();
        }
        for (Listener l : activeListenerPool) {
            Collections.shuffle(activeMemberPool);
            l.preferenceList = (ArrayList<Member>) activeMemberPool.clone();
        }
    }

    public void Match() {
        /** create preference list**/
        Baseline();
        /** get recommended match **/
        while (checkHoldingMembers()) {
            for (Member m : activeMemberPool) {
                if (!(m.holding)) {
                    m.memberPropose();
                }
            }
        }
        Random random = new Random();
        /** listeners accept/reject recommended member**/
        for (Listener l : activeListenerPool) {
            int matchIndex = (int) (Math.round(l.decisionTime) + l.createTimestamp);
            if (matchIndex != this.index) {
                l.setAvailable(false);
                continue;
            }
//            if(l.currentPartner==null) {
//                System.out.println("null partner");
//                continue;
//            }
//            int acceptRand = random.nextInt(100);
//            if (acceptRand < 90 && !(l.currentPartner.chatting) && !(l.currentPartner.quit)) {
//                CreateNewPair(l, l.currentPartner);
//            } else {
//                Member pairedMember = ChooseOneMember();
//                if(pairedMember!=null) {
//                    CreateNewPair(l, pairedMember);
//                }
//            }
                Collections.shuffle(activeMemberPool);
                for (Member availableMember : activeMemberPool) {
                    if (!(availableMember.chatting)) {
                        CreateNewPair(l, availableMember);
                        break;
                    }
                }
        }
    }

    public Member ChooseOneMember() {
        Collections.shuffle(activeMemberPool);
        for (Member availableMember : activeMemberPool) {
            if (!(availableMember.chatting)) {
                return availableMember;
            }
        }
        return null;
    }

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
        for (Pair p : currentMatchedPairs) {
            writeMatchedPairs(p);
            Machi.totalMatches++;
        }
        writeCurrentMin();
    }

    public boolean checkHoldingMembers() {
        for (Member m : activeMemberPool) {
            if (m.holding == false) {
                return true;
            }
        }
        return false;
    }

    public void CreateNewPair(Listener l, Member m) {
        Pair mlPair = new Pair(l, m, this.index);
        l.setChatting(true);
        l.setAvailable(false);
        m.setChatting(true);
        currentMatchedPairs.add(mlPair);
        Machi.matchedPairs.add(mlPair);
    }


    public void writeMatchedPairs(Pair p) {
        int listener_expertise_match=-1;
        if(p.listener.topicList.contains(p.member.topic)) {
            listener_expertise_match = 1;
        } else {
            listener_expertise_match=0;
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("baseline/Matched.csv", true));
            out.write(this.index + "," + p.member.id + "," + p.member.birthyear + "," + p.member.gender + "," + p.member.tenure + "," + p.member.experience + "," + p.listener.id + "," + p.listener.birthyear + "," + p.listener.gender + "," + p.listener.tenure + "," + p.listener.experience + "," + p.length + "," + p.rating + "," + p.blocking + "," + (p.member.waitTime) + "," + p.listener.decisionTime +"," + listener_expertise_match + "," + p.cosineSimilarity+"\n");

            out.close();
        } catch (IOException e) {
        }
    }

    public void writeQuitMembers(Member member) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("baseline/UnmatchedMembers.csv", true));
            out.write(this.index + "," + member.id + "," + member.birthyear + "," + member.gender + "," + member.tenure + "," + member.experience + "," + member.patience + "," + member.waitTime + "," + member.topic +"\n");
            out.close();
        } catch (IOException e) {
        }

    }

    public void writeCurrentMin() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("baseline/Mins.csv", true));
            out.write(this.index + "," + this.memberCount + "," + this.listenerCount + "," + Machi.memberPool.size() + "," + Machi.listenerPool.size() + "," + activeMemberPool.size() + "," + activeListenerPool.size() + "\n");
            out.close();
        } catch (IOException e) {
        }
    }
}
