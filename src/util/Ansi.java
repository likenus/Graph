package src.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Usage:
 * <li>String msg = Ansi.Red.and(Ansi.BgYellow).format("Hello %s", name)</li>
 * <li>String msg = Ansi.Blink.colorize("BOOM!")</li>
 *
 * Or, if you are adverse to that, you can use the constants directly:
 * <li>String msg = new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Green money")</li>
 * Or, even:
 * <li>String msg = Ansi.BLUE + "scientific"</li>
 *
 * NOTE: Nothing stops you from combining multiple FG colors or BG colors,
 *       but only the last one will display.
 *
 * @author dain
 * @author sn00py1310
 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#SGR_(Select_Graphic_Rendition)_parameters">Wikipedia Ansi Colors</a>
 * @link <a href="https://gist.github.com/dainkaplan/4651352">Original Source</a>
 * @version 1.0
 */
public final class Ansi {

    // Color code strings from:
    // http://www.topmudsites.com/forums/mud-coding/413-java-ansi.html

    public static final Ansi HighIntensity = new Ansi(Code.HIGH_INTENSITY);
    public static final Ansi Bold = HighIntensity;
    public static final Ansi LowIntensity = new Ansi(Code.LOW_INTENSITY);
    public static final Ansi Normal = LowIntensity;

    public static final Ansi Italic = new Ansi(Code.ITALIC);
    public static final Ansi Underline = new Ansi(Code.UNDERLINE);
    public static final Ansi Blink = new Ansi(Code.BLINK);
    public static final Ansi RapidBlink = new Ansi(Code.RAPID_BLINK);

    public static final Ansi Black = new Ansi(Code.BLACK);
    public static final Ansi Red = new Ansi(Code.RED);
    public static final Ansi Green = new Ansi(Code.GREEN);
    public static final Ansi Yellow = new Ansi(Code.YELLOW);
    public static final Ansi Blue = new Ansi(Code.BLUE);
    public static final Ansi Magenta = new Ansi(Code.MAGENTA);
    public static final Ansi Cyan = new Ansi(Code.CYAN);
    public static final Ansi White = new Ansi(Code.WHITE);

    public static final Ansi BgBlack = new Ansi(Code.BACKGROUND_BLACK);
    public static final Ansi BgRed = new Ansi(Code.BACKGROUND_RED);
    public static final Ansi BgGreen = new Ansi(Code.BACKGROUND_GREEN);
    public static final Ansi BgYellow = new Ansi(Code.BACKGROUND_YELLOW);
    public static final Ansi BgBlue = new Ansi(Code.BACKGROUND_BLUE);
    public static final Ansi BgMagenta = new Ansi(Code.BACKGROUND_MAGENTA);
    public static final Ansi BgCyan = new Ansi(Code.BACKGROUND_CYAN);
    public static final Ansi BgWhite = new Ansi(Code.BACKGROUND_WHITE);

    private final Code[] codes;
    public Ansi(Code... codes) {
        this.codes = codes;
    }

    public Ansi and(Ansi other) {
        List<Code> both = new ArrayList<>();
        Collections.addAll(both, this.codes);
        Collections.addAll(both, other.codes);
        return new Ansi(both.toArray(new Code[] {}));
    }

    public Ansi and(Code... codes){
        return this.and(new Ansi(codes));
    }

    public String colorize(String original) {
        return codesToString() + original + Code.SANE;
    }

    public String format(String template, Object... args) {
        return colorize(String.format(template, args));
    }

    @Override
    public String toString() {
        return codesToString();
    }

    private String codesToString() {
        StringBuilder output = new StringBuilder();
        for(Code code : this.codes){
            output.append(code);
        }
        return output.toString();
    }

    public enum Code {
        SANE("\u001B[0m"),

        HIGH_INTENSITY("\u001B[1m"),
        LOW_INTENSITY("\u001B[2m"),

        ITALIC("\u001B[3m"),
        UNDERLINE("\u001B[4m"),
        BLINK("\u001B[5m"),
        RAPID_BLINK("\u001B[6m"),
        REVERSE_VIDEO("\u001B[7m"),
        INVISIBLE_TEXT("\u001B[8m"),

        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        MAGENTA("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),

        BACKGROUND_BLACK("\u001B[40m"),
        BACKGROUND_RED("\u001B[41m"),
        BACKGROUND_GREEN("\u001B[42m"),
        BACKGROUND_YELLOW("\u001B[43m"),
        BACKGROUND_BLUE("\u001B[44m"),
        BACKGROUND_MAGENTA("\u001B[45m"),
        BACKGROUND_CYAN("\u001B[46m"),
        BACKGROUND_WHITE("\u001B[47m");

        public final String ansiCode;
        Code(String ansiCode){
            this.ansiCode = ansiCode;
        }

        @Override
        public String toString() {
            return ansiCode;
        }
    }
}
