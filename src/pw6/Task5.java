package pw6;

import java.util.Random;
import java.util.Scanner;

public class Task5 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        Asta asta = new Asta();

        final String BOSS_NAME = "Патрі";
        final int BOSS_MAX_HP = 1200;
        int bossHp = BOSS_MAX_HP;
        int round = 0;
        boolean isGuarding = false;

        System.out.println("==================================================");
        System.out.println("     BLACK CLOVER: ПОЄДИНОК АСТИ З ПАТРІ");
        System.out.println("==================================================");
        System.out.println("Аста, народжений без магії, зустрічає Патрі - лідера");
        System.out.println("Ока Опівнічного Сонця. Антимагія проти чарів: бій починається!");

        while (asta.isAlive() && bossHp > 0) {
            round++;

            System.out.println();
            System.out.println("==================== РАУНД " + round + " ====================");

            int bossPercent = Math.max(bossHp, 0) * 100 / BOSS_MAX_HP;

            System.out.println(BOSS_NAME + " (HP): "
                    + buildBar(bossHp, BOSS_MAX_HP) + " "
                    + bossPercent + "% ("
                    + Math.max(bossHp, 0) + "/" + BOSS_MAX_HP + ")");

            asta.showStatus();

            boolean turnFinished = false;

            while (!turnFinished) {
                System.out.println("[1] Удар мечем | [2] Суперздібність");
                System.out.println("[3] Захисна стійка | [4] Переглянути статус");
                System.out.print("Твій вибір: ");

                int action = readInt(scanner);
                System.out.println();

                switch (action) {
                    case 1 -> {
                        int damage = asta.basicSwordAttack();
                        bossHp -= damage;
                        System.out.println(BOSS_NAME + " втрачає " + damage + " HP!");
                        turnFinished = true;
                    }

                    case 2 -> {
                        asta.showSkills();
                        System.out.print("Номер навички (0 - назад): ");

                        int skillNumber = readInt(scanner);
                        System.out.println();

                        if (skillNumber != 0) {
                            int skillIndex = skillNumber - 1;

                            if (asta.canUseSkill(skillIndex)) {
                                int damage = asta.useSkill(skillIndex);

                                if (damage > 0) {
                                    bossHp -= damage;
                                    System.out.println(BOSS_NAME
                                            + " втрачає " + damage + " HP!");
                                }

                                turnFinished = true;
                            } else {
                                asta.useSkill(skillIndex);
                            }
                        }
                    }

                    case 3 -> {
                        isGuarding = true;
                        asta.gainEnergy(10);

                        System.out.println(
                                "Аста приймає захисну стійку: "
                                        + "наступний удар слабший вдвічі. "
                                        + "Антимагія +10.");

                        turnFinished = true;
                    }

                    case 4 -> asta.showStatus();

                    default -> System.out.println(
                            "Невірний вибір, спробуй ще раз.");
                }
            }

            if (bossHp > 0) {
                System.out.println();

                String attackName;
                int bossDamage;

                if (random.nextInt(100) < 20) {
                    attackName = "Потужний Промінь Опівнічного Сонця";
                    bossDamage = 190 + random.nextInt(31);
                } else {
                    attackName = "Магічний промінь";
                    bossDamage = 100 + random.nextInt(61);
                }

                if (isGuarding) {
                    bossDamage /= 2;
                    isGuarding = false;
                    System.out.println("Захисна стійка послабила удар!");
                }

                System.out.println(BOSS_NAME + " випускає «"
                        + attackName + "» (сила " + bossDamage + ")!");

                asta.takeDamage(bossDamage);
                asta.pushLimits();
            }
        }

        System.out.println();
        System.out.println("==================================================");

        if (bossHp <= 0) {
            System.out.println("ПЕРЕМОГА! Антимагія Асти розірвала чари Патрі!");
            System.out.println("Раундів зіграно: " + round);
            asta.showStatus();
        } else {
            System.out.println("ПОРАЗКА... Аста падає, але його дух не зламано.");
            System.out.println("Раундів зіграно: " + round
                    + ". Спробуй ще раз - магія тих, хто не здається, сильніша!");
        }

        System.out.println("==================================================");
        scanner.close();
    }

    private static int readInt(Scanner scanner) {
        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        }

        scanner.nextLine();
        return -1;
    }

    private static String buildBar(int value, int max) {
        int filled = (int) (((double) Math.max(value, 0) / max) * 10);
        String bar = "[";

        for (int i = 0; i < 10; i++) {
            bar += (i < filled) ? "█" : ".";
        }

        return bar + "]";
    }

    // ================= КЛАС АСТА =================

    static class Asta {

        private String name;
        private int hp;
        private int maxHp;
        private int antiMagicEnergy;
        private int baseAttack;
        private int defense;
        private boolean isBlackFormActive;
        private String[] skills;
        private int[] skillCosts;
        private int[] skillDamages;
        private boolean isLimitPushed;

        public Asta() {
            this.name = "Аста";
            this.maxHp = 1000;
            this.hp = maxHp;
            this.antiMagicEnergy = 0;
            this.baseAttack = 120;
            this.defense = 35;
            this.isBlackFormActive = false;

            this.skills = new String[]{
                    "Удар Демоноборця",
                    "Чорний Ураган",
                    "Метеорит Демона",
                    "Форма Чорного Демона"
            };

            this.skillCosts = new int[]{25, 50, 75, 100};
            this.skillDamages = new int[]{220, 380, 550, 0};
            this.isLimitPushed = false;
        }

        public void showStatus() {
            int hpPercent = hp * 100 / maxHp;

            System.out.println("-------------- " + name + " --------------");
            System.out.println("HP:        " + buildBar(hp, maxHp)
                    + " " + hpPercent + "% (" + hp + "/" + maxHp + ")");
            System.out.println("Антимагія: "
                    + buildBar(antiMagicEnergy, 100)
                    + " " + antiMagicEnergy + "/100");
            System.out.println("Форма Чорного Демона: "
                    + (isBlackFormActive
                    ? "АКТИВНА (вся шкода x2)"
                    : "неактивна"));
            System.out.println("-----------------------------------");
        }

        public void showSkills() {
            System.out.println("Навички Асти:");

            for (int i = 0; i < skills.length; i++) {
                String effect = (i == 3)
                        ? "відновлює 300 HP, шкода x2"
                        : "шкода " + skillDamages[i];

                String availability = canUseSkill(i)
                        ? ""
                        : " [недоступно]";

                System.out.println("  " + (i + 1) + " - "
                        + skills[i] + " (енергія "
                        + skillCosts[i] + ", " + effect + ")"
                        + availability);
            }
        }

        public int basicSwordAttack() {
            int damage = isBlackFormActive
                    ? baseAttack * 2
                    : baseAttack;

            gainEnergy(20);

            System.out.println("Аста б'є мечем Демоноборця! Шкода: "
                    + damage
                    + (isBlackFormActive
                    ? " (Чорна форма: x2)"
                    : "")
                    + ". Антимагія +20.");

            return damage;
        }

        public boolean canUseSkill(int skillIndex) {
            if (skillIndex < 0 || skillIndex >= skills.length) {
                return false;
            }

            if (skillIndex == 3 && isBlackFormActive) {
                return false;
            }

            return antiMagicEnergy >= skillCosts[skillIndex];
        }

        public int useSkill(int skillIndex) {
            if (!canUseSkill(skillIndex)) {
                if (skillIndex < 0 || skillIndex >= skills.length) {
                    System.out.println(
                            "Попередження: такої навички не існує!");
                } else if (skillIndex == 3 && isBlackFormActive) {
                    System.out.println(
                            "Попередження: Форма Чорного Демона вже активна!");
                } else {
                    System.out.println("Попередження: недостатньо антимагії "
                            + "для «" + skills[skillIndex] + "»! Потрібно "
                            + skillCosts[skillIndex] + ", у тебе "
                            + antiMagicEnergy + ".");
                }

                return 0;
            }

            antiMagicEnergy -= skillCosts[skillIndex];

            if (skillIndex == 3) {
                isBlackFormActive = true;
                hp = Math.min(maxHp, hp + 300);

                System.out.println("«" + skills[3]
                        + "»! Аста поглинає Чорного Демона: "
                        + "+300 HP, уся шкода тепер x2!");

                return 0;
            }

            int damage = isBlackFormActive
                    ? skillDamages[skillIndex] * 2
                    : skillDamages[skillIndex];

            System.out.println("Аста використовує «"
                    + skills[skillIndex] + "»! Шкода: " + damage
                    + (isBlackFormActive ? " (Чорна форма: x2)" : "")
                    + ". Антимагія -" + skillCosts[skillIndex] + ".");

            return damage;
        }

        public void takeDamage(int incomingDmg) {
            int realDamage = Math.max(incomingDmg - defense, 15);

            hp -= realDamage;

            if (hp <= 0) {
                hp = 0;
            }

            System.out.println("Аста отримує " + realDamage
                    + " шкоди (удар " + incomingDmg
                    + ", броня -" + defense + "). HP: "
                    + hp + "/" + maxHp);
        }

        public void pushLimits() {
            if (isLimitPushed || hp <= 0
                    || hp >= maxHp * 25 / 100) {
                return;
            }

            isLimitPushed = true;

            System.out.println();
            System.out.println(
                    "Аста: «Моя магія - ніколи не здаватися!»");

            hp = Math.min(maxHp, hp + 150);
            gainEnergy(50);

            System.out.println("Аста долає свої межі: "
                    + "+150 HP та +50 антимагії! HP: "
                    + hp + "/" + maxHp);
        }

        public boolean isAlive() {
            return hp > 0;
        }

        public void gainEnergy(int amount) {
            antiMagicEnergy = Math.min(
                    100, antiMagicEnergy + amount);
        }

        private String buildBar(int value, int max) {
            int filled = (int) (((double) Math.max(value, 0) / max) * 10);
            String bar = "[";

            for (int i = 0; i < 10; i++) {
                bar += (i < filled) ? "█" : ".";
            }

            return bar + "]";
        }
    }
}

