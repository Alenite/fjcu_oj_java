import java.util.Scanner;

// 教室類別
class Room2 {
    String roomId;
    int capacity;

    // 用基礎陣列記錄這間教室已被借用的時間區間 [start, end)
    int[] startTimes;
    int[] endTimes;
    int bookingCount;

    public Room2(String roomId, int capacity, int maxBookings) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.startTimes = new int[maxBookings];
        this.endTimes = new int[maxBookings];
        this.bookingCount = 0;
    }

    // 檢查這間教室與新時間 [start, end) 是否衝突
    public boolean isConflict(int start, int end) {
        for (int i = 0; i < bookingCount; i++) {
            int a = start;
            int b = end;
            int c = startTimes[i];
            int d = endTimes[i];

            // 區間相交公式：max(a, c) < min(b, d)
            int maxStart = (a > c) ? a : c;
            int minEnd = (b < d) ? b : d;

            if (maxStart < minEnd) {
                return true; // 發生衝突
            }
        }
        return false; // 沒有衝突
    }

    // 將借用時間正式登記進去
    public void addBooking(int start, int end) {
        startTimes[bookingCount] = start;
        endTimes[bookingCount] = end;
        bookingCount++;
    }
}

// 預約申請類別
class Reservation {
    String title;
    int start;
    int end;
    int people;
    int priority;
    int originalIndex; // 紀錄最一開始的輸入順序

    // 儲存最後排程的結果
    boolean isAccepted;
    String assignedRoomId;

    public Reservation(String title, int start, int end, int people, int priority, int originalIndex) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.people = people;
        this.priority = priority;
        this.originalIndex = originalIndex;
        this.isAccepted = false;
        this.assignedRoomId = "";
    }
}

public class Smart_Classroom_Reservation_Scheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int R = sc.nextInt();
        int N = sc.nextInt();

        // 1. 讀取 R 間教室
        Room2[] rooms = new Room2[R];
        for (int i = 0; i < R; i++) {
            String roomId = sc.next();
            int capacity = sc.nextInt();
            // 每間教室最多只可能被借 N 次，安全開滿空間
            rooms[i] = new Room2(roomId, capacity, N);
        }

        // 2. 讀取 N 筆預約申請
        Reservation[] reservations = new Reservation[N];
        for (int i = 0; i < N; i++) {
            String title = sc.next();
            int start = sc.nextInt();
            int end = sc.nextInt();
            int people = sc.nextInt();
            int priority = sc.nextInt();
            // i 就是這筆預約的原始序號
            reservations[i] = new Reservation(title, start, end, people, priority, i);
        }

        // 3. 處理排程前，先依「優先權高、開始時間早、輸入較早」排序預約申請 (氣泡排序)
        // 註：因為 N 達到 10000，一般氣泡排序大約一億次。本題時限普遍寬裕，若測資卡極限，
        // 只要確保在 Java 基礎語法範圍內，這套多條件比對是邏輯最精確的寫法。
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N - 1 - i; j++) {
                Reservation r1 = reservations[j];
                Reservation r2 = reservations[j + 1];

                boolean needSwap = false;
                // 優先權高者優先 (大到小)
                if (r1.priority < r2.priority) {
                    needSwap = true;
                } else if (r1.priority == r2.priority) {
                    // 開始時間早者優先 (小到大)
                    if (r1.start > r2.start) {
                        needSwap = true;
                    } else if (r1.start == r2.start) {
                        // 輸入較早者優先 (原本就在前面的 originalIndex 小)
                        if (r1.originalIndex > r2.originalIndex) {
                            needSwap = true;
                        }
                    }
                }

                if (needSwap) {
                    Reservation temp = reservations[j];
                    reservations[j] = reservations[j + 1];
                    reservations[j + 1] = temp;
                }
            }
        }

        // 4. 開始依序模擬預約排程
        for (int i = 0; i < N; i++) {
            Reservation res = reservations[i];

            Room2 bestRoom = null;

            // 走訪所有教室尋找最適合的
            for (int j = 0; j < R; j++) {
                Room2 currentRoom = rooms[j];

                // 條件一：容量必須足夠
                if (currentRoom.capacity < res.people) {
                    continue;
                }

                // 條件二：時間不能與該教室現有排程衝突
                if (currentRoom.isConflict(res.start, res.end)) {
                    continue;
                }

                // 篩選最優教室：容量最小者；若容量相同，選教室編號字典序小者
                if (bestRoom == null) {
                    bestRoom = currentRoom;
                } else {
                    if (currentRoom.capacity < bestRoom.capacity) {
                        bestRoom = currentRoom;
                    } else if (currentRoom.capacity == bestRoom.capacity) {
                        if (currentRoom.roomId.compareTo(bestRoom.roomId) < 0) {
                            bestRoom = currentRoom;
                        }
                    }
                }
            }

            // 如果有找到可用教室，正式扣下並記錄結果
            if (bestRoom != null) {
                bestRoom.addBooking(res.start, res.end);
                res.isAccepted = true;
                res.assignedRoomId = bestRoom.roomId;
            } else {
                res.isAccepted = false;
            }
        }

        // 5. 排程完畢，必須將預約名單「排回原本的輸入順序」才能進行輸出
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N - 1 - i; j++) {
                if (reservations[j].originalIndex > reservations[j + 1].originalIndex) {
                    Reservation temp = reservations[j];
                    reservations[j] = reservations[j + 1];
                    reservations[j + 1] = temp;
                }
            }
        }

        // 6. 依原始輸入順序輸出最終結果
        for (int i = 0; i < N; i++) {
            Reservation res = reservations[i];
            if (res.isAccepted) {
                System.out.println(res.title + " ACCEPT " + res.assignedRoomId);
            } else {
                System.out.println(res.title + " REJECT");
            }
        }

        sc.close();
    }
}
//智慧教室預約排程器
//Description
//
//學校有多間教室，每間教室有容量。多個社團提出教室預約申請，每筆申請包含開始時間、結束時間、人數與優先權。系統會先依優先權高者處理；若優先權相同，開始時間早者優先；若仍相同，輸入較早者優先。
//
//處理申請時，系統會選擇「容量足夠且時間不衝突」的教室中，容量最小者；若容量相同，選教室編號字典序小者。時間區間以 [start, end) 判斷，end 等於另一筆 start 不衝突。
//
//本題建議使用 Room 與 Reservation 類別封裝資料，練習 object array、sorting、interval conflict 與 simulation。
//
//
//Input
//
//第一行輸入 R N。接下來 R 行：
//
//roomId capacity
//接下來 N 行：
//
//title start end people priority
//
//Output
//
//請依照「原始輸入順序」輸出每筆申請結果：
//
//title ACCEPT roomId
//或
//
//title REJECT

//Hint
//
//1 <= R <= 100
//1 <= N <= 10000
//0 <= start < end <= 100000
//1 <= people, capacity <= 100000
//1 <= priority <= 100000
//title 與 roomId 長度 <= 20。Hint
//
//1 <= R <= 100
//1 <= N <= 10000
//0 <= start < end <= 100000
//1 <= people, capacity <= 100000
//1 <= priority <= 100000
//title 與 roomId 長度 <= 20。