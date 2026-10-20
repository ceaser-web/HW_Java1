package pw3;

import java.util.Scanner;

public class Task5 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("     ВТЕЧА З ТЕМНИЦІ ЧАКЛУНА - RPG-КВЕСТ");
        System.out.println("==================================================");
        System.out.println();

        // ===== 1. Створення персонажа =====
        System.out.print("Як тебе звати, шукачу пригод? ");
        String playerName = scanner.nextLine();

        System.out.println();
        System.out.println("Обери клас героя:");
        System.out.println("  1 - Воїн      (HP: 120, Сила: 15, Мана: 10, Спритність: 8)");
        System.out.println("  2 - Маг       (HP: 75,  Сила: 6,  Мана: 50, Спритність: 10)");
        System.out.println("  3 - Розбійник (HP: 90,  Сила: 10, Мана: 15, Спритність: 18)");
        System.out.println("  Будь-що інше - Волоцюга (HP: 60, Сила: 8, Мана: 5, Спритність: 8)");
        System.out.print("Твій вибір: ");
        int classChoice = scanner.hasNextInt() ? scanner.nextInt() : 0;
        scanner.nextLine(); // очищення буфера після числового введення

        String className = switch (classChoice) {
            case 1 -> "Воїн";
            case 2 -> "Маг";
            case 3 -> "Розбійник";
            default -> "Волоцюга";
        };
        int playerHp = switch (classChoice) {
            case 1 -> 120;
            case 2 -> 75;
            case 3 -> 90;
            default -> 60;
        };
        int strength = switch (classChoice) {
            case 1 -> 15;
            case 2 -> 6;
            case 3 -> 10;
            default -> 8;
        };
        int mana = switch (classChoice) {
            case 1 -> 10;
            case 2 -> 50;
            case 3 -> 15;
            default -> 5;
        };
        int agility = switch (classChoice) {
            case 1 -> 8;
            case 2 -> 10;
            case 3 -> 18;
            default -> 8;
        };
        boolean hasSpellBook = classChoice == 2; // книга заклинань є лише у Мага
        String artifacts = "немає";
        boolean obstacleCleared = false;

        System.out.println();
        System.out.println("Ти прокидаєшся у холодній темниці могутнього чаклуна, " + playerName + " (" + className + ").");
        System.out.println("Твої характеристики: HP " + playerHp + ", Сила " + strength
                + ", Мана " + mana + ", Спритність " + agility);

        // ===== 2. Етап 1: Роздоріжжя трьох дверей =====
        System.out.println();
        System.out.println("  +-------+     +-------+     +-------+");
        System.out.println("  |       |     |       |     |       |");
        System.out.println("  |   П   |     |   С   |     |   З   |");
        System.out.println("  |      o|     |      o|     |      o|");
        System.out.println("  +-------+     +-------+     +-------+");
        System.out.println("   північ         схід          захід");
        System.out.println();
        System.out.print("Куди підеш? (північ / схід / захід): ");
        String direction = scanner.nextLine().trim();

        // ===== 3. Етап 2: Випробування кімнати =====
        switch (direction.toLowerCase()) {
            case "північ" -> {
                System.out.println();
                System.out.println(">>> КІМНАТА СТАРОДАВНЬОЇ ЗАГАДКИ <<<");
                System.out.println("Магічний кристал спалахує і шепоче:");
                System.out.println("«Скільки буде 7 / 2 + 7 % 2 (цілочисельні операції Java)?»");
                System.out.print("Твоя відповідь: ");
                String answer = scanner.nextLine().trim();
                boolean isCorrect = answer.equals("4");
                playerHp = isCorrect ? (playerHp + 20) : (playerHp - 30);
                obstacleCleared = isCorrect;
                System.out.println(isCorrect
                        ? "Кристал яскраво сяє! Ти відновлюєш сили: +20 HP."
                        : "Кристал б'є блискавкою! Втрата здоров'я: -30 HP.");
            }
            case "схід" -> {
                System.out.println();
                System.out.println(">>> АРСЕНАЛ ТА ЗАЧИНЕНА СКРИНЯ ВАРТОВОГО <<<");
                System.out.println("Скриня зі складним замком. Відкрити її можуть спритні пальці");
                System.out.println("(Спритність >= 16) або магічна книга (Мана >= 25 та книга заклинань).");
                // «лінива» складена умова: якщо agility >= 16, друга частина не перевіряється
                if (agility >= 16 || (mana >= 25 && hasSpellBook)) {
                    String artifact = agility >= 16
                            ? "Легендарний меч «Тіньовий клинок»"
                            : "Амулет мага «Око Вічності»";
                    int strengthBonus = agility >= 16 ? 5 : 3;
                    strength += strengthBonus;
                    artifacts = artifact;
                    obstacleCleared = true;
                    System.out.println("Замок піддався! Ти знаходиш: " + artifact + ".");
                    System.out.println("Бонус до сили: +" + strengthBonus + ".");
                } else {
                    playerHp -= 25;
                    System.out.println("Замок не піддається... Спрацювала пастка зі стрілами! -25 HP.");
                }
            }
            case "захід" -> {
                final int GOLEM_DAMAGE = 80;
                System.out.println();
                System.out.println(">>> ПЕЧЕРА КАМ'ЯНОГО ГОЛЕМА <<<");
                System.out.println("Величезний голем блокує вихід. Один його удар - це " + GOLEM_DAMAGE + " HP!");
                System.out.println("Обери тактику:");
                System.out.println("  1 - Лобова атака          (потрібна Сила >= 12)");
                System.out.println("  2 - Магічний бар'єр       (потрібна Мана >= 25)");
                System.out.println("  3 - Спроба прослизнути    (потрібна Спритність >= 14)");
                System.out.print("Твій вибір: ");
                int tactic = scanner.hasNextInt() ? scanner.nextInt() : 0;
                scanner.nextLine(); // очищення буфера

                String tacticName = switch (tactic) {
                    case 1 -> "лобова атака";
                    case 2 -> "магічний бар'єр";
                    case 3 -> "спроба прослизнути повз";
                    default -> "розгубленість";
                };
                boolean success = switch (tactic) {
                    case 1 -> strength >= 12;
                    case 2 -> mana >= 25;
                    case 3 -> agility >= 14;
                    default -> false;
                };

                System.out.println("Твоя тактика: " + tacticName + ".");
                if (success) {
                    obstacleCleared = true;
                    artifacts = "Серце голема";
                    System.out.println("Тактика спрацювала! Голем розсипається на камінці. Ти забираєш: Серце голема.");
                } else {
                    playerHp -= GOLEM_DAMAGE;
                    System.out.println("Не вийшло! Голем б'є з усієї сили: -" + GOLEM_DAMAGE + " HP.");
                }
            }
            default -> {
                playerHp -= 15;
                System.out.println();
                System.out.println("Ти розгубився і не обрав жодних дверей. Вартовий чаклуна б'є тебе жезлом: -15 HP.");
            }
        }

        // ===== 4. Етап 3: Фінал та розв'язка =====
        System.out.println();
        System.out.println("==================================================");
        if (playerHp <= 0) {
            System.out.println("          ______");
            System.out.println("         |      |");
            System.out.println("         | R.I.P|");
            System.out.println("         |      |");
            System.out.println("       __|______|__");
            System.out.println("КІНЦІВКА: ПОРАЗКА");
            System.out.println("Герой загинув у темряві підземелля...");
        } else if (playerHp > 0 && obstacleCleared) {
            System.out.println("         .  *  .  *  .");
            System.out.println("       *   СВІТАНОК   *");
            System.out.println("         .  *  .  *  .");
            System.out.println("КІНЦІВКА: ТРІУМФАЛЬНА ПЕРЕМОГА");
            System.out.println("Ворота розчиняються, і герой виходить на світло сонця!");
        } else {
            System.out.println("КІНЦІВКА: ПОЛОН");
            System.out.println("Ти вижив, але не здолав жодної перешкоди. Чаклун знаходить тебе і замикає знову...");
        }

        String rank;
        if (playerHp <= 0) {
            rank = "F - Загинув у темряві";
        } else if (!obstacleCleared) {
            rank = "D - Бранець чаклуна";
        } else if (playerHp >= 100) {
            rank = "S - Легенда підземелля";
        } else if (playerHp >= 60) {
            rank = "A - Відважний герой";
        } else if (playerHp >= 30) {
            rank = "B - Той, хто вижив";
        } else {
            rank = "C - Ледь живий";
        }

        System.out.println("---------------- ПІДСУМОК ----------------");
        System.out.println("Герой:               " + playerName + " (" + className + ")");
        System.out.println("Залишок здоров'я:    " + Math.max(playerHp, 0) + " HP");
        System.out.println("Сила / Мана / Спр.:  " + strength + " / " + mana + " / " + agility);
        System.out.println("Артефакти:           " + artifacts);
        System.out.println("Фінальний ранг:      " + rank);
        System.out.println("==================================================");

        scanner.close();
    }
}