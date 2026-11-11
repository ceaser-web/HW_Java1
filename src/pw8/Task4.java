package pw8;

/**
 * Завдання 4: трирівнева ієрархія матриць та розв'язувач СЛАР.
 * Matrix2D -> SquareMatrix -> CramerMatrixSolver
 */
public class Task4 {

    public static void main(String[] args) {
        // ---------- Рівень 1: Matrix2D (прямокутна матриця 2x3) ----------
        printHeader("Рівень 1: Matrix2D");
        double[][] src = {
                {1, 2, 3},
                {4, 5, 6}
        };
        Matrix2D rect = new Matrix2D(src);
        src[0][0] = 999; // зовнішня зміна не повинна вплинути на матрицю (глибока копія)
        System.out.println(rect);
        System.out.println("Розмір: " + rect.getRows() + "x" + rect.getCols());
        System.out.println("Сума елементів: " + rect.getSum());
        System.out.println("Максимальний елемент: " + rect.getMaxElement());
        System.out.println("rect[0][0] після зміни вихідного масиву = " + rect.getElement(0, 0)
                + " (має бути 1.0 — захисне копіювання працює)");
        rect.setElement(1, 2, 10.5);
        System.out.println("Після setElement(1, 2, 10.5):\n" + rect);
        try {
            rect.getElement(5, 5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Перевірка меж: " + e.getMessage());
        }

        // ---------- Рівень 2: SquareMatrix ----------
        printHeader("Рівень 2: SquareMatrix 3x3 (симетрична)");
        SquareMatrix sym = new SquareMatrix(new double[][]{
                {2, -1, 0},
                {-1, 2, -1},
                {0, -1, 2}
        });
        System.out.println(sym);

        printHeader("Рівень 2: SquareMatrix 3x3 (несиметрична)");
        SquareMatrix general = new SquareMatrix(new double[][]{
                {1, 2, 3},
                {0, 1, 4},
                {5, 6, 0}
        });
        System.out.println(general);

        printHeader("Рівень 2: SquareMatrix 2x2");
        System.out.println(new SquareMatrix(new double[][]{{3, 1}, {2, 5}}));

        printHeader("Рівень 2: неквадратна матриця (попередження)");
        new SquareMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});

        // ---------- Рівень 3: CramerMatrixSolver ----------
        printHeader("Рівень 3: система 2x2  (2x + 3y = 8; 4x - y = 2)");
        CramerMatrixSolver s2 = new CramerMatrixSolver(
                new double[][]{{2, 3}, {4, -1}},
                new double[]{8, 2});
        System.out.println(s2);
        double[] r2 = s2.solve();
        System.out.println("solve() -> x = " + r2[0] + ", y = " + r2[1]);

        printHeader("Рівень 3: система 3x3  (x+y+z=6; 2y+5z=-4; 2x+5y-z=27)");
        CramerMatrixSolver s3 = new CramerMatrixSolver(
                new double[][]{{1, 1, 1}, {0, 2, 5}, {2, 5, -1}},
                new double[]{6, -4, 27});
        System.out.println(s3);

        printHeader("Рівень 3: вироджена система (Δ = 0)");
        CramerMatrixSolver bad = new CramerMatrixSolver(
                new double[][]{{1, 2}, {2, 4}},
                new double[]{3, 6});
        System.out.println(bad);
        double[] none = bad.solve();
        System.out.println("Довжина масиву розв'язків: " + none.length);
    }

    private static void printHeader(String title) {
        System.out.println("\n=== " + title + " ===");
    }
}

/** Рівень 1: довільна прямокутна матриця M x N. */
class Matrix2D {
    protected double[][] data;
    private int rows;
    private int cols;

    /** Створює глибоку копію переданого масиву. */
    public Matrix2D(double[][] source) {
        if (source == null || source.length == 0 || source[0] == null || source[0].length == 0) {
            throw new IllegalArgumentException("Матриця не може бути порожньою");
        }
        this.rows = source.length;
        this.cols = source[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (source[i] == null || source[i].length != cols) {
                throw new IllegalArgumentException("Усі рядки матриці повинні мати однакову довжину");
            }
            System.arraycopy(source[i], 0, data[i], 0, cols);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getElement(int r, int c) {
        checkBounds(r, c);
        return data[r][c];
    }

    public void setElement(int r, int c, double val) {
        checkBounds(r, c);
        data[r][c] = val;
    }

    private void checkBounds(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            throw new IndexOutOfBoundsException(
                    "Індекс [" + r + "][" + c + "] поза межами матриці " + rows + "x" + cols);
        }
    }

    public double getSum() {
        double sum = 0;
        for (double[] row : data) {
            for (double v : row) {
                sum += v;
            }
        }
        return sum;
    }

    public double getMaxElement() {
        double max = data[0][0];
        for (double[] row : data) {
            for (double v : row) {
                if (v > max) {
                    max = v;
                }
            }
        }
        return max;
    }

    /** Число з 2 знаками після коми (крапка як роздільник, без "-0.00"). */
    protected static String fmt(double value) {
        String s = String.format(java.util.Locale.US, "%.2f", value);
        return s.equals("-0.00") ? "0.00" : s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("| ");
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("%9s ", fmt(data[i][j])));
            }
            sb.append('|');
            if (i < rows - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}

/** Рівень 2: квадратна матриця N x N. */
class SquareMatrix extends Matrix2D {

    public SquareMatrix(double[][] source) {
        super(source);
        if (getRows() != getCols()) {
            System.out.println("Попередження: матриця " + getRows() + "x" + getCols() + " не є квадратною!");
        }
    }

    private boolean isSquare() {
        return getRows() == getCols();
    }

    public int getDimension() {
        return getRows();
    }

    /** Слід матриці — сума елементів головної діагоналі. */
    public double getTrace() {
        double trace = 0;
        int n = Math.min(getRows(), getCols());
        for (int i = 0; i < n; i++) {
            trace += data[i][i];
        }
        return trace;
    }

    public boolean isSymmetric() {
        if (!isSquare()) {
            return false;
        }
        int n = getDimension();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (data[i][j] != data[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Визначник: формула для 2x2, правило Саррюса для 3x3, інакше 0.0. */
    public double getDeterminant() {
        if (!isSquare()) {
            return 0.0;
        }
        int n = getDimension();
        if (n == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }
        if (n == 3) {
            return data[0][0] * data[1][1] * data[2][2]
                    + data[0][1] * data[1][2] * data[2][0]
                    + data[0][2] * data[1][0] * data[2][1]
                    - data[0][2] * data[1][1] * data[2][0]
                    - data[0][0] * data[1][2] * data[2][1]
                    - data[0][1] * data[1][0] * data[2][2];
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nСлід: " + fmt(getTrace())
                + " | Симетрична: " + (isSymmetric() ? "так" : "ні")
                + " | det(A) = " + fmt(getDeterminant());
    }
}

/** Рівень 3: СЛАР [A|B], яка розв'язується методом Крамера (2x2 або 3x3). */
class CramerMatrixSolver extends SquareMatrix {
    private static final double EPS = 1e-9;

    private double[] freeTerms;

    public CramerMatrixSolver(double[][] coefficients, double[] freeTerms) {
        super(coefficients);
        if (freeTerms == null || freeTerms.length != getRows()) {
            throw new IllegalArgumentException(
                    "Кількість вільних членів має дорівнювати кількості рівнянь (" + getRows() + ")");
        }
        this.freeTerms = freeTerms.clone();
    }

    private boolean isSupportedSystem() {
        int n = getDimension();
        return getRows() == getCols() && (n == 2 || n == 3);
    }

    /** Знаходить корені за формулами Крамера: x_k = Δ_k / Δ. */
    private double[] computeRoots(double delta) {
        int n = getDimension();
        double[] roots = new double[n];
        for (int k = 0; k < n; k++) {
            // копія A, де k-й стовпчик замінено на вільні члени
            double[][] modified = new double[n][];
            for (int i = 0; i < n; i++) {
                modified[i] = data[i].clone();
                modified[i][k] = freeTerms[i];
            }
            roots[k] = new SquareMatrix(modified).getDeterminant() / delta;
        }
        return roots;
    }

    public double[] solve() {
        if (!isSupportedSystem()) {
            System.out.println("Метод Крамера тут реалізовано лише для систем 2x2 та 3x3.");
            return new double[0];
        }
        double delta = getDeterminant();
        if (Math.abs(delta) < EPS) {
            System.out.println("Єдиного розв'язку немає (Δ = 0).");
            return new double[0];
        }
        return computeRoots(delta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Розширена матриця [A|B]:\n");
        for (int i = 0; i < getRows(); i++) {
            sb.append("| ");
            for (int j = 0; j < getCols(); j++) {
                sb.append(String.format("%9s ", fmt(data[i][j])));
            }
            sb.append("| ").append(String.format("%9s", fmt(freeTerms[i]))).append(" |\n");
        }

        double delta = getDeterminant();
        sb.append("Визначник Δ = ").append(fmt(delta)).append('\n');

        if (!isSupportedSystem() || Math.abs(delta) < EPS) {
            sb.append("Корені: єдиного розв'язку немає");
        } else {
            double[] roots = computeRoots(delta);
            sb.append("Корені: ");
            for (int i = 0; i < roots.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append("x").append(i + 1).append(" = ").append(fmt(roots[i]));
            }
        }
        return sb.toString();
    }
}