import java.util.Scanner;

// 1. 定義角色父類別
abstract class Character2 {
    String name;
    String className;
    int hp;
    int atk;
    int buff; // 累積的 BUFF 值

    public Character2(String name, String className, int hp, int atk) {
        this.name = name;
        this.className = className;
        this.hp = hp;
        this.atk = atk;
        this.buff = 0;
    }

    // 檢查是否存活
    public boolean isAlive() {
        return this.hp > 0;
    }

    // 取得存活文字狀態
    public String getStatusString() {
        return isAlive() ? "ALIVE" : "DEAD";
    }

    // 抽象方法：由各職業子類別實作傷害計算（將防守方 target 傳入）
    public abstract int calculateDamage(Character2 target);
}

// 子類別：戰士
class WARRIOR2 extends Character2 {
    public WARRIOR2(String name, String className, int hp, int atk) {
        super(name, className, hp, atk);
    }
    @Override
    public int calculateDamage(Character2 target) {
        return this.atk + this.buff + 5;
    }
}

// 子類別：法師
class MAGE2 extends Character2 {
    public MAGE2(String name, String className, int hp, int atk) {
        super(name, className, hp, atk);
    }
    @Override
    public int calculateDamage(Character2 target) {
        return (this.atk + this.buff) * 2;
    }
}

// 子類別：弓箭手
class ARCHER2 extends Character2 {
    public ARCHER2(String name, String className, int hp, int atk) {
        super(name, className, hp, atk);
    }
    @Override
    public int calculateDamage(Character2 target) {
        // Java 整數除法會自動無條件捨去（floor）
        return this.atk + this.buff + (target.hp / 10);
    }
}

public class Multi_character_combat_simulation {
    // 輔助方法：透過姓名在陣列中尋找角色的索引
    private static int findCharacterIndex(Character2[] chars, int count, String name) {
        for (int i = 0; i < count; i++) {
            if (chars[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int N = sc.nextInt();
        int Q = sc.nextInt();

        Character2[] chars = new Character2[N];

        // 1. 讀取 N 個角色資料
        for (int i = 0; i < N; i++) {
            String name = sc.next();
            String className = sc.next();
            int hp = sc.nextInt();
            int atk = sc.nextInt();

            if (className.equals("WARRIOR")) {
                chars[i] = new WARRIOR2(name, className, hp, atk);
            } else if (className.equals("MAGE")) {
                chars[i] = new MAGE2(name, className, hp, atk);
            } else if (className.equals("ARCHER")) {
                chars[i] = new ARCHER2(name, className, hp, atk);
            }
        }

        // 2. 處理 Q 筆指令
        for (int i = 0; i < Q; i++) {
            String command = sc.next();

            if (command.equals("ATTACK")) {
                String attackerName = sc.next();
                String targetName = sc.next();

                int aIdx = findCharacterIndex(chars, N, attackerName);
                int tIdx = findCharacterIndex(chars, N, targetName);

                if (aIdx == -1 || tIdx == -1) continue;

                Character2 attacker = chars[aIdx];
                Character2 target = chars[tIdx];

                // 規則：攻擊方或防守方只要有一人死亡就忽略
                if (!attacker.isAlive() || !target.isAlive()) {
                    continue;
                }

                // 計算傷害並扣血
                int dmg = attacker.calculateDamage(target);
                target.hp -= dmg;
                if (target.hp < 0) target.hp = 0; // 血量最低為 0

                // 規則：攻擊後該角色 BUFF 歸零
                attacker.buff = 0;

            } else if (command.equals("HEAL")) {
                String name = sc.next();
                int x = sc.nextInt();

                int idx = findCharacterIndex(chars, N, name);
                if (idx == -1) continue;

                Character2 c = chars[idx];
                // 規則：死亡角色不能治療
                if (c.isAlive()) {
                    c.hp += x;
                }

            } else if (command.equals("BUFF")) {
                String name = sc.next();
                int x = sc.nextInt();

                int idx = findCharacterIndex(chars, N, name);
                if (idx == -1) continue;

                Character2 c = chars[idx];
                // 規則：死亡角色不能獲得 BUFF
                if (c.isAlive()) {
                    c.buff += x;
                }

            } else if (command.equals("STATUS")) {
                String name = sc.next();

                int idx = findCharacterIndex(chars, N, name);
                if (idx == -1) continue;

                Character2 c = chars[idx];
                // 立即輸出該角色狀態
                System.out.println(c.name + " " + c.hp + " " + c.getStatusString());
            }
        }

        // 3. 輸出 FINAL 分隔線
        System.out.println("FINAL");

        // 4. 使用基礎「氣泡排序法」對全體角色進行最終排行
        for (int i = 0; i < N - 1; i++) {
            for (int j = 0; j < N - 1 - i; j++) {
                Character2 c1 = chars[j];
                Character2 c2 = chars[j + 1];

                boolean needSwap = false;

                // 條件一：生命值由高到低（大到小）
                if (c1.hp < c2.hp) {
                    needSwap = true;
                } else if (c1.hp == c2.hp) {
                    // 條件二：生命值相同，姓名字典序由小到大
                    if (c1.name.compareTo(c2.name) > 0) {
                        needSwap = true;
                    }
                }

                if (needSwap) {
                    Character2 temp = chars[j];
                    chars[j] = chars[j + 1];
                    chars[j + 1] = temp;
                }
            }
        }

        // 5. 輸出最終排行榜結果
        for (int i = 0; i < N; i++) {
            System.out.println(chars[i].name + " " + chars[i].hp + " " + chars[i].getStatusString());
        }

        sc.close();
    }
}
//多型角色戰鬥模擬
//Description
//
//在一場角色戰鬥模擬中，每個角色都有職業、生命值與攻擊力。不同職業攻擊時有不同傷害計算方式，指令可能會攻擊、治療、加成或查詢狀態。請模擬所有指令並輸出查詢結果與最後排行榜。
//
//
//Input
//
//第一行輸入 N Q。接下來 N 行輸入角色資料：
//
//name class hp atk
//接下來 Q 行為指令，格式為以下其中一種：
//
//ATTACK attacker target
//HEAL name x
//BUFF name x
//STATUS name
//class 只會是 WARRIOR、MAGE、ARCHER
//角色生命值小於等於 0 視為死亡，死亡角色不能攻擊、治療或獲得 BUFF
//攻擊死亡角色或由死亡角色攻擊皆忽略
//BUFF 會累加在下一次有效攻擊，攻擊後該角色 BUFF 歸零
//WARRIOR 傷害 = atk + buff + 5
//MAGE 傷害 = (atk + buff) * 2
//ARCHER 傷害 = atk + buff + floor(targetCurrentHp / 10)
//
//Output
//
//每遇到 STATUS 指令，輸出該角色目前生命值與狀態。所有指令處理完後，輸出 FINAL，再依生命值由高到低、姓名字典序由小到大輸出所有角色。
//
//name hp ALIVE_or_DEAD
//FINAL
//name hp ALIVE_or_DEAD

//Hint
//
//1 <= N <= 1000
//1 <= Q <= 200000
//1 <= hp, atk, x <= 1000000
//name 長度 <= 20
//建議建立 Character 父類別，並讓不同職業覆寫攻擊傷害方法。