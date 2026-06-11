import java.util.Scanner; // 嚴格限制：只能 import Scanner

public class A_belated_gift {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        if (!scan.hasNextInt()) return;
        int people = scan.nextInt();

        // 建立 Gift 物件陣列，完全符合題目建議的物件導向練習
        Gift[] gifts = new Gift[people];

        // 1. 讀入資料並建立每個人的禮物物件
        for (int i = 0; i < people; i++) {
            String name = scan.next();
            int plannedDay = scan.nextInt();
            int actualDay = scan.nextInt();

            // 透過建構子直接將資料與計算結果存入物件中
            gifts[i] = new Gift(name, plannedDay, actualDay);
        }

        // 2. 尋找遲到最久的禮物 (預設第一個為最久)
        int mostLate = gifts[0].lateDays;
        int who = 0;

        for (int i = 1; i < people; i++) {
            // 使用嚴格小於 '<'，當天數相同時，who 不會被更新
            // 如此便能完美符合「若遲到天數相同，輸出最早出現的那一份」的規則
            if (mostLate < gifts[i].lateDays) {
                mostLate = gifts[i].lateDays;
                who = i;
            }
        }

        // 3. 輸出最終結果
        System.out.println(gifts[who].name + " " + mostLate);

        scan.close();
    }
}

// 建立 Gift 類別，封裝單份禮物的屬性與計算行為
class Gift {
    String name;
    int plannedDay;
    int actualDay;
    int lateDays; // 儲存計算後的遲到天數

    // 建構子：在建立物件的同時呼叫 method 完成天數計算
    public Gift(String name, int plannedDay, int actualDay) {
        this.name = name;
        this.plannedDay = plannedDay;
        this.actualDay = actualDay;
        this.lateDays = calculate(actualDay, plannedDay); // 呼叫物件內的方法
    }

    // 完全保留你原本設計的計算核心 method
    public int calculate(int actualDay, int plannedDay) {
        int count = actualDay - plannedDay;
        if (count <= 0) {
            return 0; // 若沒有遲到，遲到天數為 0
        } else {
            return count;
        }
    }
}
//遲來的禮物
//Description
//
//助教原本答應要準時把作業題目發給大家，但因為太忙忘記了，導致這份作業變成一份「遲來的禮物」。
//
//為了向同學們道歉，助教決定準備一些小禮物送給大家。不過，每一份禮物都有原本預計送出的日期與實際送出的日期。請你幫助助教計算每份禮物遲到了幾天，並找出遲到最久的那份禮物。
//
//如果有多份禮物遲到天數相同，請輸出最早出現在輸入中的那一份。
//
//本題建議使用Gift類別建立禮物物件，練習 class、object、attribute 與 method。
//
//
//Input
//
//第一行輸入一個整數N，代表禮物數量。
//
//接下來N行，每行包含：
//
//name plannedDay actualDay
//其中：
//
//name：收件人姓名，不含空白
//plannedDay：原本預計送出日
//actualDay：實際送出日
//日期以整數表示，例如第 10 天、第 25 天。
//
//
//Output
//
//輸出遲到最久的禮物資訊，格式如下：
//
//name lateDays
//若禮物沒有遲到，遲到天數為0

//Hint
//
//1 <= N <= 1000
//
//1 <= plannedDay <= 365
//
//1 <= actualDay <= 365
//
//name 長度 <= 20