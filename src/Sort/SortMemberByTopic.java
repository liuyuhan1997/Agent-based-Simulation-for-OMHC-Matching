import java.util.Comparator;

public class SortMemberByTopic implements Comparator<Member> {
    Listener listener;

    public SortMemberByTopic(Listener l) {
        this.listener = l;
    }

    @Override
    public int compare(Member o1, Member o2) {
        boolean o1bool = this.listener.topicList.contains(o1.topic);
        boolean o2bool = this.listener.topicList.contains(o2.topic);
        if (o1bool == o2bool) {
            return 0;
        } else if (o1bool == true) {
            return -1;
        } else {
            return 1;
        }
    }
}