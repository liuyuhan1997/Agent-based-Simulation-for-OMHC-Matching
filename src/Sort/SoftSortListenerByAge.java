import java.util.Comparator;

import static java.lang.StrictMath.abs;

public class SoftSortListenerByAge implements Comparator<Listener> {
    Member member;

    SoftSortListenerByAge(Member m) {
        this.member=m;
    }

    @Override
    public int compare(Listener o1, Listener o2) {
        int diff1=abs(o1.birthyear-member.birthyear);
        int diff2=abs(o2.birthyear-member.birthyear);
        return diff1-diff2;
    }
}
