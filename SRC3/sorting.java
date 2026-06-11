import java.util.Scanner;

public class sorting {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        if (!scan.hasNextInt()) return;
        int n = scan.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = scan.nextInt();
        }

        sort(arr, n);
        scan.close();
    }

    // 負責格式化輸出陣列的方法（完全保留你寫的漂亮格式）
    public static void printarr(int array[], int x) {
        for (int i = 0; i < x; i++) {
            System.out.print(array[i]);
            if (i < x - 1)
                System.out.print(" ");
        }
        System.out.println();
    }

    // 選擇排序法：從大到小排序
    public static void sort(int array[], int x) {
        int maxnum = 0;

        // 題目要求共輸出 n-1 行，所以外層迴圈跑 x-1 次
        for (int i = 0; i < x - 1; i++) {
            maxnum = i;

            // 從尚未排序的區間 [i+1, x-1] 中找出最大值的索引
            for (int j = i + 1; j < x; j++) {
                if (array[maxnum] < array[j]) {
                    maxnum = j;
                }
            }

            // 修正核心：不論 maxnum 是否等於 i，這回合都算結束
            // 有需要交換就進行交換
            if (maxnum != i) {
                int temp = array[maxnum];
                array[maxnum] = array[i];
                array[i] = temp;
            }

            // 關鍵移處：必須放在 if 區塊外面！
            // 確保「每一回合結束後」即使元素本來就在對的位置、沒發生交換，也能確實印出當前序列
            printarr(array, x);
        }
    }
}
//Description
//
//在某個奇怪的學校裡，學生們為了爭取助教的關注，決定用「贊助金額」來排名。
//
//助教採用一種簡單但直觀的方法來排序
//
//排序規則如下：
//
//每一回合，從尚未排序的區間中，找出「大凱子」
//將該最大值與當前位置交換
//每一回合結束後，輸出目前的序列狀態
//最終得到由大到小的排序結果
//
//Input
//
//n
//a1 a2 a3 ... an
//n：整數個數（1 ≤ n ≤ 1000）
//ai：每個整數
//
//Output
//
//每一回合輸出一行排序過程（共 n-1 行）