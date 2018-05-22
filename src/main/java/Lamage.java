import java.util.*;

public class Lamage {
    public static void main(String[] args) {

            String strWorld = "wer|dffd|ddsa|dfd|dreg|de|dr|ce|ghrt|cf|gt|ser|tg|ghrt|cf|gt|";
            String[] words = strWorld.split("\\|");
            Map<String, Integer> countMap = new HashMap<String, Integer>();
            for (String word : words) {
                Integer count = countMap.get(word);
                if (count == null) {
                    countMap.put(word, 1);
                } else {
                    countMap.put(word, count + 1);
                }
            }
            System.out.println("countMap：");
            for (String key : countMap.keySet()) {
                System.out.println(key + " count：" + countMap.get(key));

        }
    }
}
