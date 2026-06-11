import java.util.Scanner;

// 1. 定義單純的學生類別，只用來存放資料
class Student {
    String name;
    int solved;   // 解題數
    int penalty;  // 總罰時

    // 用來記錄這名學生每一題的狀態
    boolean[] isSolved; // isSolved[pIdx] 為 true 代表這題已經 AC
    int[] waCount;      // waCount[pIdx] 記錄這題在 AC 之前錯了幾次

    public Student(String name, int maxProblems) {
        this.name = name;
        this.solved = 0;
        this.penalty = 0;
        this.isSolved = new boolean[maxProblems];
        this.waCount = new int[maxProblems];
    }
}

public class Ranking_of_Homework_Submissions {

    // 核心排序：手寫快速排序法 (Quick Sort) —— 依解題數、總罰時、姓名字典序排序
    private static void quickSortStudents(Student[] arr, int low, int high) {
        if (low < high) {
            Student pivot = arr[high];
            int i = low - 1;

            for (int j = low; j < high; j++) {
                boolean needMoveAhead = false;

                // 條件一：解題數多者排在前面 (大到小)
                if (arr[j].solved > pivot.solved) {
                    needMoveAhead = true;
                } else if (arr[j].solved == pivot.solved) {
                    // 條件二：解題數相同，總罰時少者排在前面 (小到大)
                    if (arr[j].penalty < pivot.penalty) {
                        needMoveAhead = true;
                    } else if (arr[j].penalty == pivot.penalty) {
                        // 條件三：總罰時相同，依姓名字典序排在前面 (小到大)
                        if (arr[j].name.compareTo(pivot.name) < 0) {
                            needMoveAhead = true;
                        }
                    }
                }

                if (needMoveAhead) {
                    i++;
                    Student temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            Student temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            int pi = i + 1;
            quickSortStudents(arr, low, pi - 1);
            quickSortStudents(arr, pi + 1, high);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int N = sc.nextInt();
        int M = sc.nextInt();

        // 防止 M 為 0 時建立長度為 0 的陣列導致錯誤
        int maxProblems = M;
        if (maxProblems < 1) {
            maxProblems = 1;
        }

        // 1. 讀取學生的姓名並建立學生玩家物件
        Student[] students = new Student[N];
        for (int i = 0; i < N; i++) {
            students[i] = new Student(sc.next(), maxProblems);
        }

        // 用來登記所有出現過的題目名稱，不重複的題目最多就是 M 個
        String[] problems = new String[maxProblems];
        int pCount = 0; // 目前登記了幾道題目

        // 2. 處理 M 筆送出紀錄
        for (int i = 0; i < M; i++) {
            int time = sc.nextInt();
            String studentName = sc.next();
            String probName = sc.next();
            String verdict = sc.next();

            // 步驟 A：尋找目前是哪位學生提交
            int sIdx = -1;
            for (int j = 0; j < N; j++) {
                if (students[j].name.equals(studentName)) {
                    sIdx = j;
                    break;
                }
            }
            if (sIdx == -1) continue; // 安全防護：找不到該學生就忽略這筆

            // 步驟 B：尋找題目對應的整數編號 (ID)
            int pIdx = -1;
            for (int j = 0; j < pCount; j++) {
                if (problems[j].equals(probName)) {
                    pIdx = j;
                    break;
                }
            }
            // 如果這道題目第一次出現，就登記到 problems 陣列中
            if (pIdx == -1) {
                problems[pCount] = probName;
                pIdx = pCount;
                pCount++;
            }

            // 步驟 C：根據 ACM 規則更新學生的分數與狀態
            Student currentStudent = students[sIdx];

            // 規則：如果這一題已經 AC 過了，後續的提交（不論 AC 或 WA）全部忽略
            if (currentStudent.isSolved[pIdx]) {
                continue;
            }

            if (verdict.equals("AC")) {
                // 標記為已解出
                currentStudent.isSolved[pIdx] = true;
                currentStudent.solved++;
                // 罰時計算 = 當前 AC 時間 + 之前錯的次數 * 20
                currentStudent.penalty += time + (currentStudent.waCount[pIdx] * 20);
            } else if (verdict.equals("WA")) {
                // 如果是 WA 且還沒 AC，就單純累積錯誤次數
                currentStudent.waCount[pIdx]++;
            }
        }

        // 3. 排序全體學生 (使用快速排序法，應付 N=1000, M=200000 的大測資)
        if (N > 0) {
            quickSortStudents(students, 0, N - 1);
        }

        // 4. 計算競賽並列排名並輸出結果
        int currentRank = 1;
        for (int i = 0; i < N; i++) {
            // 如果不是第一個學生，且當前學生的「解題數」或「罰時」與前一個人不同
            if (i > 0) {
                if (students[i].solved != students[i - 1].solved || students[i].penalty != students[i - 1].penalty) {
                    // 名次刷新為當前排在陣列的順位位置 + 1
                    currentRank = i + 1;
                }
            }
            // 輸出結果：名次 姓名 解題數 總罰時
            System.out.println(currentRank + " " + students[i].name + " " + students[i].solved + " " + students[i].penalty);
        }

        sc.close();
    }
}
//補交作業排行榜
//Description
//
//助教忘記準時發作業，只好開放補交系統。系統會記錄每位同學對每一題的送出結果，請根據 ACM 競賽排名規則建立最後排行榜。
//
//同一位學生同一題第一次 AC 才算解出，第一次 AC 之前的每次 WA 會增加 20 分鐘罰時；AC 之後同題送出全部忽略。
//
//排名依序比較：解題數多者較前、總罰時少者較前、姓名字典序小者較前。若解題數與總罰時都相同，使用競賽排名並列。
//
//
//Input
//
//第一行輸入 N M。第二行輸入 N 個學生姓名。接下來 M 行，每行為一次送出紀錄。
//
//time name problem verdict
//time：送出時間，整數分鐘
//name：學生姓名
//problem：題號，不含空白
//verdict：只會是 AC 或 WA
//
//Output
//
//依照排名後順序輸出 N 行。
//
//rank name solved penalty
//

//Hint
//
//1 <= N <= 1000
//0 <= M <= 200000
//1 <= time <= 1000000
//name 與 problem 長度 <= 20
//建議使用類別保存學生狀態，並使用 Map 紀錄每題錯誤次數與是否已 AC。