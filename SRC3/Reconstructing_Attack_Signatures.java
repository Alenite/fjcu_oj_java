import java.io.*;
import java.util.*;

public class Reconstructing_Attack_Signatures {
    public static void main(String[] args) throws Exception {
        // 使用 BufferedReader 進行高效的輸入流讀取
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line1 = br.readLine();
        if (line1 == null) return;
        // 使用 StringTokenizer 解析第一行：封包事件流 s
        StringTokenizer st1 = new StringTokenizer(line1);
        if (!st1.hasMoreTokens()) return;
        String s = st1.nextToken();

        String line2 = br.readLine();
        if (line2 == null) return;
        // 使用 StringTokenizer 解析第二行：攻擊簽章 t
        StringTokenizer st2 = new StringTokenizer(line2);
        if (!st2.hasMoreTokens()) return;
        String t = st2.nextToken();

        int sLen = s.length();
        int tLen = t.length();

        // 宣告 DP 矩陣
        int[][] dp = new int[sLen + 1][tLen + 1];

        // 初始化：當 t 是空字串時 (j = 0)，不管 s 長度多少，都有 1 種方法（即全刪除）
        for (int i = 0; i <= sLen; i++) {
            dp[i][0] = 1;
        }

        // 動態規劃狀態轉移
        for (int i = 1; i <= sLen; i++) {
            for (int j = 1; j <= tLen; j++) {
                // 字串索引從 0 開始，因此第 i 個字元在字串中是 i - 1
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    // 若字元相同，可選擇「使用它匹配」或「忽略它」
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
                } else {
                    // 若字元不同，只能選擇「忽略它」
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        // 輸出最終計算出的還原總方式數
        System.out.println(dp[sLen][tLen]);
    }
}
//從封包流中還原攻擊簽章（Reconstructing Attack Signatures）
//Description
//
//某資安分析系統正在檢查一段可疑的網路封包紀錄。
//
//系統將整段封包特徵簡化成一個字串s，其中每個字元代表一個事件，例如：
//
//a：異常登入嘗試
//b：可疑檔案存取
//g：權限提升跡象
//……
//另一方面，資安團隊已知某個攻擊行為的特徵簽章為字串t。
//
//你的任務是判斷：在不改變原始順序的前提下，從封包紀錄s中可以挑出多少種不同的方式，還原出完整攻擊簽章t？
//
//注意：
//
//你可以刪除s中任意數量的字元
//但不能改變剩下字元的相對順序
//兩種還原方式只要刪除的位置不同，就視為不同方式
//請輸出可還原出攻擊簽章t的總方式數。
//
//
//Input
//
//s
//t
//第一行為字串s，表示封包事件流
//第二行為字串t，表示攻擊簽章
//1 <= s.length, t.length <= 1000
//s和t僅由英文字母組成
//
//Output
//
//輸出一個整數，表示可以從s中還原出t的不同方式數。

//Hint
//
//這是一題典型的動態規劃問題。
//令dp[i][j]表示從s的前i個字元中，還原出t的前j個字元的方式數。
//若s[i-1] == t[j-1]，可以選擇使用它來匹配，或忽略它。
//若兩者不同，則只能忽略s[i-1]。
//請注意方式數可能很大，因此狀態轉移要正確處理加總。