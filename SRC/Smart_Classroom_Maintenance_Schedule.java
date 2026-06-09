import java.util.Scanner;

// 1. 定義設備父類別與子類別（多型）
abstract class Equipment {
    String id;
    String type;
    int age;
    int hours;
    int errors;
    int cost;
    String critical;

    public Equipment(String id, String type, int age, int hours, int errors, int cost, String critical) {
        this.id = id;
        this.type = type;
        this.age = age;
        this.hours = hours;
        this.errors = errors;
        this.cost = cost;
        this.critical = critical;
    }

    public abstract int getBaseValue();

    // 計算優先值公式
    public int getPriorityValue() {
        int criticalBonus = this.critical.equals("Y") ? 50 : 0;
        return getBaseValue() + this.age * 3 + (this.hours / 100) + this.errors * 12 + criticalBonus;
    }
}

class SENSOR extends Equipment {
    public SENSOR(String id, String type, int age, int hours, int errors, int cost, String critical) { super(id, type, age, hours, errors, cost, critical); }
    @Override public int getBaseValue() { return 20; }
}

class CAMERA2 extends Equipment {
    public CAMERA2(String id, String type, int age, int hours, int errors, int cost, String critical) { super(id, type, age, hours, errors, cost, critical); }
    @Override public int getBaseValue() { return 35; }
}

class DOOR2 extends Equipment {
    public DOOR2(String id, String type, int age, int hours, int errors, int cost, String critical) { super(id, type, age, hours, errors, cost, critical); }
    @Override public int getBaseValue() { return 40; }
}

class SERVER extends Equipment {
    public SERVER(String id, String type, int age, int hours, int errors, int cost, String critical) { super(id, type, age, hours, errors, cost, critical); }
    @Override public int getBaseValue() { return 60; }
}

// 2. 定義 DP 狀態類別（用來儲存背包中的綜合狀態）
class DpState {
    int maxValue;
    int totalCost;
    String selectedIds; // 儲存已選擇的 ID，以空格相連

    public DpState(int maxValue, int totalCost, String selectedIds) {
        this.maxValue = maxValue;
        this.totalCost = totalCost;
        this.selectedIds = selectedIds;
    }
}

public class Smart_Classroom_Maintenance_Schedule {
    // 手寫高效快速排序法：預先將設備依 ID 字典序從小到大排序
    private static void quickSortEquipments(Equipment[] arr, int low, int high) {
        if (low < high) {
            Equipment pivot = arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (arr[j].id.compareTo(pivot.id) < 0) {
                    i++;
                    Equipment temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            Equipment temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            int pi = i + 1;
            quickSortEquipments(arr, low, pi - 1);
            quickSortEquipments(arr, pi + 1, high);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int N = sc.nextInt();
        int B = sc.nextInt();

        Equipment[] items = new Equipment[N];
        for (int i = 0; i < N; i++) {
            String id = sc.next();
            String type = sc.next();
            int age = sc.nextInt();
            int hours = sc.nextInt();
            int errors = sc.nextInt();
            int cost = sc.nextInt();
            String critical = sc.next();

            if (type.equals("SENSOR")) items[i] = new SENSOR(id, type, age, hours, errors, cost, critical);
            else if (type.equals("CAMERA")) items[i] = new CAMERA2(id, type, age, hours, errors, cost, critical);
            else if (type.equals("DOOR")) items[i] = new DOOR2(id, type, age, hours, errors, cost, critical);
            else if (type.equals("SERVER")) items[i] = new SERVER(id, type, age, hours, errors, cost, critical);
        }

        // 關鍵核心：先將設備依 ID 排序。這樣未來 DP 組出的 id 序列自然就會是由小到大的順序
        quickSortEquipments(items, 0, N - 1);

        // 3. 初始化 DP 陣列
        DpState[] dp = new DpState[B + 1];
        for (int j = 0; j <= B; j++) {
            dp[j] = new DpState(0, 0, "");
        }

        // 4. 開始 0/1 背包動態規劃
        for (int i = 0; i < N; i++) {
            int cost = items[i].cost;
            int value = items[i].getPriorityValue();
            String id = items[i].id;

            // 倒序遍歷背包容量，避免同個物品重複選擇
            for (int j = B; j >= cost; j--) {
                // 如果前驅狀態有效（雖然初始全為 0 可直接轉移，但要加上新物品的價值與成本）
                int newValue = dp[j - cost].maxValue + value;
                int newCost = dp[j - cost].totalCost + cost;

                // 拼接新字串（若前驅字串為空，直接為 id；否則加上空格）
                String newIds = dp[j - cost].selectedIds.equals("") ? id : dp[j - cost].selectedIds + " " + id;

                // 與不放該物品的原狀態 dp[j] 進行比對
                boolean shouldUpdate = false;
                if (newValue > dp[j].maxValue) {
                    shouldUpdate = true;
                } else if (newValue == dp[j].maxValue) {
                    if (newCost < dp[j].totalCost) {
                        shouldUpdate = true;
                    } else if (newCost == dp[j].totalCost) {
                        // 優先值與成本皆平手，比較設備編號序列的字典序
                        if (newIds.compareTo(dp[j].selectedIds) < 0) {
                            shouldUpdate = true;
                        }
                    }
                }

                if (shouldUpdate) {
                    dp[j] = new DpState(newValue, newCost, newIds);
                }
            }
        }

        // 5. 找出全預約範圍 B 內最完美的最終狀態
        DpState best = dp[0];
        for (int j = 1; j <= B; j++) {
            if (dp[j].maxValue > best.maxValue) {
                best = dp[j];
            } else if (dp[j].maxValue == best.maxValue) {
                if (dp[j].totalCost < best.totalCost) {
                    best = dp[j];
                } else if (dp[j].totalCost == best.totalCost) {
                    if (dp[j].selectedIds.compareTo(best.selectedIds) < 0) {
                        best = dp[j];
                    }
                }
            }
        }

        // 6. 輸出結果
        System.out.println(best.maxValue + " " + best.totalCost);
        if (best.selectedIds.equals("")) {
            System.out.println("NONE");
        } else {
            System.out.println(best.selectedIds);
        }

        sc.close();
    }
}
//智慧教室維修排程
//Description
//
//智慧教室有許多設備需要維修，但今天的維修預算有限。每個設備會依類型、年齡、使用時數、錯誤次數與是否為關鍵設備計算維修優先值。請在不超過預算的情況下，選出一批設備，使總優先值最大。
//
//若有多種選法總優先值相同，選總成本較低者；若仍相同，將選到的設備編號排序後，選字典序最小的編號序列。
//
//
//Input
//
//第一行輸入 N B，代表設備數與預算。接下來 N 行每行一個設備：
//
//id type age hours errors cost critical
//type 只會是 SENSOR、CAMERA、DOOR、SERVER，基礎值分別為 20、35、40、60
//critical 只會是 Y 或 N
//優先值 = base(type) + age * 3 + floor(hours / 100) + errors * 12 + criticalBonus
//criticalBonus：Y 為 50，N 為 0
//
//Output
//
//輸出兩行。第一行輸出最大總優先值與實際總成本。第二行輸出被選到的設備 id，依字典序由小到大排列；若沒有選任何設備，輸出 NONE。
//
//maxValue totalCost
//id1 id2 id3 ...

//Hint
//
//1 <= N <= 100
//0 <= B <= 10000
//1 <= cost <= 10000
//0 <= age <= 100
//0 <= hours <= 1000000
//0 <= errors <= 1000
//本題可用 0/1 背包動態規劃。