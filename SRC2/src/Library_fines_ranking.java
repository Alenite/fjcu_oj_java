import java.util.*;

// 宣告一個 Record 類別，用來封裝單筆借閱紀錄的所有資訊與計算邏輯
class Record {
    String name;          // 讀者姓名
    String bookId;        // 書籍編號
    String type;          // 書籍類型 (TEXT, REF, MEDIA, RARE)
    int dueDay, returnDay, renewCount; // 應還日期、實際歸還日期、續借次數
    boolean isVip;        // 是否為 VIP 讀者

    int lateDays;         // 實際有效逾期天數
    int fine;             // 最終罰款金額
    String status;        // 讀者狀態 (CLEAR, WARNING, BLOCK)

    // 建構子：初始化借閱紀錄的各項基礎資料，並在建立物件時直接呼叫計算方法
    public Record(String name, String bookId, String type, int dueDay, int returnDay, int renewCount, String vipStr) {
        this.name = name;
        this.bookId = bookId;
        this.type = type;
        this.dueDay = dueDay;
        this.returnDay = returnDay;
        this.renewCount = renewCount;
        this.isVip = vipStr.equals("YES"); // 若字串為 "YES" 則設定 isVip 為 true，否則為 false
        calculate(); // 呼叫核心計算方法
    }

    // 核心計算方法：用來計算有效逾期天數、罰款以及決定最終狀態
    private void calculate() {
        int grace = 0; // 寬限天數
        int rate = 0;  // 每日罰款金額

        // 根據書籍類型 (type)，透過 switch 分支指派對應的寬限天數與每日罰款率
        switch (type) {
            case "TEXT":  grace = 3; rate = 2;  break;
            case "REF":   grace = 0; rate = 5;  break;
            case "MEDIA": grace = 1; rate = 3;  break;
            case "RARE":  grace = 0; rate = 10; break;
        }

        // 計算有效逾期天數，公式：實際歸還日 - 應還日 - 寬限天數 - (續借次數 * 7)。若算出來為負數，則與 0 取最大值 (即天數為 0)
        this.lateDays = Math.max(0, returnDay - dueDay - grace - (renewCount * 7));

        // 計算基礎罰款：有效逾期天數 * 每日罰款率
        int baseFine = this.lateDays * rate;
        // 判斷是否為 VIP：若是 VIP 則罰款打 8 折 (乘以 0.8 並強制轉型為 int 捨去小數)，若不是則維持基礎罰款
        this.fine = isVip ? (int)(baseFine * 0.8) : baseFine;


        // 根據最終罰款金額決定讀者的狀態
        if (this.fine == 0) this.status = "CLEAR";             // 罰款為 0 為 CLEAR
        else if (this.fine <= 100) this.status = "WARNING";     // 罰款 1 到 100 為 WARNING
        else this.status = "BLOCK";                             // 罰款大於 100 為 BLOCK
    }
}

// 主程式入口類別
public class Library_fines_ranking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 宣告 Scanner 用於讀取標準輸入
        if (!sc.hasNextInt()) return;        // 安全防護：若第一行沒有整數則直接結束程式
        int N = sc.nextInt();                // 讀取紀錄總筆數 N
        List<Record> records = new ArrayList<>(); // 建立一個動態陣列 List 用來存放所有的 Record 物件

        // 迴圈讀取 N 行測資，每讀完一筆資料就 new 一個 Record 物件並加入 List 中
        for (int i = 0; i < N; i++) {
            records.add(new Record(sc.next(), sc.next(), sc.next(), sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.next()));
        }


        // 使用自訂比較子對 List 進行多條件排序
        records.sort((a, b) -> {
            if (a.fine != b.fine) return b.fine - a.fine; // 條件一：罰款金額由高到低排序 (b - a)
            if (a.lateDays != b.lateDays) return b.lateDays - a.lateDays; // 條件二：罰款相同時，有效逾期天數多者優先 (b - a)
            if (!a.name.equals(b.name)) return a.name.compareTo(b.name); // 條件三：天數也相同時，依姓名字典序由小到大排序 (a 到 b)
            return a.bookId.compareTo(b.bookId); // 條件四：姓名也相同時，依書籍編號字典序由小到大排序 (a 到 b)
        });


        // 使用 for-each 迴圈遍歷排序後的 List，並依照指定格式格式化輸出結果
        for (Record r : records) {
            System.out.printf("%s %s %d %d %s\n", r.name, r.bookId, r.lateDays, r.fine, r.status);
        }
    }
}
// 圖書館罰款排行榜
//        Description
//
//        圖書館將借閱資料整理成多筆物件紀錄。不同書籍類型有不同寬限天數與每日罰款，續借可以減少逾期天數，VIP 讀者可折扣罰款。請計算每筆紀錄的有效逾期天數、罰款與狀態，並輸出罰款排行榜。
//
//
//        Input
//
//        第一行輸入 N。接下來 N 行每行一筆借閱紀錄。
//
//        name bookId type dueDay returnDay renewCount vip
//        type 只會是 TEXT、REF、MEDIA、RARE
//        vip 只會是 YES 或 NO
//        有效逾期天數 = max(0, returnDay - dueDay - grace(type) - renewCount * 7)
//        每日罰款：TEXT=2，REF=5，MEDIA=3，RARE=10
//        寬限天數：TEXT=3，REF=0，MEDIA=1，RARE=0
//        VIP 罰款為原罰款的 80%，小數直接捨去
//
//        Output
//
//        依罰款由高到低排序；罰款相同時有效逾期天數多者優先，再依姓名字典序、書籍編號字典序排序。每行輸出：
//
//        name bookId lateDays fine status
//        status 規則：fine = 0 為 CLEAR，1 到 100 為 WARNING，大於 100 為 BLOCK。
//

//Hint
//
//1 <= N <= 100000
//1 <= dueDay, returnDay <= 1000000
//0 <= renewCount <= 10
//name 與 bookId 長度 <= 20
//建議使用建構子初始化借閱紀錄，並用方法計算逾期與罰款。
