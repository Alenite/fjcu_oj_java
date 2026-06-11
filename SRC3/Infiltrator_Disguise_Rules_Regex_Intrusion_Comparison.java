import java.util.Scanner;

public class Infiltrator_Disguise_Rules_Regex_Intrusion_Comparison {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNext()) return;
        // 關鍵修正：改用 next() 替代 nextLine()，防止讀到空行或字尾隱藏的換行符號
        String s = sc.next();

        if (!sc.hasNext()) return;
        String p = sc.next();

        int n = s.length();
        int m = p.length();

        // dp[i][j] 代表 s 的前 i 個字元與 p 的前 j 個字元是否能完全匹配
        boolean[][] dp = new boolean[n+1][m+1];
        dp[0][0] = true; // 空字串與空模式必定匹配

        // 初始化第一列：處理 s 為空字串時，p 含有 '*' 的情況 (例如 p = "a*", "a*b*")
        for(int j = 2; j <= m; j++){
            if(p.charAt(j-1) == '*'){
                dp[0][j] = dp[0][j-2];
            }
        }

        // 開始進行動態規劃填表
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= m; j++){
                char pc = p.charAt(j-1);

                if(pc == '*'){
                    // 情況一：讓 '*' 匹配 0 次，直接拋棄前一個字元與 '*'
                    dp[i][j] = dp[i][j-2];

                    char pre = p.charAt(j-2); // 取得 '*' 前面的那個元素

                    // 情況二：如果前一個元素能與 s[i-1] 匹配，則可以選擇匹配 1 次或多次
                    if(pre == s.charAt(i-1) || pre == '.'){
                        dp[i][j] = dp[i][j] || dp[i-1][j];
                    }

                } else if(pc == '.' || pc == s.charAt(i-1)){
                    // 當前字元為 '.' 或是與 s[i-1] 完全相同，看各自前一個狀態
                    dp[i][j] = dp[i-1][j-1];
                }
            }
        }

        // 輸出最終結果
        System.out.println(dp[n][m] ? "true" : "false");

        sc.close();
    }
}
//滲透者的偽裝規則：Regex 入侵比對
//Description
//
//你是一名藍隊分析師，負責監控某個遭到持續性滲透（APT）攻擊的企業網站。
//
//最近 SOC（Security Operations Center）發現，攻擊者會用經過偽裝的請求字串來繞過 WAF 規則。你的任務是撰寫一個惡意流量模式比對器，判斷某一筆可疑流量s是否能被規則樣式p完整匹配。
//
//規則樣式只支援兩種特殊符號：
//
//.：匹配任意單一字元
//*：匹配前一個元素出現 0 次或多次
//請判斷模式p是否能完整覆蓋整個字串s。注意，不能只匹配其中一部分，必須整段完全符合。
//
//
//Input
//
//s
//p
//s：輸入字串，只包含小寫英文字母
//p：模式字串，只包含小寫英文字母、.、*
//1 <= s.length <= 20
//1 <= p.length <= 20
//題目保證每個*前面一定有合法字元
//
//Output
//
//若模式p可以完整匹配字串s，輸出true；否則輸出false。

