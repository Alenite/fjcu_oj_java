import java.util.Scanner;

// 1. 定義書籍父類別與子類別（多型）
abstract class Book {
    String bookId;
    String type;
    int value;

    // 借閱狀態維護
    boolean isBorrowed;
    String currentBorrower;
    int borrowTime;

    public Book(String bookId, String type, int value) {
        this.bookId = bookId;
        this.type = type;
        this.value = value;
        this.isBorrowed = false;
        this.currentBorrower = "";
        this.borrowTime = 0;
    }

    // 抽象方法：取得可借天數
    public abstract int getAllowedDays();
    // 抽象方法：取得每逾期一天的罰款
    public abstract int getPenaltyPerDay();

    // 計算逾期罰款
    public int calculatePenalty(int returnTime) {
        int duration = returnTime - this.borrowTime;
        int allowed = getAllowedDays();
        if (duration > allowed) {
            return (duration - allowed) * getPenaltyPerDay();
        }
        return 0;
    }
}

class NORMAL2 extends Book {
    public NORMAL2(String bookId, String type, int value) { super(bookId, type, value); }
    @Override public int getAllowedDays() { return 14; }
    @Override public int getPenaltyPerDay() { return 2; }
}

class RARE extends Book {
    public RARE(String bookId, String type, int value) { super(bookId, type, value); }
    @Override public int getAllowedDays() { return 7; }
    @Override public int getPenaltyPerDay() { return 5; }
}

class REFERENCE extends Book {
    public REFERENCE(String bookId, String type, int value) { super(bookId, type, value); }
    @Override public int getAllowedDays() { return 3; }
    @Override public int getPenaltyPerDay() { return 10; }
}

// 2. 定義使用者統計資料類別
class User {
    String name;
    int penalty;
    int returned;
    int holding;

    public User(String name) {
        this.name = name;
        this.penalty = 0;
        this.returned = 0;
        this.holding = 0;
    }
}

public class Library_overdue_fine_administrator {
    // 透過 ID 尋找書籍索引
    private static int findBookIndex(Book[] books, int bCount, String id) {
        for (int i = 0; i < bCount; i++) {
            if (books[i].bookId.equals(id)) return i;
        }
        return -1;
    }

    // 透過姓名尋找使用者索引
    private static int findUserIndex(User[] users, int uCount, String name) {
        for (int i = 0; i < uCount; i++) {
            if (users[i].name.equals(name)) return i;
        }
        return -1;
    }

    // 手寫高效快速排序法 (Quick Sort) —— 依罰款大到小、姓名字典序小到大排序使用者
    private static void quickSortUsers(User[] users, int low, int high) {
        if (low < high) {
            User pivot = users[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                boolean needMoveAhead = false;

                // 條件一：罰款大到小
                if (users[j].penalty > pivot.penalty) {
                    needMoveAhead = true;
                } else if (users[j].penalty == pivot.penalty) {
                    // 條件二：姓名字典序小到大
                    if (users[j].name.compareTo(pivot.name) < 0) {
                        needMoveAhead = true;
                    }
                }

                if (needMoveAhead) {
                    i++;
                    User temp = users[i];
                    users[i] = users[j];
                    users[j] = temp;
                }
            }
            User temp = users[i + 1];
            users[i + 1] = users[high];
            users[high] = temp;

            int pi = i + 1;
            quickSortUsers(users, low, pi - 1);
            quickSortUsers(users, pi + 1, high);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int B = sc.nextInt();
        int U = sc.nextInt();
        int E = sc.nextInt();

        // 讀取書籍資料
        Book[] books = new Book[B];
        for (int i = 0; i < B; i++) {
            String bookId = sc.next();
            String type = sc.next();
            int value = sc.nextInt();

            if (type.equals("NORMAL")) books[i] = new NORMAL2(bookId, type, value);
            else if (type.equals("RARE")) books[i] = new RARE(bookId, type, value);
            else if (type.equals("REFERENCE")) books[i] = new REFERENCE(bookId, type, value);
        }

        // 讀取使用者名單
        User[] users = new User[U];
        for (int i = 0; i < U; i++) {
            users[i] = new User(sc.next());
        }

        // 模擬 E 筆借還書事件
        for (int i = 0; i < E; i++) {
            int time = sc.nextInt();
            String action = sc.next();
            String userName = sc.next();
            String bookId = sc.next();

            int bIdx = findBookIndex(books, B, bookId);
            int uIdx = findUserIndex(users, U, userName);

            // 若書籍或使用者不存在，直接忽略
            if (bIdx == -1 || uIdx == -1) continue;

            Book book = books[bIdx];
            User user = users[uIdx];

            if (action.equals("BORROW")) {
                // 借書：書必須沒被借走
                if (!book.isBorrowed) {
                    book.isBorrowed = true;
                    book.currentBorrower = userName;
                    book.borrowTime = time;
                    user.holding++;
                }
            } else if (action.equals("RETURN")) {
                // 還書：書必須已被借走，且持有人必須是該使用者
                if (book.isBorrowed && book.currentBorrower.equals(userName)) {
                    // 計算並累加罰款
                    int penalty = book.calculatePenalty(time);
                    user.penalty += penalty;

                    // 更新使用者統計狀態
                    user.returned++;
                    user.holding--;

                    // 重設書籍借閱狀態
                    book.isBorrowed = false;
                    book.currentBorrower = "";
                    book.borrowTime = 0;
                }
            }
        }

        // 進行最終排行榜排序
        quickSortUsers(users, 0, U - 1);

        // 輸出結果
        for (int i = 0; i < U; i++) {
            System.out.println(users[i].name + " " + users[i].penalty + " " +
                    users[i].returned + " " + users[i].holding);
        }

        sc.close();
    }
}
//圖書館逾期罰款管理員
//Description
//
//圖書館有不同種類的書，每種書的可借天數與逾期罰款不同。系統會依時間順序收到 BORROW 與 RETURN 事件，請模擬借還書流程並統計每位使用者的罰款、成功歸還本數與目前仍持有本數。
//
//借書時若書已被借走則忽略；還書時若該使用者不是該書持有人也忽略。
//
//本題建議建立 Book 父類別與不同書籍子類別，練習 constructor、encapsulation、polymorphism 與事件模擬。
//
//
//Input
//
//第一行輸入 B U E。接下來 B 行：
//
//bookId type value
//接著一行輸入 U 個使用者姓名。接下來 E 行：
//
//time action user bookId
//action 只會是 BORROW 或 RETURN。
//
//
//Output
//
//依罰款由高到低輸出使用者統計；若罰款相同，依姓名字典序小者優先。
//
//user penalty returned holding

//Hint
//
//1 <= B,U <= 1000
//0 <= E <= 200000
//type 為 NORMAL、RARE、REFERENCE
//NORMAL 可借 14 天，每逾期 1 天罰 2；RARE 可借 7 天，每天罰 5；REFERENCE 可借 3 天，每天罰 10。
//time 為非負整數。