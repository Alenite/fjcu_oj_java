import java.util.Scanner; // 嚴格限制：只能 import Scanner

public class Student_ID_card_making_machine {

    // 手寫核心快速排序法 (Quick Sort) —— 確保大測資不超時，並嚴格遵循排序規則
    private static void quickSortStudents(Student2[] arr, int low, int high) {
        if (low < high) {
            Student2 pivot = arr[high]; // 選擇最後一個元素作為基準點
            int i = low - 1;

            for (int j = low; j < high; j++) {
                boolean needMoveAhead = false;

                // 規則 1：平均分數由高到低排序 (大到小)
                if (arr[j].average > pivot.average) {
                    needMoveAhead = true;
                } else if (arr[j].average == pivot.average) {

                    // 規則 2：如果平均分數相同，依照輸入順序由早到晚輸出 (idIndex 小的在前面)
                    if (arr[j].idIndex < pivot.idIndex) {
                        needMoveAhead = true;
                    }
                }

                // 如果滿足排在前面的條件，則交換物件在陣列中的位置
                if (needMoveAhead) {
                    i++;
                    Student2 temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            // 將基準點換回正確的位置
            Student2 temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            int pi = i + 1; // 取得切分點
            quickSortStudents(arr, low, pi - 1);  // 遞迴排序左半邊
            quickSortStudents(arr, pi + 1, high); // 遞迴排序右半邊
        }
    }

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        if (!scan.hasNextInt()) return;
        int n = scan.nextInt();
        Student2[] students = new Student2[n]; // 宣告原生長度的物件陣列

        // 1. 讀入資料並建立物件
        for (int i = 0; i < n; i++) {
            String name = scan.next();
            String id = scan.next();
            int age = scan.nextInt();
            int chi = scan.nextInt();
            int eng = scan.nextInt();
            int math = scan.nextInt();

            // 將原本的資料加上當前的索引 i (代表輸入順序) 一併傳入建構子
            students[i] = new Student2(i, name, id, age, chi, eng, math);
        }

        // 2. 執行手寫的高效快速排序法
        if (n > 0) {
            quickSortStudents(students, 0, n - 1);
        }

        // 3. 依序輸出排序後的學生證資訊
        for (int i = 0; i < n; i++) {
            Student2 s = students[i];
            System.out.printf("%s %s %.2f %s%n", s.name, s.id, s.average, s.status);
        }

        scan.close();
    }
}

// 封裝每位學生資訊與狀態判斷的類別
class Student2 {
    int idIndex;     // 用於記錄原始輸入順序的索引值
    String name;
    String id;
    double average;
    String status;

    public Student2(int idIndex, String name, String id, int age, int chi, int eng, int math) {
        this.idIndex = idIndex;
        this.name = name;
        this.id = id;
        this.average = (chi + eng + math) / 3.0;

        // 嚴格按照題目從上到下的條件順序判斷學生證狀態
        if (age < 18) {
            this.status = "UNDERAGE";
        } else if (chi < 60 || eng < 60 || math < 60) {
            this.status = "RETAKE";
        } else if (this.average >= 90) {
            this.status = "HONOR";
        } else {
            this.status = "NORMAL";
        }
    }
}
//學生證製作機
//Description
//
//學校要替新生製作學生證，但這次資料不是單純輸出而已。
//
//每位學生都有：
//
//姓名
//學號
//年齡
//國文成績
//英文成績
//數學成績
//請你建立Student類別，讀入多位學生資料，計算每位學生的平均分數，並根據規則判斷學生證狀態。
//
//學生證狀態規則如下：
//
//條件	狀態
//年齡小於 18	UNDERAGE
//任一科成績低於 60	RETAKE
//平均分數大於等於 90	HONOR
//其他情況	NORMAL
//判斷順序必須由上到下。也就是說，如果學生年齡小於 18，即使平均分數很高，狀態仍然是UNDERAGE。
//
//最後請依照平均分數由高到低排序輸出學生證資訊。
//如果平均分數相同，請依照輸入順序由早到晚輸出。
//
//
//Input
//
//第一行輸入一個整數N，代表學生人數。
//
//接下來N行，每行包含：
//
//name id age chinese english math
//其中：
//
//name：學生姓名，不含空白
//id：學生學號，不含空白
//age：學生年齡
//chinese：國文成績
//english：英文成績
//math：數學成績
//
//Output
//
//請依照排序後的順序輸出每位學生的學生證資訊。
//
//每位學生輸出一行：
//
//name id average status
//其中average請輸出到小數點後 2 位。

//Hint
//
//輸入限制
//1 <= N <= 1000
//name 長度 <= 20
//id 長度 <= 20
//1 <= age <= 120
//0 <= chinese, english, math <= 100