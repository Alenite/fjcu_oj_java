import java.util.Scanner;

// 定義課程父類別
abstract class Course {
    String courseId;
    String type;
    int credit;
    int difficulty;
    int semester; // 紀錄最早可修學期

    public Course(String courseId, String type, int credit, int difficulty) {
        this.courseId = courseId;
        this.type = type;
        this.credit = credit;
        this.difficulty = difficulty;
        this.semester = 1; // 預設最早是第 1 學期
    }

    // 抽象方法：由子類別各自實作權重公式
    public abstract int getWeight();
}

// 子類別：核心必修
class CORE extends Course {
    public CORE(String courseId, String type, int credit, int difficulty) {
        super(courseId, type, credit, difficulty);
    }
    @Override
    public int getWeight() {
        return this.credit * 100 + this.difficulty * 10;
    }
}

// 子類別：實驗課
class LAB2 extends Course {
    public LAB2(String courseId, String type, int credit, int difficulty) {
        super(courseId, type, credit, difficulty);
    }
    @Override
    public int getWeight() {
        return this.credit * 120 + this.difficulty * 15;
    }
}

// 子類別：選修課
class ELECTIVE extends Course {
    public ELECTIVE(String courseId, String type, int credit, int difficulty) {
        super(courseId, type, credit, difficulty);
    }
    @Override
    public int getWeight() {
        return this.credit * 80 + this.difficulty * 5;
    }
}

public class Course_Prerequisite_Sort_Planner {

    // 輔助方法：在陣列中尋找課程 ID 對應的索引位置
    public static int findIndex(Course[] courses, int n, String id) {
        for (int i = 0; i < n; i++) {
            if (courses[i].courseId.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int N = sc.nextInt();
        int M = sc.nextInt();

        Course[] courses = new Course[N];

        // 1. 讀取 N 門課程
        for (int i = 0; i < N; i++) {
            String courseId = sc.next();
            String type = sc.next();
            int credit = sc.nextInt();
            int difficulty = sc.nextInt();

            if (type.equals("CORE")) {
                courses[i] = new CORE(courseId, type, credit, difficulty);
            } else if (type.equals("LAB")) {
                courses[i] = new LAB2(courseId, type, credit, difficulty);
            } else if (type.equals("ELECTIVE")) {
                courses[i] = new ELECTIVE(courseId, type, credit, difficulty);
            }
        }

        // 2. 建立鄰接矩陣與入度陣列
        boolean[][] adj = new boolean[N][N];
        int[] inDegree = new int[N];

        for (int i = 0; i < M; i++) {
            String beforeId = sc.next();
            String afterId = sc.next();

            int u = findIndex(courses, N, beforeId);
            int v = findIndex(courses, N, afterId);

            if (u != -1 && v != -1 && !adj[u][v]) {
                adj[u][v] = true;
                inDegree[v]++; // 後續課程的入度 + 1
            }
        }

        // 3. 手寫拓樸排序
        boolean[] visited = new boolean[N];
        int processedCount = 0;

        for (int step = 0; step < N; step++) {
            int targetIdx = -1;

            // 尋找一個入度為 0 且尚未處理過的節點
            for (int i = 0; i < N; i++) {
                if (inDegree[i] == 0 && !visited[i]) {
                    targetIdx = i;
                    break;
                }
            }

            // 如果找不到入度為 0 的點，代表有循環（死鎖）
            if (targetIdx == -1) {
                System.out.println("IMPOSSIBLE");
                sc.close();
                return;
            }

            // 標記為已處理
            visited[targetIdx] = true;
            processedCount++;

            // 更新所有以 targetIdx 為先修的課程
            for (int nextIdx = 0; nextIdx < N; nextIdx++) {
                if (adj[targetIdx][nextIdx]) {
                    // 計算下修課程的最早學期（先修學期 + 1）
                    int nextSemester = courses[targetIdx].semester + 1;
                    if (nextSemester > courses[nextIdx].semester) {
                        courses[nextIdx].semester = nextSemester;
                    }

                    inDegree[nextIdx]--; // 先修條件少一個
                }
            }
        }

        // 4. 使用基礎「氣泡排序法」對結果進行多條件排序
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N - 1 - i; j++) {
                Course c1 = courses[j];
                Course c2 = courses[j + 1];

                boolean needSwap = false;

                // 條件一：學期由小到大
                if (c1.semester > c2.semester) {
                    needSwap = true;
                } else if (c1.semester == c2.semester) {
                    // 條件二：權重由大到小
                    if (c1.getWeight() < c2.getWeight()) {
                        needSwap = true;
                    } else if (c1.getWeight() == c2.getWeight()) {
                        // 條件三：課程編號字典序由小到大
                        if (c1.courseId.compareTo(c2.courseId) > 0) {
                            needSwap = true;
                        }
                    }
                }

                if (needSwap) {
                    Course temp = courses[j];
                    courses[j] = courses[j + 1];
                    courses[j + 1] = temp;
                }
            }
        }

        // 5. 輸出排序後的最終結果
        for (int i = 0; i < N; i++) {
            System.out.println(courses[i].courseId + " " + courses[i].semester + " " + courses[i].getWeight());
        }

        sc.close();
    }
}
//課程先修排序規劃器
//Description
//
//大二學生正在安排課程。每門課有類型、學分與難度，不同類型有不同權重公式。若課程 A 是課程 B 的先修，則 B 至少要在 A 的下一個學期才能修。
//
//請判斷是否存在合法修課順序。若存在，輸出每門課最早可修學期與權重；若有循環先修，輸出 IMPOSSIBLE。
//
//本題建議建立 Course 父類別與 CORE、LAB、ELECTIVE 子類別覆寫 getWeight 方法，並搭配拓樸排序。
//
//
//Input
//
//第一行輸入 N M。接下來 N 行：
//
//courseId type credit difficulty
//接下來 M 行：
//
//before after
//
//Output
//
//若有循環，輸出：
//
//IMPOSSIBLE
//否則依學期小到大、權重大到小、課程編號字典序小到大輸出：
//
//courseId semester weight

//Hint
//
//CORE weight = credit*100 + difficulty*10
//LAB weight = credit*120 + difficulty*15
//ELECTIVE weight = credit*80 + difficulty*5