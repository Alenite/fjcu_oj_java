import java.util.Scanner; // 嚴格限制：只能 import Scanner

public class Club_Points_Ranking {

    // 手寫核心快速排序法 (Quick Sort) —— 嚴格遵循題目的 4 層排序規則
    private static void quickSortMembers(Member[] arr, int low, int high) {
        if (low < high) {
            Member pivot = arr[high]; // 選擇最後一個元素作為基準點
            int i = low - 1;

            for (int j = low; j < high; j++) {
                boolean needMoveAhead = false;

                // 規則 1：最終總積分高者優先 (大到小)
                if (arr[j].finalScore > pivot.finalScore) {
                    needMoveAhead = true;
                } else if (arr[j].finalScore == pivot.finalScore) {

                    // 規則 2：總積分相同，平均分數高者優先 (大到小)
                    if (arr[j].averageScore > pivot.averageScore) {
                        needMoveAhead = true;
                    } else if (arr[j].averageScore == pivot.averageScore) {

                        // 規則 3：平均分數也相同，參加活動次數多者優先 (大到小)
                        if (arr[j].kCount > pivot.kCount) {
                            needMoveAhead = true;
                        } else if (arr[j].kCount == pivot.kCount) {

                            // 規則 4：若仍相同，依照輸入順序由早到晚輸出 (id 小的在前面)
                            if (arr[j].id < pivot.id) {
                                needMoveAhead = true;
                            }
                        }
                    }
                }

                // 若滿足排在前面的條件，則交換物件在陣列中的位置
                if (needMoveAhead) {
                    i++;
                    Member temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            // 將基準點換回正確的位置
            Member temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            int pi = i + 1; // 取得切分點
            quickSortMembers(arr, low, pi - 1);  // 遞迴排序左半邊
            quickSortMembers(arr, pi + 1, high); // 遞迴排序右半邊
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        if (!scan.hasNextInt()) return;
        int n = scan.nextInt();
        Member[] members = new Member[n]; // 宣告原生長度的物件陣列

        for (int i = 0; i < n; i++) {
            String name = scan.next();
            String club = scan.next();
            int k = scan.nextInt();

            int[] scores = new int[k];
            int sum = 0;
            boolean hasLessThan60 = false;

            // 讀取每場活動分數，同時計算總和與檢查是否有人低於 60 分
            for (int j = 0; j < k; j++) {
                scores[j] = scan.nextInt();
                sum += scores[j];
                if (scores[j] < 60) {
                    hasLessThan60 = true;
                }
            }

            // 計算初始平均分數
            double avg = (double) sum / k;

            // 依題目規則計算最終總積分
            int finalScore = sum;
            if (k >= 5) {
                finalScore += 20; // 規則一：活動次數 >= 5 加 20 分
            }
            if (avg >= 90) {
                finalScore += 30; // 規則二：平均分數 >= 90 加 30 分
            }
            if (hasLessThan60) {
                finalScore -= 15; // 規則三：任一場分數 < 60 扣 15 分
            }
            if (finalScore < 0) {
                finalScore = 0;   // 規則四：總積分小於 0 視為 0
            }

            // 根據最終總積分劃分活躍等級
            String level;
            if (finalScore >= 500) {
                level = "S";
            } else if (finalScore >= 300) {
                level = "A";
            } else if (finalScore >= 150) {
                level = "B";
            } else {
                level = "C";
            }

            // 將所有計算好的資料建立成 Member 物件存入陣列
            members[i] = new Member(i, name, club, k, finalScore, avg, level);
        }

        // 執行手寫的高效快速排序
        if (n > 0) {
            quickSortMembers(members, 0, n - 1);
        }

        // 輸出結果：名次依序遞增，不需要並列名次 (1 到 n)
        for (int i = 0; i < n; i++) {
            System.out.printf("%d %s %s %d %.2f %s%n",
                    (i + 1),
                    members[i].name,
                    members[i].club,
                    members[i].finalScore,
                    members[i].averageScore,
                    members[i].level
            );
        }

        scan.close();
    }
}

// 定義成員類別，用來整合單筆成員的所有資料欄位
class Member {
    int id;               // 原始輸入順序 (用於第四層排序)
    String name;          // 成員姓名
    String club;          // 社團名稱
    int kCount;           // 參加活動次數
    int finalScore;       // 最終總積分
    double averageScore;  // 平均分數
    String level;         // 活躍等級

    public Member(int id, String name, String club, int kCount, int finalScore, double averageScore, String level) {
        this.id = id;
        this.name = name;
        this.club = club;
        this.kCount = kCount;
        this.finalScore = finalScore;
        this.averageScore = averageScore;
        this.level = level;
    }
}
//社團積分排行榜
//Description
//
//開學後，學校舉辦社團活動競賽。每位社團成員都會參加多場活動，每場活動會根據表現獲得不同分數。
//
//每位成員都有：
//
//姓名
//社團名稱
//參加活動次數
//多場活動分數
//請你建立Member類別，計算每位成員的總積分、平均分數、活躍等級，並輸出社團積分排行榜。
//
//每位成員的最終總積分計算方式如下：
//
//先加總所有活動分數。
//若參加活動次數大於等於 5，額外加 20 分。
//若平均分數大於等於 90，額外加 30 分。
//若任一場活動分數低於 60，總積分扣 15 分。
//若總積分小於 0，視為 0。
//活躍等級規則如下：
//
//最終總積分	等級
//score >= 500	S
//score >= 300	A
//score >= 150	B
//score < 150	C
//請依照以下規則排序輸出：
//
//最終總積分高者優先。
//若總積分相同，平均分數高者優先。
//若平均分數也相同，參加活動次數多者優先。
//若仍相同，依照輸入順序由早到晚輸出。
//
//Input
//
//第一行輸入一個整數N，代表成員數量。
//
//接下來N組資料，每組資料格式如下：
//
//第一行包含：
//
//name club K
//第二行包含K個整數：
//
//score1 score2 ... scoreK
//其中：
//
//name：成員姓名，不含空白
//club：社團名稱，不含空白
//K：參加活動次數
//scorei：第i場活動分數
//
//Output
//
//依照排行榜排序後，輸出每位成員一行：
//
//rank name club finalScore average level
//其中：
//
//rank：名次，從 1 開始
//finalScore：最終總積分
//average：平均分數，輸出到小數點後 2 位
//level：活躍等級
//注意：即使分數相同，名次仍依序遞增，不需要並列名次。

//Hint
//
//1 <= N <= 1000
//
//name 長度 <= 20
//
//club 長度 <= 20
//
//1 <= K <= 20
//
//0 <= scorei <= 100