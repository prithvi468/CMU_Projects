import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Mock {
    public static void main(String args[]) {
        Solution1 solution = new Solution1();

        int[] nums = new int[]{-18, 12, 3, 0};
        int target = -6;
        int xc[] = solution.twoSum(nums, target);
        System.out.println(xc[0]);
        System.out.println(xc[1]);
    }


}

class Solution1 {
    public int[] twoSum(int[] nums, int target) {
        int arr[] = new int[2];
        Map<Integer, Integer> optimal = new HashMap<>();
        optimal.put(-89,-98);
        if(nums.length!=2) {
            for (int i = 0; i < nums.length; i++) {
                int complement = target - nums[i];
                if (optimal.containsKey(nums[i])) {
                    arr[0] = complement;
                    arr[1] = nums[i];
                } else {
                    optimal.put(complement, nums[i]);
                }
            }
            for (int i = 0; i < nums.length; i++) {
                if (arr[0] == nums[i]) {
                    arr[0] = i;
                }
                if (arr[1] == nums[i] && arr[0]!=i) {
                    arr[1] = i;
                    break;
                }

            }
        }
        else
        {
            arr[0] =0;
            arr[1]=1;
        }
        return arr;
    }
}