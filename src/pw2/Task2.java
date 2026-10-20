package pw2;

public class Task2 {

    public static void main(String[] args) {
        // Вхідні дані: довільне тризначне ціле число
        int number = 742;

        // 1. Виділення цифр (тільки / та %)
        int hundreds = number / 100;        // 742 / 100 = 7
        int tens = (number / 10) % 10;      // 74 % 10 = 4
        int units = number % 10;            // 742 % 10 = 2

        // 2. Сума та добуток цифр
        int digitsSum = hundreds + tens + units;
        int digitsProduct = hundreds * tens * units;

        // 3. "Дзеркальне" число
        int reversedNumber = units * 100 + tens * 10 + hundreds;

        // 4. Чи парна сума цифр
        boolean isSumEven = digitsSum % 2 == 0;

        // 5. Звіт
        System.out.println("=========== АНАЛІЗ ТРИЗНАЧНОГО ЧИСЛА ===========");
        System.out.println("Число:               " + number);
        System.out.println("Сотні:               " + hundreds);
        System.out.println("Десятки:             " + tens);
        System.out.println("Одиниці:             " + units);
        System.out.println("--------------------------------------------------");
        System.out.println("Сума цифр:           " + digitsSum);
        System.out.println("Добуток цифр:        " + digitsProduct);
        System.out.println("Дзеркальне число:    " + reversedNumber);
        System.out.println("Сума цифр парна:     " + isSumEven);
        System.out.println("==================================================");
    }
}
