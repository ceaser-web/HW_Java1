package pw9;

/**
 * Завдання 5: «Cyberpunk 2077: Лабораторія Ріпердока, Кібер-Імпланти та Межа Кіберпсихозу».
 *
 * Архітектура: CyberMercenary (композиція) HAS-A BioMonitor + HAS-MANY AbstractCyberImplant[].
 */
public class Task5 {

    public static void main(String[] args) {
        CyberMercenary david = new CyberMercenary("Девід Мартінес", 40, 400);

        // ================== КЛІНІКА ВІКТОРА ВЕКТОРА ==================
        banner("КЛІНІКА ВІКТОРА ВЕКТОРА: ВСТАНОВЛЕННЯ ІМПЛАНТІВ");
        System.out.println("Девід лягає на кушетку. Віктор розкладає інструменти...\n");
        System.out.println(david);

        System.out.println("\n>>> Встановлюємо Сандевістан:");
        david.installImplant(new SandevistanOs());

        System.out.println("\n>>> Встановлюємо Термальні Клинки Богомола:");
        david.installImplant(new MantisBlades());

        System.out.println("\n>>> Спроба поставити ДРУГУ Нейро-ОС (дублюючий слот):");
        boolean duplicate = david.installImplant(new SandevistanOs());
        System.out.println("Результат установки: " + duplicate + " (слот зайнятий — Віктор не дозволяє!)");

        System.out.println("\n>>> Встановлюємо Підшкірну Титанову Броню:");
        david.installImplant(new SubdermalTitaniumArmor());
        System.out.println("\nЛюдяність рівно на межі (20) — ще один крок, і почнеться кіберпсихоз!\n");
        System.out.println(david);

        System.out.println("\n>>> Девід наполягає на ще одному апгрейді — Кіроші Оптика (слот «Оптика»):");
        david.installImplant(new KiroshiOptics());
        System.out.println("\n☠ МЕЖУ ПЕРЕЙДЕНО! Кіберпсихоз активний: атака зросла на 50%, але нерви горять.\n");
        System.out.println(david);

        System.out.println("\n>>> Віктор ловить Девіда й вводить імуноблокатори (+30 людяності):");
        david.injectImmunoblockers(30);
        System.out.println(david);

        // ================== БІЙ З АДАМОМ СМЕШЕРОМ ==================
        banner("РЕЙД НА АРАСАКУ: ДЕВІД МАРТІНЕС VS АДАМ СМЕШЕР");
        AdamSmasher smasher = new AdamSmasher(900, 100);
        System.out.println(smasher);

        for (int round = 1; round <= 4 && david.isAlive() && smasher.isAlive(); round++) {
            System.out.println("\n──────────── РАУНД " + round + " ────────────");

            if (round == 2) {
                System.out.println("Смешер дужчий, ніж здавалося. Девід вимикає всі запобіжники!");
                david.triggerFullOverdrive();
            }

            // Хід Девіда
            int dealt = smasher.takeHit(david.getTotalAttack());
            System.out.println("Девід б'є з атакою " + david.getTotalAttack()
                    + " → Смешер втрачає " + dealt + " HP.");
            if (!smasher.isAlive()) {
                break;
            }

            // Контратака Смешера
            int raw = smasher.getAttackDamage(round);
            int taken = david.takeHit(raw);
            System.out.println("Смешер: «" + smasher.getAttackName(round) + "» (" + raw
                    + " сирого урону) → після броні Девід втрачає " + taken + " HP.");

            // Кінець ходу: перевантаження нервової системи
            int overload = david.endTurn();
            if (overload > 0) {
                System.out.println("☠ Кіберпсихоз: перевантаження нервів забирає ще " + overload + " HP!");
            }

            System.out.println();
            System.out.println(david);
            System.out.println(smasher);
        }

        // ================== ФІНАЛ ==================
        banner("ПІДСУМОК");
        if (!david.isAlive()) {
            System.out.println("Девід падає на асфальт... Смешер переміг. Ще одна легенда Нічного Міста.");
        } else if (!smasher.isAlive()) {
            System.out.println("Смешер розвалюється на іскри! Девід Мартінес зробив неможливе — і ще дихає.");
        } else {
            System.out.println("Обидва ледь тримаються на ногах — бій триває за кадром.");
        }
        System.out.println();
        System.out.println(david);
        System.out.println(smasher);
    }

    private static void banner(String title) {
        System.out.println("\n==================================================");
        System.out.println(" " + title);
        System.out.println("==================================================");
    }
}

/**
 * Інкапсульований компонент життєзабезпечення. Жодних публічних сеттерів —
 * стан змінюється лише через контрольовані бізнес-методи.
 */
class BioMonitor {
    private static final int MAX_HUMANITY = 100;
    private static final int PSYCHOSIS_THRESHOLD = 20;
    private static final int MIN_DAMAGE = 5;
    private static final int BAR_WIDTH = 10;

    private int hp;
    private final int maxHp;
    private int humanity = MAX_HUMANITY;
    private boolean cyberpsychosis = false;

    BioMonitor(int maxHp) {
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp має бути додатним");
        }
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    /** Зменшує людяність (не нижче 0). Якщо humanity < 20 — вмикається кіберпсихоз. */
    public void reduceHumanity(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost не може бути від'ємним");
        }
        humanity = Math.max(0, humanity - cost);
        if (humanity < PSYCHOSIS_THRESHOLD) {
            cyberpsychosis = true;
        }
    }

    /** Підвищує людяність (не вище 100). Якщо humanity >= 20 — кіберпсихоз знімається. */
    public void injectImmunoblockers(int restoreAmount) {
        if (restoreAmount < 0) {
            throw new IllegalArgumentException("restoreAmount не може бути від'ємним");
        }
        humanity = Math.min(MAX_HUMANITY, humanity + restoreAmount);
        if (humanity >= PSYCHOSIS_THRESHOLD) {
            cyberpsychosis = false;
        }
    }

    /** Віднімає від HP Math.max(5, rawDamage - totalArmor). Повертає фактично втрачені HP. */
    public int applyDamage(int rawDamage, int totalArmor) {
        int damage = Math.max(MIN_DAMAGE, rawDamage - totalArmor);
        int actual = Math.min(damage, hp); // HP не опускається нижче 0
        hp -= actual;
        return actual;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHumanity() {
        return humanity;
    }

    public boolean isCyberpsychosis() {
        return cyberpsychosis;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    /** ASCII-шкала виду [████████░░]. */
    static String renderBar(int value, int max) {
        int filled = value <= 0 ? 0 : (int) Math.ceil((double) value * BAR_WIDTH / max);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < BAR_WIDTH; i++) {
            sb.append(i < filled ? '█' : '░');
        }
        return sb.append(']').toString();
    }

    @Override
    public String toString() {
        String state = cyberpsychosis ? "!!! КІБЕРПСИХОЗ !!!" : "стабільний";
        return "HP:       " + renderBar(hp, maxHp) + " " + hp + "/" + maxHp + "\n"
                + "Humanity: " + renderBar(humanity, MAX_HUMANITY) + " " + humanity + "/" + MAX_HUMANITY
                + "  " + state;
    }
}

/** Контракт кібер-імпланта: кожен конкретний імплант сам визначає свої бонуси та ефект. */
abstract class AbstractCyberImplant {
    private final String name;
    private final String slot;
    private final int humanityCost;

    protected AbstractCyberImplant(String name, String slot, int humanityCost) {
        this.name = name;
        this.slot = slot;
        this.humanityCost = humanityCost;
    }

    public String getName() {
        return name;
    }

    public String getSlot() {
        return slot;
    }

    public int getHumanityCost() {
        return humanityCost;
    }

    public abstract int getAttackBonus();

    public abstract int getArmorBonus();

    public abstract String activateSpecialAbility();

    @Override
    public String toString() {
        return String.format("[%s] %s | -%d людяності | АТК +%d | БРН +%d",
                slot, name, humanityCost, getAttackBonus(), getArmorBonus());
    }
}

class SandevistanOs extends AbstractCyberImplant {
    SandevistanOs() {
        super("Військовий Сандевістан Militech Apogee", "Нейро-ОС", 35);
    }

    @Override
    public int getAttackBonus() {
        return 90;
    }

    @Override
    public int getArmorBonus() {
        return 20;
    }

    @Override
    public String activateSpecialAbility() {
        return "⚡ САНДЕВІСТАН АКТИВОВАНО: Час сповільнено на 85%! Вороги завмерли на місці!";
    }
}

class MantisBlades extends AbstractCyberImplant {
    MantisBlades() {
        super("Термальні Клинки Богомола", "Руки", 25);
    }

    @Override
    public int getAttackBonus() {
        return 110;
    }

    @Override
    public int getArmorBonus() {
        return 5;
    }

    @Override
    public String activateSpecialAbility() {
        return "🔥 Вихор розпечених клинків Богомола розтинає броню ворога!";
    }
}

class SubdermalTitaniumArmor extends AbstractCyberImplant {
    SubdermalTitaniumArmor() {
        super("Підшкірна Титанова Броня", "Шкіра", 20);
    }

    @Override
    public int getAttackBonus() {
        return 10;
    }

    @Override
    public int getArmorBonus() {
        return 65;
    }

    @Override
    public String activateSpecialAbility() {
        return "🛡️ Нано-пластини поглинають кінетичний удар!";
    }
}

/** Четвертий імплант — для заповнення останнього слота масиву. */
class KiroshiOptics extends AbstractCyberImplant {
    KiroshiOptics() {
        super("Кіроші Оптика Mk.3", "Оптика", 10);
    }

    @Override
    public int getAttackBonus() {
        return 25;
    }

    @Override
    public int getArmorBonus() {
        return 5;
    }

    @Override
    public String activateSpecialAbility() {
        return "👁 Кіроші підсвічує слабкі місця: критична зона ворога на прицілі!";
    }
}

/** Кібер-найманець: БІОМОНІТОР створюється всередині (композиція), імпланти — у масиві слотів. */
class CyberMercenary {
    private static final int BASE_ARMOR = 10;
    private static final int OVERDRIVE_HUMANITY_COST = 25;

    private final String nickname;
    private final int baseAttack;
    private final BioMonitor bioMonitor;
    private final AbstractCyberImplant[] implants = new AbstractCyberImplant[4];
    private int implantCount;

    public CyberMercenary(String nickname, int baseAttack, int maxHp) {
        this.nickname = nickname;
        this.baseAttack = baseAttack;
        this.bioMonitor = new BioMonitor(maxHp); // створюється всередині конструктора
    }

    /** Метод Ріпердока: один слот — один імплант; встановлення знижує людяність. */
    public boolean installImplant(AbstractCyberImplant implant) {
        if (implant == null) {
            return false;
        }
        if (implantCount >= implants.length) {
            System.out.println("❌ Усі " + implants.length + " слоти зайняті!");
            return false;
        }
        for (int i = 0; i < implantCount; i++) {
            if (implants[i].getSlot().equalsIgnoreCase(implant.getSlot())) {
                System.out.println("❌ Слот «" + implant.getSlot() + "» вже зайнятий: "
                        + implants[i].getName() + ". Встановити «" + implant.getName() + "» неможливо!");
                return false;
            }
        }
        implants[implantCount++] = implant;
        bioMonitor.reduceHumanity(implant.getHumanityCost());
        System.out.println("✅ Встановлено: " + implant);
        return true;
    }

    /** Сума бази та бонусів усіх імплантів, без урахування кіберпсихозу. */
    private int rawAttack() {
        int total = baseAttack;
        for (int i = 0; i < implantCount; i++) {
            total += implants[i].getAttackBonus();
        }
        return total;
    }

    /** При кіберпсихозі загальна атака зростає на 50%. */
    public int getTotalAttack() {
        int total = rawAttack();
        return bioMonitor.isCyberpsychosis() ? (int) Math.round(total * 1.5) : total;
    }

    public int getTotalArmor() {
        int total = BASE_ARMOR;
        for (int i = 0; i < implantCount; i++) {
            total += implants[i].getArmorBonus();
        }
        return total;
    }

    /** Повний овердрайв: активує спецефекти всіх імплантів (поліморфізм) ціною людяності. */
    public void triggerFullOverdrive() {
        System.out.println("⚙ ПОВНИЙ ОВЕРДРАЙВ: " + nickname + " вимикає всі запобіжники!");
        for (int i = 0; i < implantCount; i++) {
            System.out.println("   " + implants[i].activateSpecialAbility());
        }
        bioMonitor.reduceHumanity(OVERDRIVE_HUMANITY_COST);
        System.out.println("   Нервова система перевантажена: -" + OVERDRIVE_HUMANITY_COST + " людяності.");
    }

    public void injectImmunoblockers(int amount) {
        bioMonitor.injectImmunoblockers(amount);
    }

    public int takeHit(int rawDamage) {
        return bioMonitor.applyDamage(rawDamage, getTotalArmor());
    }

    /**
     * Кінець ходу: при кіберпсихозі найманець втрачає 10% власного (поточного) HP.
     * Повертає втрачені HP (0, якщо кіберпсихозу немає).
     */
    public int endTurn() {
        if (!bioMonitor.isCyberpsychosis() || !bioMonitor.isAlive()) {
            return 0;
        }
        return bioMonitor.applyDamage(bioMonitor.getHp() / 10, 0);
    }

    public boolean isAlive() {
        return bioMonitor.isAlive();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══ КІБЕР-ДЕКА: ").append(nickname).append(" ═══\n");
        sb.append("║ Атака: ").append(getTotalAttack());
        if (bioMonitor.isCyberpsychosis()) {
            sb.append(" (база ").append(rawAttack()).append(" +50% кіберпсихоз)");
        }
        sb.append(" | Броня: ").append(getTotalArmor()).append('\n');
        sb.append("║ ").append(bioMonitor.toString().replace("\n", "\n║ ")).append('\n');
        sb.append("║ Імпланти (").append(implantCount).append('/').append(implants.length).append("):\n");
        if (implantCount == 0) {
            sb.append("║   (порожньо)\n");
        }
        for (int i = 0; i < implantCount; i++) {
            sb.append("║   ").append(i + 1).append(") ").append(implants[i]).append('\n');
        }
        sb.append("╚══════════════════════════");
        return sb.toString();
    }
}

/** Бос рейду — кіборг Арасаки Адам Смешер. */
class AdamSmasher {
    private static final String[] ATTACK_NAMES = {
            "Ракетний залп із плеча",
            "Удар лівою рукою-гідравлікою",
            "Шквал із вбудованого міномета",
            "Крапкова екзекуція з мінігана"
    };
    private static final int[] ATTACK_DAMAGE = {150, 190, 230, 270};

    private final int maxHp;
    private final int armor;
    private int hp;

    AdamSmasher(int maxHp, int armor) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.armor = armor;
    }

    int takeHit(int rawDamage) {
        int damage = Math.max(1, rawDamage - armor);
        int actual = Math.min(damage, hp);
        hp -= actual;
        return actual;
    }

    boolean isAlive() {
        return hp > 0;
    }

    String getAttackName(int round) {
        return ATTACK_NAMES[(round - 1) % ATTACK_NAMES.length];
    }

    int getAttackDamage(int round) {
        return ATTACK_DAMAGE[(round - 1) % ATTACK_DAMAGE.length];
    }

    @Override
    public String toString() {
        return "АДАМ СМЕШЕР (броня " + armor + ")  HP: "
                + BioMonitor.renderBar(hp, maxHp) + " " + hp + "/" + maxHp;
    }
}