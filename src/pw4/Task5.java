package pw4;

import java.util.Scanner;

public class Task5 {

    static final int BOSS_MAX_HP = 10000;
    static final int BOSS_BASE_ATTACK = 700;
    static final int MAX_AMMO = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("   BRAWL STARS: ROBO RUMBLE - БИТВА З РОБО-БОСОМ");
        System.out.println("==================================================");
        System.out.println("Величезний Робо-Бос атакує сейф арени! Обери бійця:");
        System.out.println("  1 - Шеллі     (HP: 4000, Дробовик: 1200, Супер: 2800 + відкидає боса, його атака -50%)");
        System.out.println("  2 - Кольт     (HP: 3000, Черга куль: 1600, Супер: 3600)");
        System.out.println("  3 - Ель Прімо (HP: 6000, Кулаки: 1100, Супер: 2200 + оглушення боса)");

        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.print("Твій вибір (1-3): ");
            choice = scanner.hasNextInt() ? scanner.nextInt() : 0;
            scanner.nextLine(); // очищення буфера
        }

        // ===== Характеристики обраного бійця (modern switch) =====
        String brawlerName = switch (choice) {
            case 1 -> "Шеллі";
            case 2 -> "Кольт";
            default -> "Ель Прімо";
        };
        int maxHp = switch (choice) {
            case 1 -> 4000;
            case 2 -> 3000;
            default -> 6000;
        };
        int attackDamage = switch (choice) {
            case 1 -> 1200;
            case 2 -> 1600;
            default -> 1100;
        };
        int superDamage = switch (choice) {
            case 1 -> 2800;
            case 2 -> 3600;
            default -> 2200;
        };
        String attackName = switch (choice) {
            case 1 -> "Атака дробовиком";
            case 2 -> "Атака чергою куль";
            default -> "Атака кулаками";
        };
        String superName = switch (choice) {
            case 1 -> "Супер-дріб";
            case 2 -> "Кульова буря";
            default -> "Метеорний стрибок";
        };

        // ===== Стан битви =====
        int playerHp = maxHp;
        int bossHp = BOSS_MAX_HP;
        int ammo = MAX_AMMO;
        int superCharge = 0;
        int powerCubes = 0;       // кожен куб назавжди дає +15% до шкоди
        int rounds = 0;
        boolean bossStunned = false;   // супер Ель Прімо: бос втрачає наступний хід
        boolean bossWeakened = false;  // супер Шеллі: наступна атака боса -50%
        int lastPhase = 1;

        System.out.println();
        System.out.println("Ти обрав: " + brawlerName + "! Захисти сейф та зупини залізного гіганта!");

        // ===== Ігровий цикл =====
        while (playerHp > 0 && bossHp > 0) {
            rounds++;

            // --- Візуалізація стану ---
            System.out.println();
            System.out.println("================== РАУНД " + rounds + " ==================");
            drawHealthBar("РОБО-БОС", bossHp, BOSS_MAX_HP);
            drawHealthBar(brawlerName, playerHp, maxHp);
            int phase = getPhase(bossHp);
            System.out.println("Фаза боса: " + phaseName(phase) + " (шкода за удар: " + calcBossDamage(phase) + ")");
            System.out.println("Набої: " + ammo + "/" + MAX_AMMO
                    + " | Супер: " + superCharge + "%" + (superCharge >= 100 ? " (ГОТОВИЙ!)" : "")
                    + " | Power Cubes: " + powerCubes + " (+" + (powerCubes * 15) + "% шкоди)");
            System.out.println("--------------------------------------------------");
            System.out.println("  1 - Основна атака (-1 набій, +35% заряду супера)");
            System.out.println("  2 - СУПЕР (потрібен заряд 100%)");
            System.out.println("  3 - Захисний маневр і перезарядка (+2 набої, +15% HP)");
            System.out.println("  4 - Ризик: пошук Power Cube (60% успіху)");

            // --- Вибір дії (з перевіркою) ---
            int action = 0;
            boolean validAction = false;
            while (!validAction) {
                System.out.print("Твій вибір (1-4): ");
                action = scanner.hasNextInt() ? scanner.nextInt() : 0;
                scanner.nextLine(); // очищення буфера
                if (action < 1 || action > 4) {
                    System.out.println("Невірний вибір, спробуй ще раз.");
                } else if (action == 2 && superCharge < 100) {
                    System.out.println("Супер ще не готовий! Заряд: " + superCharge + "%. Обери іншу дію.");
                } else {
                    validAction = true;
                }
            }
            System.out.println();

            // --- Хід гравця ---
            // Множник кубів рахуємо в цілих числах, щоб не втратити точність: 100% + 15% за кожен куб
            int damageMultiplierPercent = 100 + 15 * powerCubes;
            switch (action) {
                case 1 -> {
                    if (ammo >= 1) {
                        int damage = attackDamage * damageMultiplierPercent / 100;
                        bossHp -= damage;
                        ammo--;
                        superCharge = Math.min(100, superCharge + 35);
                        System.out.println(attackName + "! Робо-Бос отримує " + damage + " шкоди.");
                        if (superCharge >= 100) {
                            System.out.println("СУПЕР ГОТОВИЙ!");
                        }
                    } else {
                        System.out.println("Набої закінчились! Перезарядка забирає твій хід.");
                    }
                }
                case 2 -> {
                    int damage = superDamage * damageMultiplierPercent / 100;
                    bossHp -= damage;
                    superCharge = 0;
                    System.out.println("СУПЕР «" + superName + "»! Колосальна шкода: " + damage + "!");
                    if (choice == 1) {
                        bossWeakened = true;
                        System.out.println("Бос відкинутий: його наступна атака буде на 50% слабшою.");
                    } else if (choice == 3) {
                        bossStunned = true;
                        System.out.println("Бос оглушений: він втратить свій наступний хід!");
                    }
                }
                case 3 -> {
                    int ammoBefore = ammo;
                    ammo = Math.min(MAX_AMMO, ammo + 2);
                    int heal = Math.min(maxHp - playerHp, maxHp * 15 / 100);
                    playerHp += heal;
                    System.out.println("Захисний маневр! Набої: +" + (ammo - ammoBefore) + ", здоров'я: +" + heal + " HP.");
                }
                case 4 -> {
                    if (Math.random() < 0.6) {
                        powerCubes++;
                        System.out.println("Ти знайшов Power Cube! Уся твоя шкода назавжди зросла на 15%.");
                    } else {
                        System.out.println("Енергетичний куб не знайдено... Час втрачено.");
                    }
                }
            }

            // --- Хід Робо-Боса ---
            if (bossHp > 0) {
                int currentPhase = getPhase(bossHp);
                if (currentPhase > lastPhase) {
                    lastPhase = currentPhase;
                    System.out.println(currentPhase == 2
                            ? "!!! Робо-Бос червоніє від люті: ФАЗА 2 - ANGRY (шкода +30%) !!!"
                            : "!!! Робо-Бос шаленіє: ФАЗА 3 - ENRAGED / GOD MODE (шкода +70%) !!!");
                }

                if (bossStunned) {
                    System.out.println("Робо-Бос оглушений і пропускає свій хід!");
                    bossStunned = false;
                } else {
                    int bossDamage = calcBossDamage(currentPhase);
                    if (bossWeakened) {
                        bossDamage = bossDamage / 2;
                        bossWeakened = false;
                        System.out.println("Відкинутий бос б'є слабше (-50%).");
                    }
                    if (Math.random() < 0.15) {
                        System.out.println("Ти спритно ухилився від удару кувалдою! Шкоди немає.");
                    } else {
                        playerHp -= bossDamage;
                        System.out.println("Робо-Бос б'є кувалдою: -" + bossDamage + " HP!");
                    }
                }

                // Щораунду автоматично відновлюється 1 набій (не більше 3)
                ammo = Math.min(MAX_AMMO, ammo + 1);
            }
        }

        // ===== Завершення битви =====
        System.out.println();
        System.out.println("==================== КІНЕЦЬ БИТВИ ====================");
        drawHealthBar("РОБО-БОС", bossHp, BOSS_MAX_HP);
        drawHealthBar(brawlerName, playerHp, maxHp);
        System.out.println();
        if (bossHp <= 0) {
            System.out.println("ПЕРЕМОГА! Робо-Бос розсипається на запчастини!");
            System.out.println("Раундів зіграно: " + rounds);
            System.out.println("Нагорода: +10 Трофеїв 🏆, 100 Star Drops!");
        } else {
            System.out.println("ПОРАЗКА... Сейф арени зруйновано Робо-Босом.");
            System.out.println("Раундів зіграно: " + rounds);
            System.out.println(choice == 3
                    ? "Навіть Ель Прімо не впорався. Спробуй інакше використовувати Супер та Power Cube!"
                    : "Спробуй взяти витривалішого бійця - Ель Прімо.");
        }

        scanner.close();
    }

    // Смужка здоров'я з 10 символів
    static void drawHealthBar(String title, int currentHp, int maxHp) {
        int hp = Math.max(currentHp, 0);
        int bars = (int) (((double) hp / maxHp) * 10);
        System.out.print(String.format("%-10s", title) + " [");
        for (int i = 0; i < bars; i++) {
            System.out.print("█");
        }
        for (int i = 0; i < 10 - bars; i++) {
            System.out.print("░");
        }
        System.out.println("] " + hp + " / " + maxHp);
    }

    // Фаза люті: 1 - нормальна (HP > 60%), 2 - ANGRY (30%-60%), 3 - ENRAGED (< 30%)
    // Порівняння в цілих числах, щоб уникнути похибок double
    static int getPhase(int bossHp) {
        if (bossHp * 100 > BOSS_MAX_HP * 60) {
            return 1;
        } else if (bossHp * 100 >= BOSS_MAX_HP * 30) {
            return 2;
        } else {
            return 3;
        }
    }

    // Шкода боса залежно від фази (збільшення через приведення типу (int))
    static int calcBossDamage(int phase) {
        int bossDamage = BOSS_BASE_ATTACK;
        if (phase == 2) {
            bossDamage = (int) (bossDamage * 1.3);
        } else if (phase == 3) {
            bossDamage = (int) (bossDamage * 1.7);
        }
        return bossDamage;
    }

    static String phaseName(int phase) {
        return switch (phase) {
            case 1 -> "1 - НОРМАЛЬНА";
            case 2 -> "2 - ANGRY";
            default -> "3 - ENRAGED / GOD MODE";
        };
    }
}

