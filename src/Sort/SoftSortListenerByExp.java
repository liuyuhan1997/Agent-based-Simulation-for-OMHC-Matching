import java.util.Comparator;

import static java.lang.StrictMath.abs;

public class SoftSortListenerByExp implements Comparator<Listener> {
    Member member;
    SoftSortListenerByExp(Member m){this.member=m;}

    @Override
    public int compare(Listener o1, Listener o2) {
        int diff1=abs(o1.tenure-member.tenure);
        int diff2=abs(o2.tenure-member.tenure);
        return diff1-diff2;
    }
}
