import java.util.Random;

public class Pair {
    int rating;
    int blocking;
    Listener listener;
    Member member;
    int length;
    int createIndex;
    boolean end;
    double cosineSimilarity;

    Pair(Listener l, Member m,int index) {
        listener=l;
        member=m;
        createIndex=index;
        end=false;
        rating= 0;
        blocking= -1;
        Random random=new Random();
        length= AgentsFactory.sessionArray[random.nextInt(980646)];
        cosineSimilarity=SortBySimilarity.CosineSimilarity(m.userVector,l.userVector);

    }

    public void setEnd(boolean end) {
        this.end = end;
    }

}
