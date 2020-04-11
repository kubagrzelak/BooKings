import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

/**
 * Class taken from:
 * https://howtodoinjava.com/java/string/how-to-left-right-or-center-align-string-in-java/
 * It gives you ability to align the text displayed in the appliction.
 */
public class StringAlignUtils extends Format {

    private static final long serialVersionUID = 1L;

    public enum Alignment {
        LEFT, CENTER, RIGHT,
    }

    /** Current justification for formatting */
    private Alignment currentAlignment;

    /** Current max length in a line */
    private int maxChars;

    /**
     *
     * @param maxChars
     * @param align
     */
    public StringAlignUtils(int maxChars, Alignment align) {
        switch (align) {
            case LEFT:
            case CENTER:
            case RIGHT:
                this.currentAlignment = align;
                break;
            default:
                throw new IllegalArgumentException("invalid justification arg.");
        }
        if (maxChars < 0) {
            throw new IllegalArgumentException("maxChars must be positive.");
        }
        this.maxChars = maxChars;
    }

    /**
     *
     * @param input
     * @param where
     * @param ignore
     * @return
     */
    public StringBuffer format(Object input, StringBuffer where, FieldPosition ignore)
    {
        String s = input.toString();
        List<String> strings = splitInputString(s);

        for (String wanted : strings) {
            //Get the spaces in the right place.
            switch (currentAlignment) {
                case RIGHT:
                    pad(where, maxChars - wanted.length());
                    where.append(wanted);
                    break;
                case CENTER:
                    int toAdd = maxChars - wanted.length();
                    pad(where, toAdd / 2);
                    where.append(wanted);
                    pad(where, toAdd - toAdd / 2);
                    break;
                case LEFT:
                    where.append(wanted);
                    pad(where, maxChars - wanted.length());
                    break;
            }
            where.append("\n");
        }
        return where;
    }

    /**
     *
     * @param to
     * @param howMany
     */
    protected final void pad(StringBuffer to, int howMany) {
        to.append(" ".repeat(Math.max(0, howMany)));
    }

    /**
     *
     * @param s
     * @return
     */
    public String format(String s) {
        return format(s, new StringBuffer(), null).toString();
    }

    /** ParseObject is required, but not useful here. */
    public Object parseObject(String source, ParsePosition pos) {
        return source;
    }

    /**
     *
     * @param str
     * @return
     */
    private List<String> splitInputString(String str) {
        List<String> list = new ArrayList<String>();
        if (str == null)
            return list;
        for (int i = 0; i < str.length(); i = i + maxChars)
        {
            int endindex = Math.min(i + maxChars, str.length());
            list.add(str.substring(i, endindex));
        }
        return list;
    }
}

