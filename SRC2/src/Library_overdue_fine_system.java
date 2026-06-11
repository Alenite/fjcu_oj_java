import java.util.Scanner; // 嚴格限制：只能 import Scanner

public class Library_overdue_fine_system {

    // 手寫核心快速排序法 (Quick Sort) —— 嚴格遵循題目的 4 層排序規則
    private static void quickSortRecords(BookRecord[] arr, int low, int high) {
        if (low < high) {
            BookRecord pivot = arr[high]; // 選擇最後一個元素作為基準點
            int i = low - 1;

            for (int j = low; j < high; j++) {
                boolean needMoveAhead = false;

                // 規則 1：最終罰款高者優先 (大到小)
                if (arr[j].finalFine > pivot.finalFine) {
                    needMoveAhead = true;
                } else if (arr[j].finalFine == pivot.finalFine) {

                    // 規則 2：最終罰款相同，逾期天數多者優先 (大到小)
                    if (arr[j].lateDays > pivot.lateDays) {
                        needMoveAhead = true;
                    } else if (arr[j].lateDays == pivot.lateDays) {

                        // 規則 3：逾期天數也相同，書籍類型優先順序為 RARE > REFERENCE > NORMAL
                        int wj = getTypeWeight(arr[j].type);
                        int wPivot = getTypeWeight(pivot.type);
                        if (wj > wPivot) {
                            needMoveAhead = true;
                        } else if (wj == wPivot) {

                            // 規則 4：若仍相同，依照輸入順序由早到晚輸出 (id 小的在前面)
                            if (arr[j].id < pivot.id) {
                                needMoveAhead = true;
                            }
                        }
                    }
                }

                // 如果滿足排在前面的條件，則交換元素位置
                if (needMoveAhead) {
                    i++;
                    BookRecord temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            // 將基準點換回正確的位置
            BookRecord temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            int pi = i + 1; // 取得切分點
            quickSortRecords(arr, low, pi - 1);  // 遞迴排序左半邊
            quickSortRecords(arr, pi + 1, high); // 遞迴排序右半邊
        }
    }

    // 輔助方法：將書籍類型轉換為權重，方便進行大小比較 (RARE 最高，NORMAL 最低)
    private static int getTypeWeight(String type) {
        if (type.equals("RARE")) return 3;
        if (type.equals("REFERENCE")) return 2;
        return 1; // NORMAL
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        if (!scan.hasNextInt()) return;
        int n = scan.nextInt();
        BookRecord[] records = new BookRecord[n]; // 宣告原生長度的物件陣列

        for (int i = 0; i < n; i++) {
            String studentName = scan.next();
            String bookName = scan.next();
            int borrowDay = scan.nextInt();
            int dueDay = scan.nextInt();
            int actualReturnDay = scan.nextInt();
            String type = scan.next();

            // 1. 計算逾期天數 (若準時或早還，逾期天數為 0)
            int lateDays = actualReturnDay - dueDay;
            if (lateDays < 0) {
                lateDays = 0;
            }

            // 2. 計算罰款
            int finalFine = 0;
            if (lateDays > 0) {
                // A. 決定每日罰款
                int rate = 5;
                if (type.equals("REFERENCE")) {
                    rate = 10;
                } else if (type.equals("RARE")) {
                    rate = 20;
                }

                // B. 計算基本罰款
                finalFine = lateDays * rate;

                // C. 額外罰款規則一：逾期天數大於等於 30 天，加收 100 元
                if (lateDays >= 30) {
                    finalFine += 100;
                }

                // D. 額外罰款規則二：書籍類型為 RARE 且逾期天數大於 0，加收 50 元
                if (type.equals("RARE")) {
                    finalFine += 50;
                }

                // E. 額外罰款規則三：實際還書日超過第 365 天，加收 200 元
                if (actualReturnDay > 365) {
                    finalFine += 200;
                }
            } else {
                // 若沒有逾期，最終罰款必定為 0 (題目特別規定)
                finalFine = 0;
            }

            // 將所有計算完的結果打包封裝存入陣列
            records[i] = new BookRecord(i, studentName, bookName, type, lateDays, finalFine);
        }

        // 3. 執行手寫的高效快速排序
        if (n > 0) {
            quickSortRecords(records, 0, n - 1);
        }

        // 4. 依序輸出排行榜 (名次依序遞增，不需要並列名次)
        for (int i = 0; i < n; i++) {
            System.out.printf("%d %s %s %d %d%n", (i + 1), records[i].studentName, records[i].bookName, records[i].lateDays, records[i].finalFine);
        }

        scan.close();
    }
}

// 定義借書紀錄類別，用來儲存及傳遞單筆資料欄位
class BookRecord {
    int id;               // 原始輸入順序
    String studentName;   // 學生姓名
    String bookName;      // 書籍名稱
    String type;          // 書籍類型
    int lateDays;         // 逾期天數
    int finalFine;        // 最終罰款

    public BookRecord(int id, String studentName, String bookName, String type, int lateDays, int finalFine) {
        this.id = id;
        this.studentName = studentName;
        this.bookName = bookName;
        this.type = type;
        this.lateDays = lateDays;
        this.finalFine = finalFine;
    }
}
//Description
//
//學校圖書館想要建立一個借書紀錄管理系統。每筆借書紀錄包含學生姓名、書籍名稱、借書日、應還日、實際還書日與書籍類型。
//
//不同書籍類型有不同的每日逾期罰款：
//
//書籍類型	每日罰款
//NORMAL	5
//REFERENCE	10
//RARE	20
//逾期天數計算方式：
//
//lateDays = actualReturnDay - dueDay
//若 actualReturnDay <= dueDay，逾期天數為 0。
//
//基本罰款計算方式：
//
//baseFine = lateDays * 每日罰款
//額外罰款規則如下：
//
//若逾期天數大於等於 30 天，額外加收 100 元。
//若書籍類型為 RARE 且逾期天數大於 0，額外加收 50 元。
//若實際還書日超過第 365 天，額外加收 200 元。
//若沒有逾期，最終罰款必定為 0，不加任何額外罰款。
//請依照以下規則排序輸出：
//
//最終罰款高者優先。
//若最終罰款相同，逾期天數多者優先。
//若逾期天數也相同，書籍類型優先順序為：RARE > REFERENCE > NORMAL。
//若仍相同，依照輸入順序由早到晚輸出。
//注意：即使罰款相同，名次仍依序遞增，不需要並列名次。
//
//
//Input
//
//第一行輸入一個整數 N，代表借書紀錄數量。
//
//接下來 N 行，每行包含：
//
//studentName bookName borrowDay dueDay actualReturnDay type
//其中：
//
//studentName：學生姓名，不含空白
//bookName：書籍名稱，不含空白
//borrowDay：借書日
//dueDay：應還日
//actualReturnDay：實際還書日
//type：書籍類型，只會是 NORMAL、REFERENCE 或 RARE
//日期皆以整數表示。
//
//
//Output
//
//依照罰款排行榜排序後，輸出每筆紀錄一行：
//
//rank studentName bookName lateDays finalFine
//其中：
//
//rank：名次，從 1 開始
//lateDays：逾期天數
//finalFine：最終罰款

//Hint
//
//輸入限制：
//
//1 <= N <= 1000
//studentName 長度 <= 20
//bookName 長度 <= 20
//1 <= borrowDay <= dueDay <= 365
//1 <= actualReturnDay <= 500
//type 只會是 NORMAL、REFERENCE 或 RARE