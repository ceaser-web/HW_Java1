package pw5;

import java.util.Locale;

public class Task4 {

    public static void main(String[] args) {
        // 1. Паралельні масиви статистики (індекс i = один і той самий гравець)
        String[] heroes = {"Pudge", "Invoker", "Juggernaut", "Crystal Maiden", "Earthshaker"};
        int[] kills = {8, 12, 10, 2, 4};
        int[] deaths = {6, 3, 2, 9, 5};
        int[] assists = {14, 9, 8, 18, 16};
        int[] netWorth = {14500, 21200, 24800, 9800, 12300};

        // 2. Індивідуальний KDA = (Kills + Assists) / max(1, Deaths)
        double[] kdaRatios = new double[5];
        for (int i = 0; i < heroes.length; i++) {
            kdaRatios[i] = (double) (kills[i] + assists[i]) / Math.max(1, deaths[i]);
        }

        // 3. Загальні показники команди
        int totalKills = 0;
        int totalDeaths = 0;
        int totalAssists = 0;
        int totalNetWorth = 0;
        for (int i = 0; i < heroes.length; i++) {
            totalKills += kills[i];
            totalDeaths += deaths[i];
            totalAssists += assists[i];
            totalNetWorth += netWorth[i];
        }

        // 4. Kill Participation % = (Kills + Assists) / TotalTeamKills * 100
        double[] killParticipation = new double[5];
        for (int i = 0; i < heroes.length; i++) {
            killParticipation[i] = totalKills > 0
                    ? (double) (kills[i] + assists[i]) / totalKills * 100
                    : 0.0;
        }

        // 5. MVP - гравець з найвищим KDA
        int mvpIndex = 0;
        for (int i = 1; i < heroes.length; i++) {
            if (kdaRatios[i] > kdaRatios[mvpIndex]) {
                mvpIndex = i;
            }
        }

        // 6. Фінальна таблиця (Scoreboard)
        String rowFormat = "| %-16s | %3s | %3s | %3s | %6s | %7s | %10s |%n";
        String separator = "-".repeat(69);

        System.out.println("=".repeat(69));
        System.out.println("        DOTA 2 - ПІСЛЯМАТЧЕВИЙ ЗВІТ (SCOREBOARD)");
        System.out.println("=".repeat(69));
        System.out.printf(Locale.US, rowFormat, "Герой", "K", "D", "A", "KDA", "KP %", "Net Worth");
        System.out.println(separator);

        for (int i = 0; i < heroes.length; i++) {
            String name = (i == mvpIndex ? "* " : "  ") + heroes[i];
            System.out.printf(Locale.US, rowFormat,
                    name,
                    kills[i],
                    deaths[i],
                    assists[i],
                    String.format(Locale.US, "%.2f", kdaRatios[i]),
                    String.format(Locale.US, "%.1f%%", killParticipation[i]),
                    String.format(Locale.US, "%,d", netWorth[i]));
        }

        System.out.println(separator);
        System.out.printf(Locale.US, rowFormat,
                "  РАЗОМ КОМАНДА",
                totalKills,
                totalDeaths,
                totalAssists,
                "",
                "",
                String.format(Locale.US, "%,d", totalNetWorth));
        System.out.println("=".repeat(69));

        System.out.println("Сумарна кількість вбивств команди: " + totalKills);
        System.out.println("Загальний командний капітал:        " + String.format(Locale.US, "%,d", totalNetWorth) + " золота");
        System.out.println();
        System.out.println("* MVP матчу: " + heroes[mvpIndex]
                + " (KDA " + String.format(Locale.US, "%.2f", kdaRatios[mvpIndex]) + ")");
    }
}
