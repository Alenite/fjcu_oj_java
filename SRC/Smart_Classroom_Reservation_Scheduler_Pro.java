import java.util.Scanner;

// 定義父類別 Room
abstract class Room {
    String roomId;
    String type;
    int capacity;

    // 用基礎陣列記錄該教室所有已被預約的時間區間 [start, end)
    int[] startTimes;
    int[] endTimes;
    int bookingCount;

    public Room(String roomId, String type, int capacity, int maxBookings) {
        this.roomId = roomId;
        this.type = type;
        this.capacity = capacity;
        // 最多可能被預約 Q 次，直接開滿
        this.startTimes = new int[maxBookings];
        this.endTimes = new int[maxBookings];
        this.bookingCount = 0;
    }

    // 抽象方法：計算費用，由子類別各自實作
    public abstract int getCost(int duration, int people);

    // 檢查新請求 [start, end) 是否與這間教室現有的預約衝突
    public boolean isConflict(int start, int end) {
        for (int i = 0; i < bookingCount; i++) {
            int a = start;
            int b = end;
            int c = startTimes[i];
            int d = endTimes[i];

            // 提示公式：max(a, c) < min(b, d)
            int maxStart = (a > c) ? a : c;
            int minEnd = (b < d) ? b : d;

            if (maxStart < minEnd) {
                return true; // 發生衝突
            }
        }
        return false; // 沒有衝突
    }

    // 同意預約，記錄時間
    public void addBooking(int start, int end) {
        startTimes[bookingCount] = start;
        endTimes[bookingCount] = end;
        bookingCount++;
    }
}

// 子類別：普通教室
class CLASS extends Room {
    public CLASS(String roomId, String type, int capacity, int maxBookings) {
        super(roomId, type, capacity, maxBookings);
    }
    @Override
    public int getCost(int duration, int people) {
        return duration * 50 + people * 5;
    }
}

// 子類別：實驗室
class LAB extends Room {
    public LAB(String roomId, String type, int capacity, int maxBookings) {
        super(roomId, type, capacity, maxBookings);
    }
    @Override
    public int getCost(int duration, int people) {
        return duration * 80 + people * 10;
    }
}

// 子類別：大禮堂
class HALL extends Room {
    public HALL(String roomId, String type, int capacity, int maxBookings) {
        super(roomId, type, capacity, maxBookings);
    }
    @Override
    public int getCost(int duration, int people) {
        return duration * 120 + this.capacity * 2;
    }
}

public class Smart_Classroom_Reservation_Scheduler_Pro {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int R = sc.nextInt();
        int Q = sc.nextInt();

        // 建立教室陣列
        Room[] rooms = new Room[R];

        // 讀取 R 間教室
        for (int i = 0; i < R; i++) {
            String roomId = sc.next();
            String type = sc.next();
            int capacity = sc.nextInt();

            if (type.equals("CLASS")) {
                rooms[i] = new CLASS(roomId, type, capacity, Q);
            } else if (type.equals("LAB")) {
                rooms[i] = new LAB(roomId, type, capacity, Q);
            } else if (type.equals("HALL")) {
                rooms[i] = new HALL(roomId, type, capacity, Q);
            }
        }

        int totalCost = 0; // 紀錄總收入

        // 處理 Q 筆預約請求
        for (int q = 0; q < Q; q++) {
            String requestId = sc.next();
            int start = sc.nextInt();
            int end = sc.nextInt();
            int people = sc.nextInt();

            int duration = end - start;

            Room bestRoom = null;
            int minCost = Integer.MAX_VALUE;

            // 遍歷所有教室，尋找最合適的
            for (int i = 0; i < R; i++) {
                Room currentRoom = rooms[i];

                // 條件 1：容量必須足夠
                if (currentRoom.capacity < people) {
                    continue;
                }

                // 條件 2：時間不能衝突
                if (currentRoom.isConflict(start, end)) {
                    continue;
                }

                // 計算這間教室的費用
                int currentCost = currentRoom.getCost(duration, people);

                // 條件 3：挑選費用最低者；若費用相同，選 ID 字典序小者
                if (bestRoom == null) {
                    bestRoom = currentRoom;
                    minCost = currentCost;
                } else {
                    if (currentCost < minCost) {
                        bestRoom = currentRoom;
                        minCost = currentCost;
                    } else if (currentCost == minCost) {
                        // 費用相同，比較字典序
                        if (currentRoom.roomId.compareTo(bestRoom.roomId) < 0) {
                            bestRoom = currentRoom;
                            minCost = currentCost;
                        }
                    }
                }
            }

            // 輸出此筆請求的結果
            if (bestRoom != null) {
                System.out.println(requestId + " " + bestRoom.roomId + " " + minCost);
                // 正式將時間塞入該教室的排程
                bestRoom.addBooking(start, end);
                totalCost += minCost;
            } else {
                System.out.println(requestId + " REJECTED");
            }
        }

        // 最後輸出總費用
        System.out.println("TOTAL " + totalCost);

        sc.close();
    }
}
//智慧教室預約排程器 Pro
//Description
//
//學校有多間教室，每種教室有不同租借費用公式。預約請求會依照輸入順序處理，每筆請求必須選擇容量足夠且時間不衝突的教室。
//
//若有多間可用教室，選擇費用最低者；若費用相同，選擇教室編號字典序最小者。若無法安排，輸出 REJECTED。
//
//本題建議建立 Room 父類別與 CLASS、LAB、HALL 子類別覆寫 getCost 方法，並練習區間衝突判斷。
//
//
//Input
//
//第一行輸入 R Q。接下來 R 行：
//
//roomId type capacity
//接下來 Q 行：
//
//requestId start end people
//區間為 [start, end)。
//
//
//Output
//
//每筆請求輸出：
//
//requestId roomId cost
//或
//
//requestId REJECTED
//最後輸出：
//
//TOTAL totalCost

//Hint
//
//CLASS cost = duration*50 + people*5
//LAB cost = duration*80 + people*10
//HALL cost = duration*120 + capacity*2
//兩個區間 [a,b), [c,d) 若 max(a,c) < min(b,d) 則衝突。