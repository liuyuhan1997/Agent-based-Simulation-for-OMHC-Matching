import java.util.Comparator;

public class SoftSortMemberByGender implements Comparator<Member> {
    Listener listener;

    SoftSortMemberByGender(Listener l) {
        this.listener=l;
    }

    @Override
    public int compare(Member o1, Member o2) {
        if(o1.gender==this.listener.gender) {
            if(o2.gender==this.listener.gender) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2.gender==this.listener.gender) {
            return 1;
        } else {
            return o2.waitTime-o1.waitTime;
        }
    }
}
