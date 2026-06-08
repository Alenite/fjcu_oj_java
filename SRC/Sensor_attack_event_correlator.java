import java.util.Scanner;

// 1. 定義感測器裝置父類別與子類別（多型）
abstract class Device {
    String deviceId;
    String type;
    int baseRisk;

    public Device(String deviceId, String type, int baseRisk) {
        this.deviceId = deviceId;
        this.type = type;
        this.baseRisk = baseRisk;
    }

    // 抽象方法：根據事件種類與數值，計算出該事件的分數
    public abstract int getEventScore(String kind, int value);
}

class CAMERA extends Device {
    public CAMERA(String deviceId, String type, int baseRisk) { super(deviceId, type, baseRisk); }
    @Override
    public int getEventScore(String kind, int value) {
        if (kind.equals("MOTION")) return this.baseRisk + value * 2;
        if (kind.equals("TAMPER")) return this.baseRisk + 100;
        return 0;
    }
}

class TEMP extends Device {
    public TEMP(String deviceId, String type, int baseRisk) { super(deviceId, type, baseRisk); }
    @Override
    public int getEventScore(String kind, int value) {
        if (kind.equals("HIGH")) return this.baseRisk + value * 3;
        if (kind.equals("LOW")) return this.baseRisk + value;
        return 0;
    }
}

class DOOR extends Device {
    public DOOR(String deviceId, String type, int baseRisk) { super(deviceId, type, baseRisk); }
    @Override
    public int getEventScore(String kind, int value) {
        if (kind.equals("OPEN")) return this.baseRisk + value * 5;
        if (kind.equals("FORCED")) return this.baseRisk + 120;
        return 0;
    }
}

class POWER extends Device {
    public POWER(String deviceId, String type, int baseRisk) { super(deviceId, type, baseRisk); }
    @Override
    public int getEventScore(String kind, int value) {
        if (kind.equals("SPIKE")) return this.baseRisk + value * 4;
        if (kind.equals("OUTAGE")) return this.baseRisk + 80;
        return 0;
    }
}

// 2. 定義事件物件類別
class Event {
    int time;
    String deviceId;
    String kind;
    int value;

    public Event(int time, String deviceId, String kind, int value) {
        this.time = time;
        this.deviceId = deviceId;
        this.kind = kind;
        this.value = value;
    }
}

// 3. 定義事件鏈結構
class Chain {
    String deviceId;
    int startTime;
    int endTime;
    int score;

    public Chain(String deviceId, int startTime, int endTime, int score) {
        this.deviceId = deviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
    }

    public String getLevel() {
        if (this.score >= 300) return "CRITICAL";
        if (this.score >= 150) return "HIGH";
        return "LOW";
    }
}

public class Sensor_attack_event_correlator {
    // 尋找裝置在陣列中的索引位置
    private static int findDeviceIndex(Device[] devices, int dCount, String id) {
        for (int i = 0; i < dCount; i++) {
            if (devices[i].deviceId.equals(id)) return i;
        }
        return -1;
    }

    // 手寫高效快速排序法 (Quick Sort) —— 依時間從小到大排序事件
    private static void quickSortEvents(Event[] events, int low, int high) {
        if (low < high) {
            int pivot = events[high].time;
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (events[j].time <= pivot) {
                    i++;
                    Event temp = events[i];
                    events[i] = events[j];
                    events[j] = temp;
                }
            }
            Event temp = events[i + 1];
            events[i + 1] = events[high];
            events[high] = temp;

            int pi = i + 1;
            quickSortEvents(events, low, pi - 1);
            quickSortEvents(events, pi + 1, high);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int D = sc.nextInt();
        int E = sc.nextInt();
        int W = sc.nextInt();

        Device[] devices = new Device[D];
        for (int i = 0; i < D; i++) {
            String deviceId = sc.next();
            String type = sc.next();
            int baseRisk = sc.nextInt();

            if (type.equals("CAMERA")) devices[i] = new CAMERA(deviceId, type, baseRisk);
            else if (type.equals("TEMP")) devices[i] = new TEMP(deviceId, type, baseRisk);
            else if (type.equals("DOOR")) devices[i] = new DOOR(deviceId, type, baseRisk);
            else if (type.equals("POWER")) devices[i] = new POWER(deviceId, type, baseRisk);
        }

        Event[] events = new Event[E];
        for (int i = 0; i < E; i++) {
            int time = sc.nextInt();
            String deviceId = sc.next();
            String kind = sc.next();
            int value = sc.nextInt();
            events[i] = new Event(time, deviceId, kind, value);
        }

        // 步驟一：對所有事件進行快速排序 (時間從小到大)
        quickSortEvents(events, 0, E - 1);

        // 步驟二：串接與關聯事件鏈
        // 為了記錄每台裝置目前「正在串接中」的事件鏈，我們開陣列來追蹤
        int[] lastEventTime = new int[D];
        int[] chainStartTime = new int[D];
        int[] chainScore = new int[D];
        boolean[] hasActiveChain = new boolean[D];

        Chain bestChain = null;

        for (int i = 0; i < E; i++) {
            Event ev = events[i];
            int dIdx = findDeviceIndex(devices, D, ev.deviceId);
            if (dIdx == -1) continue;

            int currentEventScore = devices[dIdx].getEventScore(ev.kind, ev.value);

            if (!hasActiveChain[dIdx]) {
                // 該裝置目前沒有開起的事件鏈，直接建立新鏈
                chainStartTime[dIdx] = ev.time;
                lastEventTime[dIdx] = ev.time;
                chainScore[dIdx] = currentEventScore;
                hasActiveChain[dIdx] = true;
            } else {
                // 檢查是否超時 W
                if (ev.time - lastEventTime[dIdx] <= W) {
                    // 沒超時，併入現有事件鏈
                    chainScore[dIdx] += currentEventScore;
                    lastEventTime[dIdx] = ev.time;
                } else {
                    // 超時了！先結算舊的鏈，看看有沒有打破紀錄
                    Chain oldChain = new Chain(devices[dIdx].deviceId, chainStartTime[dIdx], lastEventTime[dIdx], chainScore[dIdx]);
                    bestChain = updateBestChain(bestChain, oldChain);

                    // 開啟新的事件鏈
                    chainStartTime[dIdx] = ev.time;
                    lastEventTime[dIdx] = ev.time;
                    chainScore[dIdx] = currentEventScore;
                }
            }
        }

        // 所有的事件都跑完了，別忘了把每台裝置「手頭上最後那一條還沒結算」的鏈抓出來比對！
        for (int i = 0; i < D; i++) {
            if (hasActiveChain[i]) {
                Chain finalChain = new Chain(devices[i].deviceId, chainStartTime[i], lastEventTime[i], chainScore[i]);
                bestChain = updateBestChain(bestChain, finalChain);
            }
        }

        // 步驟三：輸出最高分的事件鏈
        if (bestChain != null) {
            System.out.println(bestChain.deviceId + " " + bestChain.startTime + " " +
                    bestChain.endTime + " " + bestChain.score + " " + bestChain.getLevel());
        }

        sc.close();
    }

    // 輔助方法：進行多條件規格比較，挑出最棒的事件鏈
    private static Chain updateBestChain(Chain currentBest, Chain newChain) {
        if (currentBest == null) return newChain;

        // 條件一：總分最高者優先
        if (newChain.score > currentBest.score) return newChain;
        if (newChain.score < currentBest.score) return currentBest;

        // 條件二：總分相同，開始時間較早者優先
        if (newChain.startTime < currentBest.startTime) return newChain;
        if (newChain.startTime > currentBest.startTime) return currentBest;

        // 條件三：時間也相同，裝置編號字典序小者優先
        if (newChain.deviceId.compareTo(currentBest.deviceId) < 0) return newChain;

        return currentBest;
    }
}
//
//感測器攻擊事件關聯器
//Description
//
//校園 IoT 系統會從不同感測器收到異常事件。不同類型的感測器有不同事件分數公式。對同一台裝置而言，若相鄰事件時間差不超過 W，視為同一個事件鏈；若時間差大於 W，則開啟新的事件鏈。
//
//請找出總分最高的事件鏈。若總分相同，事件鏈開始時間較早者優先；若仍相同，裝置編號字典序小者優先。
//
//本題建議使用 Device 父類別與 CAMERA、TEMP、DOOR、POWER 子類別覆寫 getScore 方法，練習 inheritance、polymorphism、sorting 與 attack-chain style correlation。
//
//
//Input
//
//第一行輸入 D E W。接下來 D 行：
//
//deviceId type baseRisk
//接下來 E 行：
//
//time deviceId kind value
//事件不保證已排序。
//
//
//Output
//
//輸出最高分事件鏈：
//
//deviceId startTime endTime score level
//
//level：score >= 300 為 CRITICAL，score >= 150 為 HIGH，否則 LOW。

//Hint
//
//1 <= D <= 1000
//1 <= E <= 200000
//0 <= W <= 100000
//0 <= time <= 1000000000
//1 <= baseRisk <= 1000
//CAMERA: MOTION=base+value*2, TAMPER=base+100
//TEMP: HIGH=base+value*3, LOW=base+value
//DOOR: OPEN=base+value*5, FORCED=base+120
//POWER: SPIKE=base+value*4, OUTAGE=base+80