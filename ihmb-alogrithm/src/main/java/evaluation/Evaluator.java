package evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;


public class Evaluator {
    public static String databasefilename = "Evaluation.txt";
    String path;
    HashSet<Pair<String>> relevantImages = new HashSet<Pair<String>>();
    HashMap<String, Integer> numberOfRelevants = new HashMap<String, Integer>();

    public Evaluator(String path) {
        this.databasefilename = path + "\\Evaluation.txt";
        this.path = path;
        getRelevantImagesSet();
//		
//		HashSet<Integer> vv = new HashSet<>();
//		vv.add(new Integer(2));
//		if ( vv.contains(new Integer(2))) {
//			System.out.println("That's true");
//		} else {
//			System.out.println("That's false");
//		}
//		
//		Pair<String> a = new Pair<String>("abc","def");
//		Pair<String> b = new Pair<String>("abc","def");
//		System.out.println(a.hashCode() + " " + b.hashCode());
//		System.out.println(a.equals(b));
//		HashSet<Pair<String>> hs = new HashSet<Pair<String>>();
//		hs.add(a);
//		if ( hs.contains(b)) {
//			System.out.println("That's true");
//		} else {
//			System.out.println("That's false");
//		}
    }

    private void getRelevantImagesSet() {
        try {
            FileReader fr = new FileReader(this.databasefilename);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ";");
                String target = st.nextToken();
                int count = 0;
                String query = "";
                while (st.hasMoreTokens()) {
//					System.out.println(target + " " + query);
                    query = st.nextToken();
                    relevantImages.add(new Pair<String>(target, query));
                    count++;
                }
                System.out.println(target + " " + count);
                numberOfRelevants.put(target, count);
            }
        } catch (Exception e) {
            System.out.println("There is exception here");
        }
    }

    public boolean evaluate(String target, String query) {
        if (relevantImages.contains(new Pair<String>(target, query))) {
            return true;
        }
        return false;
    }

    public void calculatePrecisionAndRecall(String target, String[] query, Pair<Float> results) {
        int count = 0;
        for (int i = 0; i < query.length; i++) {
            if (evaluate(target, query[i])) {
                count++;
            }
        }
        //result.first is precision
        results.first = (float) count / (float) query.length;
        results.second = (float) count / (float) numberOfRelevants.get(target);
    }
}
