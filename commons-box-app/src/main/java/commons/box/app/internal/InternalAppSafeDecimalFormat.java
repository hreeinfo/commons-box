package commons.box.app.internal;

import java.text.*;

/**
 * TODO 待实现
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/23 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalAppSafeDecimalFormat extends NumberFormat {
    private final String pattern;
    private final DecimalFormatSymbols symbols;

    private final ThreadLocal<DecimalFormat> _inst;

    public InternalAppSafeDecimalFormat(String pattern, DecimalFormatSymbols symbols) {
        super();
        this.pattern = pattern;
        this.symbols = symbols;
        this._inst = ThreadLocal.withInitial(() -> new DecimalFormat(pattern, symbols));
    }

    private DecimalFormat get() {
        DecimalFormat fmt = this._inst.get();
        if (fmt == null) fmt = new DecimalFormat(this.pattern, this.symbols);
        return fmt;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return this.get().format(obj, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.get().format(number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.get().format(number, toAppendTo, pos);
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return this.get().parse(source, parsePosition);
    }
}
