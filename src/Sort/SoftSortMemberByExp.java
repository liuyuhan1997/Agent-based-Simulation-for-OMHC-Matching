import java.util.Comparator;

import static java.lang.StrictMath.abs;

public class SoftSortMemberByExp implements Comparator<Member> {
    Listener listener;

    SoftSortMemberByExp(Listener l) {
        this.listener=l;
    }

    @Override
    public int compare(Member o1, Member o2) {
        int diff1=abs(o1.experience-listener.experience);
        int diff2=abs(o2.experience-listener.experience);
        if(diff1==diff2){
            return o2.waitTime-o1.waitTime;
        } else {
            return diff1 - diff2;
        }
    }

}
