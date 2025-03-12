import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import com.google.gson.*;

class OutputFormat {
    int[] defence;
    int[] attack;
    int k;
    int answer;
}

class test {
    public static void main(String[] args) {
        Gson gson = new Gson();
        OutputFormat[] datas;
        int num_ac = 0;
        int user_ans;
        OutputFormat data;

        try {
            datas = gson.fromJson(new FileReader("test_RPG.json"), OutputFormat[].class);
            for (int i = 0; i < datas.length; ++i) {
                data = datas[i];
                user_ans = new RPG(data.defence, data.attack).maxDamage(data.k);
                System.out.print("Sample" + i + ": ");
                if (data.answer == user_ans) {
                    System.out.println("AC");
                    num_ac++;
                } else {
                    System.out.println("WA");
                    System.out.println("Data_atk:  " + Arrays.toString(data.attack));
                    System.out.println("Data_dfc:  " + Arrays.toString(data.defence));
                    System.out.println("Test_ans:  " + data.answer);
                    System.out.println("User_ans:  " + user_ans);
                    System.out.println("");
                }
            }
            System.out.println("Score: " + num_ac + "/" + datas.length);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

// paste your own RPG class here :)
// class RPG {
// private int[] defence;
// private int[] attack;
// private int n;
// private int[][] damageCalculated;

// public RPG(int[] defence, int[] attack) {
// // Initialize some variables
// this.defence = defence;
// this.attack = attack;
// }

// public int calculateDamage(int round, int is_boosted) {
// if (round > this.n)
// return 0;
// if (damageCalculated[is_boosted][round] != 0) {
// return damageCalculated[is_boosted][round];
// }
// int damage;
// if (is_boosted == 1) {
// damage = 2 * attack[round] - defence[round] + calculateDamage(round + 1, 0);
// } else {
// int attackDamage = attack[round] - defence[round] + calculateDamage(round +
// 1, 0);
// int boostedDamage = calculateDamage(round + 1, 1);
// if (attackDamage > boostedDamage) {
// damage = attackDamage;
// } else {
// damage = boostedDamage;
// }

// }
// damageCalculated[is_boosted][round] = damage;
// return damage;
// }

// public int maxDamage(int n) {
// // return the highest total damage after n rounds.
// this.damageCalculated = new int[2][n];
// this.n = n - 1;
// return calculateDamage(0, 0);
// }

// // public static void main(String[] args) {
// // RPG sol = new RPG(new int[] { 5, 4, 1, 7, 98, 2 }, new int[] { 200, 200,
// 200,
// // 200, 200, 200 });
// // System.out.println(sol.maxDamage(6));
// // // 1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
// // // maxDamage: 1187
// // }
// }