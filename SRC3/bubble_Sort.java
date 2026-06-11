import java.util.Scanner;

public class bubble_Sort {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // 讀取陣列的長度 n
        int n = scan.nextInt();
        int[] arr = new int[n];

        // 依序讀入 n 個整數並存入陣列中
        for (int i = 0; i < n; i++) {
            arr[i] = scan.nextInt();
        }

        // 呼叫氣泡排序函數
        sort(arr, n);
    }

    // 格式化輸出陣列的方法：數字之間用空格隔開，行尾換行不留多餘空格
    public static void printarr(int array[], int x) {
        for (int i = 0; i < x; i++) {
            System.out.print(array[i]);
            if (i < x - 1)
                System.out.print(" ");
        }
        System.out.println();
    }

    // 氣泡排序法（Bubble Sort）：由大到小排序
    public static void sort(int array[], int x) {
        // 外層迴圈：控制排序的總輪數。共跑 x-1 輪
        for (int i = x - 1; i > 0; i--) {

            // 內層迴圈：負責相鄰元素的比較與交換
            // 從頭走到尾，把較小的數一路往後「擠」
            for (int j = 0; j < x - 1; j++) {

                // 排序規則：由大到小
                // 如果前面的數 array[j] 比後面的數 array[j+1] 還小，就進行交換
                if (array[j] < array[j + 1]) {
                    int temp;
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }

            // 每一輪內層迴圈比對、交換結束後，輸出當前整個陣列的狀態
            printarr(array, x);
        }
    }
}
//鉛球排序法
//Description
//
//在某個奇怪的體育學院裡，學生們不用紙筆成績排名，而是用「丟鉛球」來排名。
//
//場上站成一排的學生，每個人前面都有一個數字，代表他丟鉛球的距離。教練想把這些學生依照成績高低排序，距離越遠的人要排越前面。
//
//他採用了一種特別的排序方式：
//
//每次從左到右檢查相鄰的兩個人，如果左邊那位丟出的距離小於右邊那位，代表右邊的人表現更好，兩人就要立刻交換位置。
//
//經過一輪又一輪的比較與交換之後，整排學生就會依照鉛球距離由大到小排好。
//
//這種排序方式，被大家稱為「鉛球排序法」。
//
//現在請你模擬這個排序過程，輸出排序每一輪完成後的結果。
//
//
//Input
//
//第一行輸入一個整數n，代表共有幾位學生。
//
//第二行輸入n個整數a1, a2, ..., an，代表每位學生丟鉛球的距離。
//
//
//Output
//
//輸出n-1行
//
//每一行代表一輪結束後的序列
//
//
//Sample Input 1
//
//5
//3 8 2 6 4
//Sample Output 1
//
//8 3 6 4 2
//8 6 4 3 2
//8 6 4 3 2
//8 6 4 3 2