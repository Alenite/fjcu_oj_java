import java.util.Scanner;

// 定義父類別 Character
abstract class Character {
    String id;
    String type;
    int level;
    int hp;
    int atk;
    int mp;

    public Character(String id, String type, int level, int hp, int atk, int mp) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.hp = hp;
        this.atk = atk;
        this.mp = mp;
    }

    // 抽象方法：讓子類別各自實作戰力公式
    public abstract int getScore();
}

// 子類別：戰士
class WARRIOR extends Character {
    public WARRIOR(String id, String type, int level, int hp, int atk, int mp) {
        super(id, type, level, hp, atk, mp);
    }
    @Override
    public int getScore() {
        return this.level * 100 + this.hp * 2 + this.atk * 5;
    }
}

// 子類別：法師
class MAGE extends Character {
    public MAGE(String id, String type, int level, int hp, int atk, int mp) {
        super(id, type, level, hp, atk, mp);
    }
    @Override
    public int getScore() {
        return this.level * 100 + this.mp * 4 + this.atk * 3;
    }
}

// 子類別：弓箭手
class ARCHER extends Character {
    public ARCHER(String id, String type, int level, int hp, int atk, int mp) {
        super(id, type, level, hp, atk, mp);
    }
    @Override
    public int getScore() {
        return this.level * 100 + this.hp + this.atk * 6;
    }
}

// 子類別：坦克
class TANK extends Character {
    public TANK(String id, String type, int level, int hp, int atk, int mp) {
        super(id, type, level, hp, atk, mp);
    }
    @Override
    public int getScore() {
        return this.level * 100 + this.hp * 3 + this.atk * 2;
    }
}

public class Guild_Power_Ranking {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int N = sc.nextInt();
        int E = sc.nextInt();

        // 使用一般陣列儲存所有角色
        Character[] list = new Character[N];

        // 讀取 N 個角色資料
        for (int i = 0; i < N; i++) {
            String id = sc.next();
            String type = sc.next();
            int level = sc.nextInt();
            int hp = sc.nextInt();
            int atk = sc.nextInt();
            int mp = sc.nextInt();

            if (type.equals("WARRIOR")) {
                list[i] = new WARRIOR(id, type, level, hp, atk, mp);
            } else if (type.equals("MAGE")) {
                list[i] = new MAGE(id, type, level, hp, atk, mp);
            } else if (type.equals("ARCHER")) {
                list[i] = new ARCHER(id, type, level, hp, atk, mp);
            } else if (type.equals("TANK")) {
                list[i] = new TANK(id, type, level, hp, atk, mp);
            }
        }

        // 處理 E 筆事件指令
        for (int i = 0; i < E; i++) {
            String command = sc.next();
            String id = sc.next();
            int value = sc.nextInt();

            // 尋找對應 ID 的角色
            Character target = null;
            for (int j = 0; j < N; j++) {
                if (list[j].id.equals(id)) {
                    target = list[j];
                    break;
                }
            }

            if (target == null) continue;

            // 執行指令
            if (command.equals("TRAIN")) {
                target.level += value;
                target.atk += (value / 2);
            } else if (command.equals("DAMAGE")) {
                target.hp = target.hp - value;
                if (target.hp < 0) target.hp = 0;
            } else if (command.equals("HEAL")) {
                target.hp = target.hp + value;
                if (target.hp > 999) target.hp = 999;
            } else if (command.equals("MANA")) {
                target.mp = target.mp + value;
                if (target.mp > 999) target.mp = 999;
            }
        }

        // 統計存活的角色數量
        int aliveCount = 0;
        for (int i = 0; i < N; i++) {
            if (list[i].hp > 0) {
                aliveCount++;
            }
        }

        // 若全部死亡，輸出 EMPTY
        if (aliveCount == 0) {
            System.out.println("EMPTY");
            return;
        }

        // 把存活的角色放進新陣列中
        Character[] aliveList = new Character[aliveCount];
        int index = 0;
        for (int i = 0; i < N; i++) {
            if (list[i].hp > 0) {
                aliveList[index] = list[i];
                index++;
            }
        }

        // 使用基礎的「氣泡排序法」進行自訂排序
        for (int i = 0; i < aliveCount - 1; i++) {
            for (int j = 0; j < aliveCount - 1 - i; j++) {
                Character c1 = aliveList[j];
                Character c2 = aliveList[j + 1];

                boolean needSwap = false;

                // 1. 比較戰力（大到小）
                if (c1.getScore() < c2.getScore()) {
                    needSwap = true;
                } else if (c1.getScore() == c2.getScore()) {
                    // 2. 戰力相同，比較等級（大到小）
                    if (c1.level < c2.level) {
                        needSwap = true;
                    } else if (c1.level == c2.level) {
                        // 3. 等級也相同，比較 ID 字典序（小到大）
                        if (c1.id.compareTo(c2.id) > 0) {
                            needSwap = true;
                        }
                    }
                }

                // 如果需要交換，進行位置互換
                if (needSwap) {
                    Character temp = aliveList[j];
                    aliveList[j] = aliveList[j + 1];
                    aliveList[j + 1] = temp;
                }
            }
        }

        // 輸出結果
        for (int i = 0; i < aliveCount; i++) {
            System.out.println(aliveList[i].id + " " + aliveList[i].getScore() + " " + aliveList[i].hp);
        }

        sc.close();
    }
}
//多型公會戰力排行榜
//Description
//
//公會有不同職業的角色，不同職業有不同戰力公式。接著系統會收到多筆訓練、受傷、治療與魔力恢復事件。
//
//請模擬所有事件，最後只輸出仍存活的角色，排序規則為戰力高到低、等級高到低、角色編號字典序小到大。若沒有角色存活，輸出 EMPTY。
//
//本題建議建立 Character 父類別與 WARRIOR、MAGE、ARCHER、TANK 子類別覆寫 getScore 方法。
//
//
//Input
//
//第一行輸入 N E。接下來 N 行：
//
//id type level hp atk mp
//接下來 E 行：
//
//command id value
//
//Output
//
//若有存活角色，輸出：
//
//id score hp
//若全部死亡，輸出：
//
//EMPTY


//TRAIN: level += value, atk += floor(value/2)
//DAMAGE: hp = max(0, hp-value)
//HEAL: hp = min(999, hp+value)
//MANA: mp = min(999, mp+value)
//WARRIOR score = level*100 + hp*2 + atk*5
//MAGE score = level*100 + mp*4 + atk*3
//ARCHER score = level*100 + hp + atk*6
//TANK score = level*100 + hp*3 + atk*2