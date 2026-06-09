import java.util.Scanner;

// 1. 定義包裹父類別
abstract class Package2 {
    String id;
    String type;
    int weight;
    int distance;
    int option;

    public Package2(String id, String type, int weight, int distance, int option) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.distance = distance;
        this.option = option;
    }

    // 抽象方法：由各服務類型實作其基本公式
    public abstract int getBaseFee();

    // 最終計價方法（包含基本費用與共通的超重、遠距離加價）
    public int getFee() {
        int fee = getBaseFee();

        // 超重加價規則
        if (this.weight > 20) {
            fee += (this.weight - 20) * 25;
        }

        // 遠距離加價規則
        if (this.distance > 300) {
            fee += (this.distance - 300) * 3;
        }

        return fee;
    }

    // 取得收費級別
    public String getCategory() {
        int fee = getFee();
        if (fee >= 1000) return "VIP";
        if (fee >= 500) return "NORMAL";
        return "LOW";
    }
}

// 子類別：標準包裹
class STANDARD extends Package2 {
    public STANDARD(String id, String type, int weight, int distance, int option) {
        super(id, type, weight, distance, option);
    }
    @Override
    public int getBaseFee() {
        return 50 + this.weight * 10 + this.distance * 2;
    }
}

// 子類別：易碎包裹
class FRAGILE extends Package2 {
    public FRAGILE(String id, String type, int weight, int distance, int option) {
        super(id, type, weight, distance, option);
    }
    @Override
    public int getBaseFee() {
        return 80 + this.weight * 12 + this.distance * 2 + this.option * 30;
    }
}

// 子類別：冷鏈包裹
class COLD2 extends Package2 {
    public COLD2(String id, String type, int weight, int distance, int option) {
        super(id, type, weight, distance, option);
    }
    @Override
    public int getBaseFee() {
        return 100 + this.weight * 15 + this.distance * 3 + this.option * 20;
    }
}

// 子類別：快捷包裹
class EXPRESS extends Package2 {
    public EXPRESS(String id, String type, int weight, int distance, int option) {
        super(id, type, weight, distance, option);
    }
    @Override
    public int getBaseFee() {
        return 120 + this.weight * 14 + this.distance * 4 + this.option * 50;
    }
}

public class Multi_type_express_delivery_quotation_system {
    // 手寫高效快速排序法 (Quick Sort) —— 依費用大到小、ID字典序小到大排序
    private static void quickSortPackages(Package2[] pkgs, int low, int high) {
        if (low < high) {
            Package2 pivot = pkgs[high];
            int pivotFee = pivot.getFee();
            int i = low - 1;

            for (int j = low; j < high; j++) {
                boolean needMoveAhead = false;
                int currentFee = pkgs[j].getFee();

                // 條件一：費用大到小
                if (currentFee > pivotFee) {
                    needMoveAhead = true;
                } else if (currentFee == pivotFee) {
                    // 條件二：費用相同，ID 字典序小到大
                    if (pkgs[j].id.compareTo(pivot.id) < 0) {
                        needMoveAhead = true;
                    }
                }

                if (needMoveAhead) {
                    i++;
                    Package2 temp = pkgs[i];
                    pkgs[i] = pkgs[j];
                    pkgs[j] = temp;
                }
            }
            Package2 temp = pkgs[i + 1];
            pkgs[i + 1] = pkgs[high];
            pkgs[high] = temp;

            int pi = i + 1;
            quickSortPackages(pkgs, low, pi - 1);
            quickSortPackages(pkgs, pi + 1, high);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int N = sc.nextInt();
        Package2[] pkgs = new Package2[N];

        // 1. 讀取 N 筆包裹資料
        for (int i = 0; i < N; i++) {
            String id = sc.next();
            String type = sc.next();
            int weight = sc.nextInt();
            int distance = sc.nextInt();
            int option = sc.nextInt();

            if (type.equals("STANDARD")) {
                pkgs[i] = new STANDARD(id, type, weight, distance, option);
            } else if (type.equals("FRAGILE")) {
                pkgs[i] = new FRAGILE(id, type, weight, distance, option);
            } else if (type.equals("COLD")) {
                pkgs[i] = new COLD2(id, type, weight, distance, option);
            } else if (type.equals("EXPRESS")) {
                pkgs[i] = new EXPRESS(id, type, weight, distance, option);
            }
        }

        // 2. 進行最終排行榜排序 (Quick Sort 複雜度 O(N log N))
        quickSortPackages(pkgs, 0, N - 1);

        // 3. 輸出排序後的結果
        for (int i = 0; i < N; i++) {
            System.out.println(pkgs[i].id + " " + pkgs[i].type + " " + pkgs[i].getFee() + " " + pkgs[i].getCategory());
        }

        sc.close();
    }
}
//多型快遞報價系統
//Description
//
//快遞公司支援多種包裹服務。不同服務類型有不同報價公式，且超重與遠距離需要額外加價。請根據包裹資料計算每件包裹費用，並依費用由高到低輸出。
//
//本題建議建立 Package 父類別，並以 STANDARD、FRAGILE、COLD、EXPRESS 子類別覆寫計價方法，練習 inheritance、polymorphism、method overriding 與 sorting。
//
//
//Input
//
//第一行輸入 N。接下來 N 行：
//
//id type weight distance option
//type 為 STANDARD、FRAGILE、COLD、EXPRESS。option 對 STANDARD 可為 0；其他類型代表附加服務參數。
//
//
//Output
//
//依費用由高到低輸出。若費用相同，依 id 字典序小者優先。
//
//id type fee category
//category：fee >= 1000 為 VIP，fee >= 500 為 NORMAL，否則 LOW。

//Hint
//
//1 <= N <= 100000
//1 <= weight <= 1000
//1 <= distance <= 100000
//0 <= option <= 100
//基本公式：STANDARD=50+w*10+d*2；FRAGILE=80+w*12+d*2+option*30；COLD=100+w*15+d*3+option*20；EXPRESS=120+w*14+d*4+option*50。若 weight>20，額外加 (weight-20)*25；若 distance>300，額外加 (distance-300)*3。