import java.util.Comparator;

public class SortMemberByWaiting implements Comparator<Member> {
    Listener listener;

    SortMemberByWaiting(Listener l) {
        this.listener=l;
    }

    @Override
    public int compare(Member o1, Member o2) {
        if(o1.waitTime>o2.waitTime) {
            return -1;
        } else if (o1.waitTime==o2.waitTime) {
            return 0;
        } else {
            return 1;
        }
    }
}
