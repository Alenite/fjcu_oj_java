import java.util.Scanner;

// 定義包裹父類別
abstract class Package {
    int weight;
    int distance;
    int fragile;
    int express;

    public Package(int weight, int distance, int fragile, int express) {
        this.weight = weight;
        this.distance = distance;
        this.fragile = fragile;
        this.express = express;
    }

    // 抽象方法：計算基礎運費
    public abstract int getBasePrice();

    // 計算打折後的最終運費（包含折扣門檻判定）
    public int getFinalPrice() {
        int base = getBasePrice();
        if (base >= 1000) {
            return (int) (base * 85 / 100); // 打 85 折，使用整數除法自動無條件捨去
        } else if (base >= 500) {
            return (int) (base * 90 / 100); // 打 9 折
        }
        return base;
    }
}

// 子類別：常溫包裹
class NORMAL extends Package {
    public NORMAL(int weight, int distance, int fragile, int express) {
        super(weight, distance, fragile, express);
    }
    @Override
    public int getBasePrice() {
        return this.weight * 10 + this.distance * 2 + this.fragile * 50 + this.express * 100;
    }
}

// 子類別：冷鏈包裹
class COLD extends Package {
    public COLD(int weight, int distance, int fragile, int express) {
        super(weight, distance, fragile, express);
    }
    @Override
    public int getBasePrice() {
        return this.weight * 15 + this.distance * 3 + this.fragile * 80 + this.express * 120;
    }
}

// 子類別：國際包裹
class INTERNATIONAL extends Package {
    public INTERNATIONAL(int weight, int distance, int fragile, int express) {
        super(weight, distance, fragile, express);
    }
    @Override
    public int getBasePrice() {
        return this.weight * 20 + this.distance * 5 + this.fragile * 100 + this.express * 200;
    }
}

// 用來儲存每個收件人彙整結果的類別
class ReceiverSummary {
    String name;
    int totalCost;
    int count;

    public ReceiverSummary(String name) {
        this.name = name;
        this.totalCost = 0;
        this.count = 0;
    }

    // 根據總運費取得會員等級
    public String getLevel() {
        if (this.totalCost >= 2000) return "GOLD";
        if (this.totalCost >= 1000) return "SILVER";
        return "BRONZE";
    }
}

public class express_delivery_member_settlement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int N = sc.nextInt();

        // 用來儲存所有不重複的收件人資料，最多不超過 N 個
        ReceiverSummary[] summaries = new ReceiverSummary[N];
        int uniqueCount = 0; // 記錄目前抓到幾位不重複的收件人

        // 處理 N 筆包裹資料
        for (int i = 0; i < N; i++) {
            String receiver = sc.next();
            String type = sc.next();
            int weight = sc.nextInt();
            int distance = sc.nextInt();
            int fragile = sc.nextInt();
            int express = sc.nextInt();

            // 1. 多型建立包裹物件並計算費用
            Package p = null;
            if (type.equals("NORMAL")) {
                p = new NORMAL(weight, distance, fragile, express);
            } else if (type.equals("COLD")) {
                p = new COLD(weight, distance, fragile, express);
            } else if (type.equals("INTERNATIONAL")) {
                p = new INTERNATIONAL(weight, distance, fragile, express);
            }

            int finalPrice = (p != null) ? p.getFinalPrice() : 0;

            // 2. 彙整：檢查這個收件人是否已經在 summaries 中
            ReceiverSummary targetSummary = null;
            for (int j = 0; j < uniqueCount; j++) {
                if (summaries[j].name.equals(receiver)) {
                    targetSummary = summaries[j];
                    break;
                }
            }

            // 如果是新面孔，就建立新資料並塞入陣列
            if (targetSummary == null) {
                targetSummary = new ReceiverSummary(receiver);
                summaries[uniqueCount] = targetSummary;
                uniqueCount++;
            }

            // 累加運費與包裹數
            targetSummary.totalCost += finalPrice;
            targetSummary.count++;
        }

        // 3. 氣泡排序法排序
        for (int i = 0; i < uniqueCount - 1; i++) {
            for (int j = 0; j < uniqueCount - 1 - i; j++) {
                ReceiverSummary r1 = summaries[j];
                ReceiverSummary r2 = summaries[j + 1];

                boolean needSwap = false;

                // 規則一：總運費由高到低
                if (r1.totalCost < r2.totalCost) {
                    needSwap = true;
                } else if (r1.totalCost == r2.totalCost) {
                    // 規則二：包裹數由多到少
                    if (r1.count < r2.count) {
                        needSwap = true;
                    } else if (r1.count == r2.count) {
                        // 規則三：收件人名稱字典序由小到大
                        if (r1.name.compareTo(r2.name) > 0) {
                            needSwap = true;
                        }
                    }
                }

                if (needSwap) {
                    ReceiverSummary temp = summaries[j];
                    summaries[j] = summaries[j + 1];
                    summaries[j + 1] = temp;
                }
            }
        }

        // 4. 依序輸出結果
        for (int i = 0; i < uniqueCount; i++) {
            ReceiverSummary r = summaries[i];
            System.out.println(r.name + " " + r.totalCost + " " + r.count + " " + r.getLevel());
        }

        sc.close();
    }
}
//多型快遞會員結算
//Description
//
//快遞公司有不同包裹類型，不同類型有不同基礎運費公式。每筆包裹運費計算後，若達到折扣門檻需要套用折扣。
//
//請依收件人彙整總運費與包裹數，並輸出會員等級。排序規則為總運費高到低、包裹數多到少、收件人名稱字典序小到大。
//
//本題建議建立 Package 父類別與 NORMAL、COLD、INTERNATIONAL 子類別覆寫 getPrice 方法。
//
//
//Input
//
//第一行輸入 N。接下來 N 行：
//
//receiver type weight distance fragile express
//
//Output
//
//依排序輸出：
//
//receiver total count level
//total >= 2000 為 GOLD，total >= 1000 為 SILVER，否則 BRONZE。

//Hint
//
//NORMAL: weight*10 + distance*2 + fragile*50 + express*100
//COLD: weight*15 + distance*3 + fragile*80 + express*120
//INTERNATIONAL: weight*20 + distance*5 + fragile*100 + express*200
//price >= 1000 打 85 折；否則 price >= 500 打 9 折；皆使用整數除法無條件捨去。