import java.util.Scanner;

public class Insertion_Sort {
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

    // 插入排序法：由大到小排序
    public static void sort(int array[], int x) {
        // 從第 2 個元素（索引 1）開始，將當前元素視為要插入的「牌 (key)」
        for (int i = 1; i < x; i++) {
            int key = array[i];
            int j = i - 1;

            // 往左邊已排序的區間搜尋
            // 規則：因為是由大到小排序，只要前面的數字 array[j] 比 key 還小，
            // 就把前面的數字直接往後覆蓋（向右移一格）
            while (j >= 0 && array[j] < key) {
                array[j + 1] = array[j]; // 往後挪
                j--; // 繼續往左看
            }

            // 找到正確位置後，把 key 放入空出來的位置
            array[j + 1] = key;

            // 每一輪插入完成後，輸出當前整個數列
            printarr(array, x);
        }
    }
}
//Description
//
//在某個大學（輔X、X大）裡，人工智慧與資訊安X系的學弟妹（還有和我同年級的同學，以及大我一屆的朋友）都爭先恐後地想要向程式X計二助教「贊助」一筆費用。大家搶著要出更多銀子，甚至不惜破壞排隊的傳統美德，只希望能夠優先獲得助教的關愛。
//
//面對這樣混亂的場面，天才般的助教想到了一個完美的方法：
//
//只有贊助金額最高的人，才有機會被優先關注；其他人只能乖乖排隊，祈禱自己也能被挑選上。
//
//助教決定使用一種簡單且有效率的方式來整理這份名單：
//
//一開始，只有第一位學生被視為已經排入名單
//接著依序將後面的學生，一個一個插入到前面已排序好的名單中
//每次插入時，都要找到正確的位置，確保整份名單始終維持由高到低的順序
//最終，助教會得到一份完整的贊助排序結果，並依此決定誰能夠先被「聖上」挑選。
//
//現在，請你模擬這個排序過程，將學生的出價依照由大到小排列，並輸出每一輪插入完成後的結果。
//
//
//Input
//
//輸入包含兩行：
//
//第一行為一個整數n（1≤n≤1000），表示學生人數
//第二行包含n個整數，代表每位學生自願奉獻的誠意
//
//Output
//
//請模擬插入排序（由大到小），並輸出每一輪插入完成後的結果：
//
//從第 2 個元素開始（第 1 個視為已排序）
//每完成一次插入，輸出當前整個數列
//每行輸出一輪結果
//每個數字之間以一個空格分隔
//行尾不可有多餘空格