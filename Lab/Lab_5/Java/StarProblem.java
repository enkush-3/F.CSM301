import java.util.*;

public class StarProblem{

    public static int searchMax(List<Number> array, int start, int end){
        if(start > end){
            return -1;
        }

        int mid = (end + start) / 2;

        int left = searchMax(array, start, mid);
        int right = searchMax(array, mid + 1, end);

        return array.get(left).getValue() < array.get(right).getValue() ? right : left;
    }

    public static List<Number>  SolutionFunc(List<Number> array, int start, int end, int deep){
        if(start >= end){
            return array;
        }

        int maxIndex = searchMax(array, start, end);

        array.get(maxIndex).setDeep(deep);

        SolutionFunc(array, start, maxIndex - 1, deep + 1);
        SolutionFunc(array, maxIndex  + 1, end, deep + 1);

        return array;
    }


    public static void main(String args []) {
        Scanner scan = new Scanner(System.in);

        int n = scan.nextInt();
        while(n > 0){
            int a = scan.nextInt();

            List<Map<Integer, Integer>> array = new ArrayList<>();

            for(int i=0; i< a; i++)
            {
                Map<Integer, Integer> deepValue = new HashMap<>();
                int b = scan.nextInt();
                array.add(b,0);
            }

            List<Number> result = new ArrayList<>();

            result = SolutionFunc(array, 0, array.size() - 1, 0);

            for(Number i : result){
                System.out.print(i.getDeep() + " ");
            }
            System.out.println();
        }
    }
}