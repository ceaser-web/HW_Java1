package pw10;

/**
 * Завдання 5: «Відьмак 3: Дикий Гін — Бестіарій Монстрів, Інтерфейси Здатностей та Поліморфне Полювання».
 *
 * AbstractMonster (IS-A) + інтерфейси здатностей (CAN-DO) + WitcherHunter з Pattern Matching instanceof.
 * Потрібна Java 16+ (instanceof з оголошенням змінної).
 */
public class Task5 {

    private static final int MAX_ROUNDS = 6;

    public static void main(String[] args) {
        WitcherHunter geralt = new WitcherHunter("Ґеральт із Рівії", "Школа Вовка", 150, 30, 500);

        // Поліморфний масив замовлень з дошки оголошень
        AbstractMonster[] contracts = {
                new RoyalGriffin(),
                new Noonwraith(),
                new HigherVampire(),
                new AncientLeshen()
        };

        banner("ДОШКА ОГОЛОШЕНЬ: ЗАМОВЛЕННЯ НА МОНСТРІВ");
        for (int i = 0; i < contracts.length; i++) {
            System.out.println((i + 1) + ") " + contracts[i]);
        }
        System.out.println("\n" + geralt);

        int completed = 0;
        for (int i = 0; i < contracts.length; i++) {
            if (hunt(geralt, contracts[i], i + 1)) {
                completed++;
            }
            if (!geralt.isAlive()) {
                break;
            }
            geralt.meditate();
        }

        banner("КОНТРОЛЬНИЙ ЕКСПЕРИМЕНТ: ЩО БУЛО Б БЕЗ ТАКТИКИ БЕСТІАРІЮ");
        int power = geralt.getStrikePower();
        System.out.println("Свіжі монстри отримують «сліпий» удар силою " + power + " (без Аарда, Ірден та пилу):\n");
        AbstractMonster[] fresh = {new RoyalGriffin(), new Noonwraith(), new HigherVampire()};
        for (AbstractMonster m : fresh) {
            int before = m.getHp();
            m.receiveDamage(power);
            int dealt = before - m.getHp();
            int healed = (m instanceof RegeneratingMonster rm) ? rm.regenerateHealth() : 0;
            System.out.printf("%-24s отримав лише %3d з %d", m.getName(), dealt, power);
            if (healed > 0) {
                System.out.print(", а потім регенерував +" + healed);
            }
            System.out.println();
        }

        banner("ПІДСУМОК ПОЛЮВАННЯ");
        System.out.println("Виконано замовлень: " + completed + " з " + contracts.length);
        System.out.println(geralt);
    }

    /** Один контракт: раунди бою Відьмака проти монстра. Повертає true, якщо монстра вбито. */
    private static boolean hunt(WitcherHunter geralt, AbstractMonster monster, int number) {
        banner("КОНТРАКТ №" + number + ": " + monster.getName().toUpperCase());
        System.out.println(monster);
        System.out.println("Бойовий клич: «" + monster.getBattleCry() + "»");

        for (int round = 1; round <= MAX_ROUNDS && monster.isAlive() && geralt.isAlive(); round++) {
            System.out.println("\n-- Раунд " + round + " --");

            int dealt = geralt.executeSilverStrike(monster);
            System.out.println("⚔ Срібний меч (сила " + geralt.getStrikePower() + ") завдає реальної шкоди: " + dealt);
            System.out.println("   " + monster);
            if (!monster.isAlive()) {
                break;
            }

            // Хід монстра: регенерація (якщо вміє) та атака
            if (monster instanceof RegeneratingMonster rm) {
                if (rm.isRegenerationBlocked()) {
                    System.out.println("   🌙 Регенерацію придушено Місячним пилом.");
                } else {
                    System.out.println("   ✚ Монстр регенерує +" + rm.regenerateHealth() + " HP.");
                }
            }
            int attack = monster.calculateMonsterAttack();
            geralt.receiveHit(attack);
            System.out.println("   Монстр атакує із силою " + attack + ".");
            System.out.println("   " + geralt);
        }

        boolean killed = !monster.isAlive();
        System.out.println(killed
                ? "\n✔ Контракт виконано! Монстра знищено."
                : "\n✘ Контракт провалено...");
        return killed;
    }

    private static void banner(String title) {
        System.out.println("\n==================================================");
        System.out.println(" " + title);
        System.out.println("==================================================");
    }
}

// ======================= ІНТЕРФЕЙСИ ЗДАТНОСТЕЙ (CAN-DO) =======================

/** Літаюча почвара: Аард збиває її на землю, позбавляючи бонусу ухилення. */
interface FlyingMonster {
    boolean isAirborne();

    void knockDownToGround();
}

/** Регенеруюча істота: бомба «Місячний пил» блокує регенерацію. */
interface RegeneratingMonster {
    int regenerateHealth();

    void applyMoonDustBomb();

    /** Допоміжний default-метод, щоб Відьмак не кидав бомбу повторно; за замовчуванням регенерація активна. */
    default boolean isRegenerationBlocked() {
        return false;
    }
}

/** Примарна істота: безтілесний привид поглинає 80% шкоди, поки не пов'язаний знаком Ірден. */
interface SpectralPhantom {
    boolean isIncorporeal();

    void bindWithYrdenSign();
}

// ======================= АБСТРАКТНИЙ МОНСТР (IS-A) =======================

abstract class AbstractMonster {
    private final String name;
    private final String bestiaryCategory;
    protected int hp;
    protected final int maxHp;

    protected AbstractMonster(String name, String bestiaryCategory, int maxHp) {
        this.name = name;
        this.bestiaryCategory = bestiaryCategory;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public String getName() {
        return name;
    }

    public String getBestiaryCategory() {
        return bestiaryCategory;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public abstract int calculateMonsterAttack();

    public abstract String getBattleCry();

    public final boolean isAlive() {
        return hp > 0;
    }

    /** Базове отримання шкоди; підкласи можуть змінювати вхідну шкоду й викликати super. */
    public void receiveDamage(int dmg) {
        hp = Math.max(0, hp - Math.max(0, dmg));
    }

    /** ASCII-шкала виду [██████░░░░]. */
    static String renderBar(int value, int max, int width) {
        int filled = value <= 0 ? 0 : (int) Math.ceil((double) value * width / max);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < filled ? '█' : '░');
        }
        return sb.append(']').toString();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) HP %s %d/%d%s",
                name, bestiaryCategory, renderBar(hp, maxHp, 20), hp, maxHp, isAlive() ? "" : " ☠");
    }
}

// ======================= КОНКРЕТНІ МОНСТРИ =======================

/** Королівський грифон: у повітрі отримує вдвічі менше шкоди від меча. */
class RoyalGriffin extends AbstractMonster implements FlyingMonster {
    private boolean airborne = true;

    RoyalGriffin() {
        super("Королівський Грифон", "Гібриди", 400);
    }

    @Override
    public boolean isAirborne() {
        return airborne;
    }

    @Override
    public void knockDownToGround() {
        airborne = false;
    }

    @Override
    public int calculateMonsterAttack() {
        return airborne ? 90 : 60; // пікірування сильніше за наземну атаку
    }

    @Override
    public String getBattleCry() {
        return "Кррріііі! Грифон здіймається в небо та готується до пікірування!";
    }

    @Override
    public void receiveDamage(int dmg) {
        super.receiveDamage(airborne ? dmg / 2 : dmg);
    }
}

/** Полудниця: безтілесна, поки не потрапила в коло Ірден (поглинає 80% шкоди). */
class Noonwraith extends AbstractMonster implements SpectralPhantom {
    private boolean incorporeal = true;

    Noonwraith() {
        super("Полудниця", "Привиди", 400);
    }

    @Override
    public boolean isIncorporeal() {
        return incorporeal;
    }

    @Override
    public void bindWithYrdenSign() {
        incorporeal = false;
    }

    @Override
    public int calculateMonsterAttack() {
        return incorporeal ? 70 : 50;
    }

    @Override
    public String getBattleCry() {
        return "Іііі-і-і! Полудниця розчиняється в мареві спеки...";
    }

    @Override
    public void receiveDamage(int dmg) {
        super.receiveDamage(incorporeal ? dmg * 20 / 100 : dmg); // поглинає 80%
    }
}

/** Вищий вампір Детлафф: множинна реалізація — туман (політ) + регенерація +120 HP щоходу. */
class HigherVampire extends AbstractMonster implements FlyingMonster, RegeneratingMonster {
    private static final int REGEN_PER_TURN = 120;

    private boolean airborne = true;       // у формі туману
    private boolean regenBlocked = false;

    HigherVampire() {
        super("Вищий Вампір Детлафф", "Вампіри", 600);
    }

    @Override
    public boolean isAirborne() {
        return airborne;
    }

    @Override
    public void knockDownToGround() {
        airborne = false;
    }

    @Override
    public int regenerateHealth() {
        if (regenBlocked || !isAlive()) {
            return 0;
        }
        int healed = Math.min(REGEN_PER_TURN, maxHp - hp);
        hp += healed;
        return healed;
    }

    @Override
    public void applyMoonDustBomb() {
        regenBlocked = true;
    }

    @Override
    public boolean isRegenerationBlocked() {
        return regenBlocked;
    }

    @Override
    public int calculateMonsterAttack() {
        return airborne ? 130 : 100;
    }

    @Override
    public String getBattleCry() {
        return "Ти прийшов за кров'ю брата? Тоді тобі доведеться пролити свою!";
    }

    @Override
    public void receiveDamage(int dmg) {
        super.receiveDamage(airborne ? dmg / 2 : dmg); // у формі туману шкода вдвічі менша
    }
}

/** Предавній Лісовик — final-клас (вершина еволюції реліктів). Регенерація залежить від тотемів. */
final class AncientLeshen extends AbstractMonster implements RegeneratingMonster {
    private static final int REGEN_PER_TOTEM = 20;

    private final int forestTotems = 3;
    private boolean regenBlocked = false;

    AncientLeshen() {
        super("Предавній Лісовик", "Реліктові", 540);
    }

    @Override
    public int regenerateHealth() {
        if (regenBlocked || !isAlive()) {
            return 0;
        }
        int healed = Math.min(forestTotems * REGEN_PER_TOTEM, maxHp - hp);
        hp += healed;
        return healed;
    }

    @Override
    public void applyMoonDustBomb() {
        regenBlocked = true;
    }

    @Override
    public boolean isRegenerationBlocked() {
        return regenBlocked;
    }

    @Override
    public int calculateMonsterAttack() {
        return 65 + forestTotems * 10;
    }

    @Override
    public String getBattleCry() {
        return "Ліс здригається — корені й круки виконують волю Лісовика!";
    }
}

// ======================= МИСЛИВЕЦЬ =======================

class WitcherHunter {
    private final String name;
    private final String schoolName;
    private final int silverSwordAttack;
    private final int signIntensity;
    private final int maxVitality;
    private int vitality;

    WitcherHunter(String name, String schoolName, int silverSwordAttack, int signIntensity, int maxVitality) {
        this.name = name;
        this.schoolName = schoolName;
        this.silverSwordAttack = silverSwordAttack;
        this.signIntensity = signIntensity;
        this.maxVitality = maxVitality;
        this.vitality = maxVitality;
    }

    /** Тактичний аналіз бестіарію через Pattern Matching instanceof. */
    public void applyBestiaryTactics(AbstractMonster target) {
        if (target instanceof SpectralPhantom sp && sp.isIncorporeal()) {
            System.out.println("   🔮 " + name + " кидає знак Ірден — безтілесність «" + target.getName() + "» зруйновано!");
            sp.bindWithYrdenSign();
        }
        if (target instanceof FlyingMonster fm && fm.isAirborne()) {
            System.out.println("   💨 " + name + " б'є знаком Аард — «" + target.getName() + "» скинуто на землю!");
            fm.knockDownToGround();
        }
        if (target instanceof RegeneratingMonster rm && !rm.isRegenerationBlocked()) {
            System.out.println("   🌙 " + name + " кидає бомбу «Місячний пил» — регенерацію «" + target.getName() + "» придушено!");
            rm.applyMoonDustBomb();
        }
    }

    /** Фінальний метод удару: спочатку тактика бестіарію, потім потужний удар срібним мечем. */
    public final int executeSilverStrike(AbstractMonster target) {
        int hpBefore = target.getHp();
        applyBestiaryTactics(target);
        target.receiveDamage(getStrikePower());
        return hpBefore - target.getHp();
    }

    public int getStrikePower() {
        return silverSwordAttack + signIntensity;
    }

    public void receiveHit(int dmg) {
        vitality = Math.max(0, vitality - Math.max(0, dmg));
    }

    /** Медитація між контрактами відновлює здоров'я. */
    public void meditate() {
        vitality = maxVitality;
    }

    public boolean isAlive() {
        return vitality > 0;
    }

    @Override
    public String toString() {
        return name + " (" + schoolName + ") HP " + AbstractMonster.renderBar(vitality, maxVitality, 20)
                + " " + vitality + "/" + maxVitality;
    }
}
