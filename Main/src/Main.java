import java.util.*;

public class Main {
    public static void main(String Args[]) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int ml = 0;

        for (int i = 0; i < s.length(); i++) {
            String cur = "";

            for (int j = i; j < s.length(); j++) {
                char c = s.charAt(j);
                if (cur.indexOf(c) == -1) {
                    cur += c;
                    ml = Math.max(ml, cur.length());
                } else {
                    break;
                }
            }
        }

        System.out.println(ml);
    }
}
