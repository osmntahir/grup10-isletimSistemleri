package job;

public class Color {
    // Text için renkler
    public static final String ANSI_RESET = "\u001B[0m";  // Sıfırlama rengi
    public static final String ANSI_BLACK = "\u001B[30m";  // Siyah renk
    public static final String ANSI_RED = "\u001B[31m";  // Kırmızı renk
    public static final String ANSI_GREEN = "\u001B[32m";  // Yeşil renk
    public static final String ANSI_YELLOW = "\u001B[33m";  // Sarı renk
    public static final String ANSI_BLUE = "\u001B[34m";  // Mavi renk
    public static final String ANSI_PURPLE = "\u001B[35m";  // Mor renk
    public static final String ANSI_CYAN = "\u001B[36m";  // Cyan (Camgöbeği) renk
    public static final String ANSI_WHITE = "\u001B[37m";  // Beyaz renk

    // Arkaplan renkleri
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";  // Siyah arkaplan
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";  // Kırmızı arkaplan
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";  // Yeşil arkaplan
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";  // Sarı arkaplan
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";  // Mavi arkaplan
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";  // Mor arkaplan
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";  // Cyan (Camgöbeği) arkaplan
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";  // Beyaz arkaplan

    // Belirli bir ID'ye göre rastgele renk döndüren metod
    public static String getRandomColor(int id) {
        switch (id) {
            case 0:
                return ANSI_RED;
            case 1:
                return ANSI_GREEN;
            case 2:
                return ANSI_YELLOW;
            case 3:
                return ANSI_BLUE;
            case 4:
                return ANSI_PURPLE;
            case 5:
                return ANSI_CYAN;
            case 6:
                return ANSI_CYAN;
            default:
                return ANSI_RED_BACKGROUND;
        }
    }
    
    // Renk atamasını gerçekleştiren metod
    public static void setColor(String color) {
        System.out.print(color);
    }
    
    // Rengi sıfırlayan metod
    public static void resetColor() {
        System.out.print(ANSI_RESET);
    }
}
