import java.util.Comparator;

public class SoftSortListenerByGender implements Comparator<Listener> {
    Member member;

    SoftSortListenerByGender(Member m) {
        this.member=m;
    }

    @Override
    public int compare(Listener o1, Listener o2) {
        if(o1.gender==this.member.gender) {
            if(o2.gender==this.member.gender) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2.gender==this.member.gender) {
            return 1;
        } else {
            return 0;
        }
    }
}
