import java.io.*;
import java.util.*;

public class AgentsFactory {
    static int memberId = 0;
    static int listenerId = 0;
    static int[] memberGenderDistribution = new int[]{635, 940, 966, 984};
    static int[] memberExperienceDistribution = new int[]{131, 344, 701, 954};
    static int[][] memberBirth=LoadingBirthyears("member");
    static int[] memberSignupDistribution=LoadingSignup("member");
    static int[] listenerDistribution = new int[]{545, 929, 958, 984};
    static int[] listenerExperienceDistribution = new int[]{19, 87, 397, 849};
    static int[][] listenerBirth=LoadingBirthyears("listener");
    static int[] listenerSignupDistribution=LoadingSignup("listener");
    static int[] patienceArray=LoadingPatience();
    static int[] sessionArray=LoadSession();
    static String[] memberTopics=LoadingMemberTopic();
    static String[] listenerTopics=LoadingListenerTopic();
    static ArrayList<String> allTopic=LoadAllTopic();
    static HashMap<String, Integer> topicMapping=LoadMapping();

    public static HashMap<String, Integer> LoadMapping() {
        HashMap<String, Integer> valueToIndex = new HashMap<>();
        // Assign each unique value an index
        for (int i = 0; i < allTopic.size(); i++) {
            valueToIndex.put(allTopic.get(i), i);
        }
        return valueToIndex;
    }

    public static ArrayList<String> LoadAllTopic() {
        ArrayList<String> vocabulary = new ArrayList<>();
        vocabulary.add("parents");
        vocabulary.add("dating");
        vocabulary.add("self_improvement");
        vocabulary.add("romantic_relationship");
        vocabulary.add("family");
        vocabulary.add("lonely");
        vocabulary.add("depression");
        vocabulary.add("anxiety");
        vocabulary.add("home");
        vocabulary.add("sexuality");
        vocabulary.add("suicide");
        vocabulary.add("pandemic");
        vocabulary.add("dissociative_identity");
        vocabulary.add("health");
        vocabulary.add("stress");
        vocabulary.add("LGBTQ");
        vocabulary.add("overwhelming");
        vocabulary.add("intimacy");
        return vocabulary;
    }

    /**
     * Loading user counts in each hours (assume in each hour, number of agents does not change)
     * @param type listener or member
     * @return int array, length= 168 (there is 168 hours in a week)
     */
    public static int[] LoadingUserCounts(String type) {
        int[] userCounts = new int[168];
        int readIndex = 0;
        if (type.equals("listener")) {
            readIndex = 2;
        } else {
            readIndex = 1;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/timestamped_counts.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            userCounts[Integer.parseInt(values[0])] = Integer.parseInt(values[readIndex]);
        }
        return userCounts;
    }

    /**
     * Method for creating members
     * @param timestamp the time of creating this member
     * @return the new member just created
     */
    public static Member CreateMembers(int timestamp) {
        Random rand = new Random();
        int genderRand = rand.nextInt(1000);
        int memberGender = GenderRand(memberGenderDistribution, genderRand);
        int birthRand=rand.nextInt(29484);
        int memberBirthyear = BirthRand(memberBirth, birthRand);
        int signupRand= rand.nextInt(29484);
        int memberTenure = SignupRand(memberSignupDistribution, signupRand);
        int experiecneRand = rand.nextInt(1000);
        int memberExperience = ExperienceRand(memberExperienceDistribution, experiecneRand);
        int memberCreation = timestamp;
        int patienceIndex=rand.nextInt(95817);
        int memberPatience=patienceArray[patienceIndex];
        int memberTopicIndex=rand.nextInt(595701);
        String memberTopic=memberTopics[memberTopicIndex];
        Member newMember = new Member(memberBirthyear, memberGender, memberExperience, memberTenure, memberCreation, memberId, memberPatience,memberTopic);
        writeMemberInfo(newMember);
        memberId++;
        return newMember;
    }

    /**
     * Method for creating listeners
     * @param timestamp the time of creating this listener
     * @return the new listener just created
     */
    public static Listener CreateListeners(int timestamp) {
        Random rand = new Random();
        int genderRand = rand.nextInt(1000);
        int listenerGender = GenderRand(listenerDistribution, genderRand);
        int birthRand=rand.nextInt(29484);
        int listenerBirthyear = BirthRand(listenerBirth, birthRand);
        int signupRand=rand.nextInt(29484);
        int listenerTenure = SignupRand(listenerSignupDistribution, signupRand);
        int experiecneRand = rand.nextInt(1000);
        int listenerExperience = ExperienceRand(listenerExperienceDistribution, experiecneRand);
        int listenerCreation = timestamp;
        int listenerTopicIndex=rand.nextInt(119370);
        ArrayList<String> listenerTopic=GettingListenerTopicList(listenerTopicIndex);
        Listener newlistener = new Listener(listenerBirthyear, listenerGender, listenerExperience, listenerTenure, listenerCreation, listenerId, listenerTopic);
        writeListenerInfo(newlistener);
        listenerId++;
        return newlistener;
    }

    /**
     * Get agent's gender based on random number
     * @param distribution
     * @param rand
     * @return the gender 0-5
     */
    public static int GenderRand(int[] distribution, int rand) {
        if (rand < distribution[0]) {
            return 0;
        } else if (rand < distribution[1]) {
            return 1;
        } else if (rand < distribution[2]) {
            return 2;
        } else if (rand < distribution[3]) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * Get agent's experience level based on random number
     * @param distribution
     * @param rand
     * @return the experience level 0-5
     */
    public static int ExperienceRand(int[] distribution, int rand) {
        if (rand < distribution[0]) {
            return 0;
        } else if (rand < distribution[1]) {
            return 1;
        } else if (rand < distribution[2]) {
            return 2;
        } else if (rand < distribution[3]) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * Loading the file of birth year distribution
     * @param type listener or member
     * @return array with the distribution
     */
    public static int[][] LoadingBirthyears(String type) {
        int readIndex = -1;
        int[][] birthyear;
        int threshold;
        if (type.equals("listener")) {
            readIndex = 1;
            birthyear=new int[61][2];
            threshold=61;
        } else {
            readIndex = 0;
            birthyear=new int[65][2];
            threshold=65;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/birthyear.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        int i=0;
        while (i<threshold) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            birthyear[i++] = new int[]{Integer.parseInt(values[readIndex]), Integer.parseInt(values[readIndex+2])};
        }
        return birthyear;
    }

    /**
     * Get agent's birth year based on random number
     * @param distribution
     * @param rand
     * @return
     */
    public static int BirthRand(int[][] distribution, int rand) {
        for (int i = 0; i < distribution.length; i++) {
            if(rand<=distribution[i][1]) {
                return distribution[i][0];
            }
        }
        return 1920;
    }

    /**
     * Loading the file of the signup date distribution
     * @param type listener or member
     * @return array with distribution
     */
    public static int[] LoadingSignup(String type) {
        int readIndex = 0;
        int[] signupCounts=new int[80];
        if (type.equals("listener")) {
            readIndex = 2;
        } else {
            readIndex = 1;
        }
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/signup.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            signupCounts[Integer.parseInt(values[0])] = Integer.parseInt(values[readIndex]);
        }
        return signupCounts;
    }

    /**
     * Get agent's sign up month (0 means January 2014, 1 means February 2014))
     * @param distribution
     * @param rand
     * @return sign up month 0-79
     */
    public static int SignupRand(int[] distribution, int rand) {
        for (int i = 0; i < distribution.length; i++) {
            if(rand<=distribution[i]) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Loading agent's patience time, one row is a real data, random number decides which data to fit
     * @return the array of patience level
     */
    public static int[] LoadingPatience() {
        int[] patience=new int[95817];
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/waitTime.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        int i=0;
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            patience[i++] = Integer.parseInt(values[0]);
        }
        return patience;
    }

    /**
     * Loading listener agent's topic, one row represents a real data, random number decides which data to fit
     * @return the array of topic
     */
    public static String[] LoadingListenerTopic() {
        String[] listenerTopic=new String[119370];
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/listener_topic.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        int i=0;
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            listenerTopic[i++] = values[0];
        }
        return listenerTopic;
    }
    public static ArrayList<String> GettingListenerTopicList(int index) {
        String topicString=listenerTopics[index];
        return new ArrayList<String>(Arrays.asList(topicString.split(";")));
    }
    /**
     * Loading member agent's topic, one row represents a real data, random number decides which data to fit
     * @return the array of topic
     */
    public static String[] LoadingMemberTopic() {
        String[] memberTopic=new String[595702];
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/member_topic.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        int i=0;
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            memberTopic[i++] = values[0];
        }
        return memberTopic;
    }

    /**
     * Loading chat length, one row is a real data, random number decides which data to fit
     * @return the array of session length
     */
    public static int[] LoadSession() {
        int[] session=new int[980646];
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("data/sessionLength.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(",");
        int i=0;
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            String[] values = row.split(",");
            session[i++] = Integer.parseInt(values[0]);
        }
        return session;
    }

    /**
     * Write agent's info into file
     * @param listener
     */
    public static void writeListenerInfo(Listener listener) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("similarity-ag/Listeners.csv",true));
            out.write(listener.createTimestamp+","+listener.id+","+listener.birthyear+","+listener.gender+","+listener.tenure+","+listener.experience+","+listener.topicList.toString()+"\n");
            out.close();
        } catch (IOException e) {
        }

    }

    /**
     * Write agent's info into file
     * @param member
     */
    public static void writeMemberInfo(Member member) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("similarity-ag/Members.csv",true));
            out.write(member.createTimestamp+","+member.id+","+member.birthyear+","+member.gender+","+member.tenure+","+member.experience+","+member.patience+","+member.topic+"\n");
            out.close();
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        String[] lt=LoadingListenerTopic();
        System.out.println(GettingListenerTopicList(3).toString());
    }

}


