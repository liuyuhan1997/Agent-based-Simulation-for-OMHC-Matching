import java.util.Comparator;

public class SortListenerByTopic implements Comparator<Listener> {
    Member member;

    public SortListenerByTopic(Member m) {
        this.member = m;
    }

    @Override
    public int compare(Listener o1, Listener o2) {
        boolean o1bool = o1.topicList.contains(this.member.topic);
        boolean o2bool = o2.topicList.contains(this.member.topic);
        if (o1bool == o2bool) {
            return 0;
        } else if (o1bool == true) {
            return -1;
        } else {
            return 1;
        }
    }
}
