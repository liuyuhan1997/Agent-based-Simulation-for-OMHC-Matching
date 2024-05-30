import java.util.ArrayList;

public class Machi {
    static int[] listenerCount = new int[168];
    static int[] memberCount = new int[168];
    static int simulationDuration;
    static ArrayList<Member> memberPool;
    static ArrayList<Listener> listenerPool;
    static ArrayList<Pair> matchedPairs;
    static int totalMatches;
    static int totalFailedMatches;
    static int i = 0;

    public static void main(String[] args) {
        /** Setting the system **/
        Machi.simulationDuration = 168;
        Machi.memberCount = AgentsFactory.LoadingUserCounts("member");
        Machi.listenerCount = AgentsFactory.LoadingUserCounts("listener");
        Machi.memberPool = new ArrayList<Member>();
        Machi.listenerPool = new ArrayList<Listener>();
        Machi.matchedPairs = new ArrayList<Pair>();
        /** Starting Simulation **/
        for (int i = 0; i < Machi.simulationDuration * 60; i++) {
            int index = i / 60;
            int tempMemberCount = Machi.memberCount[index];
            int tempListenerCount = Machi.listenerCount[index];
            SimulationPeriod currentMinute = new SimulationPeriod(i, tempMemberCount, tempListenerCount);
            currentMinute.Match();
            currentMinute.Record();
            System.out.println(i);
        }
        System.out.println("total matches: " + totalMatches);
        System.out.println("total failed matches: " + totalFailedMatches);
        System.out.println(Machi.i);
    }

}