import java.util.Scanner;

// 課程類別
class Course2 {
    String courseId;
    int capacity;

    // 正取名單（最大長度就是 capacity）
    String[] roster;
    int rosterCount;

    // 候補佇列（最大可能長度為總操作數 Q）
    String[] waitlist;
    boolean[] waitValid; // 記錄該候補是否依然有效（若 DROP 候補則設為 false）
    int waitHead;        // 佇列前端索引
    int waitTail;        // 佇列後端索引

    public Course2(String courseId, int capacity, int maxOps) {
        this.courseId = courseId;
        this.capacity = capacity;
        this.roster = new String[capacity];
        this.rosterCount = 0;

        this.waitlist = new String[maxOps];
        this.waitValid = new boolean[maxOps];
        this.waitHead = 0;
        this.waitTail = 0;
    }

    // 檢查學生是否已經是正取
    public boolean inRoster(String student) {
        for (int i = 0; i < rosterCount; i++) {
            if (roster[i].equals(student)) return true;
        }
        return false;
    }

    // 檢查學生是否已經在候補中（且有效）
    public boolean inWaitlist(String student) {
        for (int i = waitHead; i < waitTail; i++) {
            if (waitlist[i].equals(student) && waitValid[i]) return true;
        }
        return false;
    }

    // 加選邏輯
    public void addStudent(String student) {
        // 規則：如果已經在正取或候補中，忽略
        if (inRoster(student) || inWaitlist(student)) {
            return;
        }

        // 還有空位，直接進入正取
        if (rosterCount < capacity) {
            roster[rosterCount] = student;
            rosterCount++;
        } else {
            // 沒空位，進入候補佇列
            waitlist[waitTail] = student;
            waitValid[waitTail] = true;
            waitTail++;
        }
    }

    // 退選邏輯
    public void dropStudent(String student) {
        // 情況 A：學生在正取中
        int rosterIdx = -1;
        for (int i = 0; i < rosterCount; i++) {
            if (roster[i].equals(student)) {
                rosterIdx = i;
                break;
            }
        }

        if (rosterIdx != -1) {
            // 從正取中移除（後方學生往前遞補移平陣列）
            for (int i = rosterIdx; i < rosterCount - 1; i++) {
                roster[i] = roster[i + 1];
            }
            rosterCount--;

            // 觸發候補遞補：尋找第一個有效的候補學生
            while (waitHead < waitTail) {
                if (waitValid[waitHead]) {
                    // 找到有效候補，拉進正取
                    roster[rosterCount] = waitlist[waitHead];
                    rosterCount++;
                    waitValid[waitHead] = false; // 已遞補，設為無效
                    waitHead++;
                    break;
                }
                waitHead++; // 跳過已作廢的候補
            }
            return;
        }

        // 情況 B：學生在候補中，取消候補
        for (int i = waitHead; i < waitTail; i++) {
            if (waitlist[i].equals(student) && waitValid[i]) {
                waitValid[i] = false; // 標記為作廢，不影響佇列指標
                break;
            }
        }
    }
}

public class Course_addition_and_withdrawal_simulator {
    // 輔助方法：根據 ID 尋找課程索引
    private static int findCourseIndex(Course2[] courses, int cCount, String id) {
        for (int i = 0; i < cCount; i++) {
            if (courses[i].courseId.equals(id)) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int C = sc.nextInt();
        int Q = sc.nextInt();

        Course2[] courses = new Course2[C];
        for (int i = 0; i < C; i++) {
            String courseId = sc.next();
            int capacity = sc.nextInt();
            courses[i] = new Course2(courseId, capacity, Q);
        }

        // 模擬 Q 筆指令
        for (int i = 0; i < Q; i++) {
            String action = sc.next();
            String student = sc.next();
            String courseId = sc.next();

            int cIdx = findCourseIndex(courses, C, courseId);
            if (cIdx == -1) continue;

            if (action.equals("ADD")) {
                courses[cIdx].addStudent(student);
            } else if (action.equals("DROP")) {
                courses[cIdx].dropStudent(student);
            }
        }

        // 依照原始課程輸入順序輸出結果
        for (int i = 0; i < C; i++) {
            Course2 course = courses[i];

            // 輸出前，正取學生名單需依字典序排序 (氣泡排序法)
            for (int j = 0; j < course.rosterCount - 1; j++) {
                for (int k = 0; k < course.rosterCount - 1 - j; k++) {
                    if (course.roster[k].compareTo(course.roster[k + 1]) > 0) {
                        String temp = course.roster[k];
                        course.roster[k] = course.roster[k + 1];
                        course.roster[k + 1] = temp;
                    }
                }
            }

            // 列印結果
            System.out.print(course.courseId + " " + course.rosterCount);
            for (int j = 0; j < course.rosterCount; j++) {
                System.out.print(" " + course.roster[j]);
            }
            System.out.println();
        }

        sc.close();
    }
}
//課程加退選模擬器
//Description
//
//選課系統需要處理加選、退選與候補遞補。每門課有容量限制，學生加選時若課程已滿，會進入該課程的候補佇列。學生退選後，候補佇列中最早仍有效的學生會自動遞補。
//
//同一位學生對同一門課若已在正取或候補中，再次 ADD 需忽略；DROP 正取會釋出名額，DROP 候補則取消候補，其他情況忽略。
//
//本題建議以 Course 類別封裝容量、正取集合與候補佇列，練習 class、encapsulation、Queue、Set 與模擬。
//
//
//Input
//
//第一行輸入 C Q。接下來 C 行為課程資料：
//
//courseId capacity
//接下來 Q 行為操作：
//
//ADD student courseId
//DROP student courseId
//
//Output
//
//依照課程輸入順序輸出每門課最後正取名單：
//
//courseId count name1 name2 ...
//學生姓名請依字典序輸出。若沒有正取學生，只輸出courseId 0。

//Hint
//
//1 <= C <= 1000
//0 <= Q <= 200000
//1 <= capacity <= 1000
//courseId 與 student 長度 <= 20
//建議使用 Map<String, Course>、HashSet 與 Queue。