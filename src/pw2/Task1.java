package pw2;

import java.util.Locale;

public class Task1 {
    public static void main(String[] args) {
        // Вхідні дані
        double usdAmount = 250.0;
        final double EXCHANGE_RATE = 41.25;
        final double COMMISSION_PERCENT = 1.5; // 1.5%

        // 1. Загальна сума в гривнях до комісії
        double totalUah = usdAmount * EXCHANGE_RATE;

        // 2. Комісія банку
        double commission = totalUah * (COMMISSION_PERCENT / 100);

        // 3. Чиста сума до видачі
        double netAmount = totalUah - commission;

        // 4. Округлення до цілого числа гривень (видача готівкою без монет)
        long cashAmount = Math.round(netAmount);

        // 5. Квитанція (конкатенація; Locale.US — щоб розділювач був крапкою)
        String line = "==================================================";
        String dash = "--------------------------------------------------";

        System.out.println("================ КВИТАНЦІЯ ОБМІНУ ================");
        System.out.println("Сума до обміну:            " + String.format(Locale.US, "%.2f", usdAmount) + " USD");
        System.out.println("Курс валют:                " + String.format(Locale.US, "%.2f", EXCHANGE_RATE) + " UAH/USD");
        System.out.println("Нараховано до комісії:     " + String.format(Locale.US, "%.2f", totalUah) + " UAH");
        System.out.println("Комісія банку (" + COMMISSION_PERCENT + "%):      " + String.format(Locale.US, "%.2f", commission) + " UAH");
        System.out.println(dash);
        System.out.println("Чиста сума до видачі:      " + String.format(Locale.US, "%.2f", netAmount) + " UAH");
        System.out.println("До видачі готівкою:        " + cashAmount + " UAH");
        System.out.println(line);
    }


}
