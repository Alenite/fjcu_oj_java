import java.util.Scanner;

// 定義卡片父類別
abstract class Card {
    String cardId;
    String type;
    int balance;   // 剩餘餘額
    int spent;     // 累積扣款
    int success;   // 成功次數
    int failed;    // 失敗次數

    public Card(String cardId, String type, int balance) {
        this.cardId = cardId;
        this.type = type;
        this.balance = balance;
        this.spent = 0;
        this.success = 0;
        this.failed = 0;
    }

    // 抽象方法：由子類別實作各自的票價公式
    public abstract int calculateFare(int distance, int zone, int peak);
}

// 子類別：普通全票
class ADULT extends Card {
    public ADULT(String cardId, String type, int balance) {
        super(cardId, type, balance);
    }
    @Override
    public int calculateFare(int distance, int zone, int peak) {
        return distance * 10 + zone * 25 + peak * 40;
    }
}

// 子類別：學生票
class STUDENT extends Card {
    public STUDENT(String cardId, String type, int balance) {
        super(cardId, type, balance);
    }
    @Override
    public int calculateFare(int distance, int zone, int peak) {
        // 利用整數除法達到 floor 效果
        int base = (distance * 10 + zone * 25) * 70 / 100;
        return base + peak * 20;
    }
}

// 子類別：敬老票
class ELDER extends Card {
    public ELDER(String cardId, String type, int balance) {
        super(cardId, type, balance);
    }
    @Override
    public int calculateFare(int distance, int zone, int peak) {
        return (distance * 10 + zone * 25) * 50 / 100;
    }
}

// 子類別：VIP 貴賓卡
class VIP extends Card {
    public VIP(String cardId, String type, int balance) {
        super(cardId, type, balance);
    }
    @Override
    public int calculateFare(int distance, int zone, int peak) {
        return (distance * 10 + zone * 25 + peak * 40) * 80 / 100;
    }
}

public class Campus_bus_deduction_ranking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int P = sc.nextInt();
        int T = sc.nextInt();

        // 建立一般陣列儲存 P 張卡片
        Card[] cards = new Card[P];

        // 1. 讀取卡片初始資料
        for (int i = 0; i < P; i++) {
            String cardId = sc.next();
            String type = sc.next();
            int balance = sc.nextInt();

            if (type.equals("ADULT")) {
                cards[i] = new ADULT(cardId, type, balance);
            } else if (type.equals("STUDENT")) {
                cards[i] = new STUDENT(cardId, type, balance);
            } else if (type.equals("ELDER")) {
                cards[i] = new ELDER(cardId, type, balance);
            } else if (type.equals("VIP")) {
                cards[i] = new VIP(cardId, type, balance);
            }
        }

        // 2. 模擬 T 筆搭乘交易
        for (int i = 0; i < T; i++) {
            String cardId = sc.next();
            int distance = sc.nextInt();
            int zone = sc.nextInt();
            int peak = sc.nextInt();

            // 尋找對應的卡片
            Card targetCard = null;
            for (int j = 0; j < P; j++) {
                if (cards[j].cardId.equals(cardId)) {
                    targetCard = cards[j];
                    break;
                }
            }

            if (targetCard == null) continue;

            // 計算這次的票價
            int fare = targetCard.calculateFare(distance, zone, peak);

            // 扣款邏輯檢查
            if (targetCard.balance >= fare) {
                targetCard.balance -= fare;
                targetCard.spent += fare;
                targetCard.success++;
            } else {
                targetCard.failed++;
            }
        }

        // 3. 基礎氣泡排序法（依題目多重條件排序）
        for (int i = 0; i < P - 1; i++) {
            for (int j = 0; j < P - 1 - i; j++) {
                Card c1 = cards[j];
                Card c2 = cards[j + 1];

                boolean needSwap = false;

                // 條件一：累積扣款金額由高到低
                if (c1.spent < c2.spent) {
                    needSwap = true;
                } else if (c1.spent == c2.spent) {
                    // 條件二：剩餘餘額較少者優先（由低到高）
                    if (c1.balance > c2.balance) {
                        needSwap = true;
                    } else if (c1.balance == c2.balance) {
                        // 條件三：票卡編號字典序較小者優先（由小到大）
                        if (c1.cardId.compareTo(c2.cardId) > 0) {
                            needSwap = true;
                        }
                    }
                }

                if (needSwap) {
                    Card temp = cards[j];
                    cards[j] = cards[j + 1];
                    cards[j + 1] = temp;
                }
            }
        }

        // 4. 依序輸出排行榜結果
        for (int i = 0; i < P; i++) {
            System.out.println(cards[i].cardId + " " + cards[i].spent + " " +
                    cards[i].balance + " " + cards[i].success + " " + cards[i].failed);
        }

        sc.close();
    }
}
//校園巴士扣款排行榜
//Description
//
//校園巴士系統有多種票卡。不同票卡型態有不同票價公式。請模擬每一筆搭乘紀錄，餘額足夠才會扣款，否則該筆搭乘失敗。
//
//最後依照累積扣款金額由高到低輸出所有票卡；若相同，剩餘餘額較少者優先；若仍相同，票卡編號字典序較小者優先。
//
//本題建議建立 Card 父類別與 ADULT、STUDENT、ELDER、VIP 子類別覆寫 calculateFare 方法。
//
//
//Input
//
//第一行輸入 P T。接下來 P 行：
//
//cardId type balance
//接下來 T 行：
//
//cardId distance zone peak
//
//Output
//
//依排序輸出每張卡：
//
//cardId spent balance success failed

//Hint
//
//ADULT: distance*10 + zone*25 + peak*40
//STUDENT: floor((distance*10+zone*25)*70/100) + peak*20
//ELDER: floor((distance*10+zone*25)*50/100)
//VIP: floor((distance*10+zone*25+peak*40)*80/100)