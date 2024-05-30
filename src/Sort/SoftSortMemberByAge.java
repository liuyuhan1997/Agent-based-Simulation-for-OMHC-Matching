import java.util.Comparator;

import static java.lang.StrictMath.abs;

public class SoftSortMemberByAge implements Comparator<Member> {
    Listener listener;

    SoftSortMemberByAge(Listener l) {
        this.listener=l;
    }

    @Override
    public int compare(Member o1, Member o2) {
        int diff1=abs(o1.birthyear-listener.birthyear);
        int diff2=abs(o2.birthyear-listener.birthyear);
        if(diff1==diff2) {
            return o2.waitTime-o1.waitTime;
        } else {
            return diff1-diff2;
        }
    }
}
