class RPG {
    private int[] defence;
    private int[] attack;
    private int n;
    private Integer[][] damageCalculated;

    public RPG(int[] defence, int[] attack) {
        // Initialize some variables
        this.defence = defence;
        this.attack = attack;
    }

    public int calculateDamage(int round, int is_boosted) {
        if (round > this.n) {
            return 0;
        }

        if (damageCalculated[is_boosted][round] != null) {
            // System.out.println("memorized!!");
            return damageCalculated[is_boosted][round];
        }
        int damage;

        if (is_boosted == 1) {
            damage = 2 * attack[round] - defence[round] + calculateDamage(round + 1, 0);
        } else {
            int attackDamage = attack[round] - defence[round] + calculateDamage(round + 1, 0);
            int boostedDamage = calculateDamage(round + 1, 1);
            if (attackDamage > boostedDamage) {
                damage = attackDamage;
            } else {
                damage = boostedDamage;
            }

        }
        damageCalculated[is_boosted][round] = damage;
        return damage;
    }

    public int maxDamage(int n) {
        // return the highest total damage after n rounds.
        this.damageCalculated = new Integer[2][n]; // initiate with null value
        this.n = n - 1;
        return calculateDamage(0, 0); // attack in the first round is not boosted
    }

    // public static void main(String[] args) {
    // RPG sol = new RPG(new int[] { 116, 148, 186, 177, 192, 108, 190, 113, 159,
    // 163, 158, 51, 191, 64,
    // 6, 125, 84, 2, 100, 181 },
    // new int[] { 292, 292, 295, 286, 290, 295, 290, 288, 293, 294, 289, 293, 288,
    // 289, 289, 288, 294, 295, 287, 291 });
    // System.out.println(sol.maxDamage(19));
    // // 1: boost, 2: attack, 3: boost, 4: attack, 5: boost, 6: attack
    // // maxDamage: 1187
    // }
}