import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class SortSimilarityWithoutExp implements Comparator<User> {
    User user;
    SortSimilarityWithoutExp(User u) {
        this.user=u;
    }
    @Override
    public int compare(User o1, User o2) {
        double[] v1=o1.userVector;
        double[] v2=o2.userVector;
        double[] vm=this.user.userVector;
        double sim1=CosineSimilarity(vm,v1);
        double sim2=CosineSimilarity(vm,v2);
        if(sim1==sim2) {
            return 0;
        }
        if(sim1>sim2) {
            return -1;
        }else {
            return 1;
        }
    }



    public static double CosineSimilarity(double[] v1, double[] v2) {
        double sum1=0;
        double sum2=0;
        double sum=0;
        for (int i = 0; i < 7+AgentsFactory.allTopic.size(); i++) {
            if(i==6) {
                continue;
            }
            else {
                sum1 += (v1[i] * v1[i]);
                sum2 += (v2[i] * v2[i]);
                sum += (v1[i] * v2[i]);
            }
        }
        double s1=Math.sqrt(1.0*sum1);
        double s2=Math.sqrt(1.0*sum2);
        return sum*1.0/(s1*s2);
    }


    public static void main(String[] args) {
        Listener l=AgentsFactory.CreateListeners(1);
        Member m1=AgentsFactory.CreateMembers(1);
        Member m2=AgentsFactory.CreateMembers(1);
        ArrayList<Member> templist=new ArrayList<>();
        templist.add(m1);
        templist.add(m2);
        Collections.sort(templist,new SortBySimilarity(l));
        System.out.println(templist);
    }
}
