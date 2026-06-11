import java.util.Scanner;

public class Q_Sort {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // 安全檢查：若輸入的第一個不是整數，則直接結束程式
        if (!scan.hasNextInt()) return;

        // 讀取數列總長度 n
        int n = scan.nextInt();
        int[] arr = new int[n];

        // 依序讀入 n 個學生的成績/秒數
        for (int i = 0; i < n; i++) {
            arr[i] = scan.nextInt();
        }

        // 呼叫快速排序（Quick Sort），初始區間為 0 到 n-1
        sort(arr, 0, n - 1, n);

        scan.close();
    }

    // 格式化輸出陣列：數字之間用空格隔開，行尾不留多餘空格
    public static void printarr(int array[], int x) {
        for (int i = 0; i < x; i++) {
            System.out.print(array[i]);
            if (i < x - 1) System.out.print(" ");
        }
        System.out.println();
    }

    // 3秒快排主函數（由大到小排序）
    public static void sort(int array[], int left, int right, int x) {
        // 當左邊界小於右邊界時，代表目前區間內至少有兩個元素，需要繼續切分
        if (left < right) {
            // 選擇當前區間的最左邊元素作為基準點 (Pivot)
            int pivot = array[left];
            // 左指針 i 從基準點的下一個位置開始往右走
            int i = left + 1;
            // 右指針 j 從目前區間的最右端開始往左走
            int j = right;
            int temp;

            // 當左右指針還沒交錯時，持續進行區間內元素的分類
            while (i <= j) {

                // 目的地是由大到小：左邊應該放大數
                // 只要 array[i] 大於或等於 pivot，代表位置正確，左指針 i 繼續往右推進
                while (i <= right && array[i] >= pivot) {
                    i++;
                }

                // 右邊應該放小數
                // 只要 array[j] 小於 pivot，代表位置正確，右指針 j 繼續往左推進
                while (j >= left + 1 && array[j] < pivot) {
                    j--;
                }

                // 當兩邊指針都停下來時，如果 i 還在 j 的左邊 (尚未交錯)
                // 代表兩邊各自抓到了「放錯位置的邊緣人」，必須互相交換
                if (i < j) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;

                    // 交換完畢後，雙指針各往前進一步，繼續下一次比對
                    i++;
                    j--;
                }
            }
            //╰（‵□′）╯ 指針分類終於結束了！

            // 此時 j 會停在所有「大於或等於 pivot」的數的最右側
            // 將位於 left 的基準點 (pivot) 與 j 位置的數對調，讓 pivot 正確歸位
            array[left] = array[j];
            array[j] = pivot;

            // 這一輪的切分與歸位（Partition）完成，立即印出目前整個陣列的最新狀態
            printarr(array, x);

            // 依據歸位後的基準點位置 j，切分成左右兩邊繼續遞迴排序
            sort(array, left, j - 1, x);  // 遞迴處理左半邊（全是大數的區間）
            sort(array, j + 1, right, x); // 遞迴處理右半邊（全是小數的區間）
        }
    }
}
//3秒排序法
//        Description
//
//這題我不會，我就沒有那麼快過，但我相信我的學弟妹們都那麼快吧，AC的人自動承認(好像不適合公開開玩笑，先在這道歉ORZ)
//
//在某個『競賽』中，選手們的秒數以一個整數序列表示。為了快速得到排名，評審決定使用一種高效率的排序方法——3秒排序（3Q Sort）。
//
//不過這次有點特別：
//成績越高的人要排在越前面（由大到小排序）。
//
//你的任務是模擬這個排序過程，並輸出每輪排序結果。
//
//
//Input
//
//第一行：一個整數n，表示數列長度
//第二行：n個整數，表示各個成績
//
//        Output
//
//輸出每輪排序完成後的序列（由大到小）
//
//
//Sample Input 1
//
//        5
//        3 1 4 5 2
//Sample Output 1
//
//        5 4 3 1 2
//        5 4 3 1 2
//        5 4 3 2 1
//Hint
//
//小心：
//
//無腦遞迴可能 TLE
//pivot 固定左邊 → 最壞O(n^2)
//AI 說的，看不懂 有人知道什麼是TLE嗎? telephone number?
//
//抱歉我也忘記這是什麼了，需要提示嗎?
//
