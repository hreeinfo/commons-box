package commons.box.app;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 值对象封装
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class DataValue {
    public static enum Type {
        BYTE, INT, SHORT, DOUBLE, FLOAT, LONG, BIG_INT, BIG_DEC,
        STRING, DATE,
        LIST, SET, MAP, ENTRY,
        OBJECT, ARRAY
    }

    private final Type type;
    private final Class<?> defineType;
    private final Object value;

    public DataValue(Class<?> defineType, Object value) {
        this.defineType = (defineType != null) ? defineType : ((value != null) ? value.getClass() : Object.class);
        this.value = value;
        this.type = (this.defineType != null) ? typeOf(this.defineType) : typeOfValue(this.value);
    }

    public Type type() {
        return type;
    }

    public Class<?> defineType() {
        return defineType;
    }

    public Object getValue() {
        return value;
    }

    public byte getByteValue() {
        if (this.value == null) return 0;

        //TODO
        return 0;
    }

    public short getShortValue() {
        if (this.value == null) return 0;

        //TODO
        return 0;
    }

    public int getIntValue() {
        if (this.value == null) return 0;

        //TODO
        return 0;
    }

    public long getLongValue() {
        if (this.value == null) return 0L;

        //TODO
        return 0L;
    }

    public float getFloatValue() {
        if (this.value == null) return 0.0f;

        //TODO
        return 0.0f;
    }

    public double getDoubleValue() {
        if (this.value == null) return 0.0;

        //TODO
        return 0.0;
    }

    public BigInteger getBigIntValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public BigDecimal getBigDecValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public String getStrValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public Date getDateValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public <T> List<T> getListValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public <T> Set<T> getSetValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public <K, T> Map<K, T> getMapValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public <K, T> Map.Entry<K, T> getEntryValue() {
        if (this.value == null) return null;

        //TODO
        return null;
    }

    public boolean isCollection() {
        if (type == null) return false;
        switch (type) {
            case LIST:
            case SET:
                return true;
        }
        return false;
    }

    public boolean isNumber() {
        if (type == null) return false;
        switch (type) {
            case BYTE:
            case INT:
            case SHORT:
            case DOUBLE:
            case FLOAT:
            case LONG:
            case BIG_INT:
            case BIG_DEC:
                return true;
        }
        return false;
    }

    public boolean isDate() {
        return Type.DATE.equals(type);
    }

    public boolean isString() {
        return Type.STRING.equals(type);
    }

    public boolean isByte() {
        return Type.BYTE.equals(type);
    }

    public boolean isInt() {
        return Type.INT.equals(type);
    }

    public boolean isLong() {
        return Type.LONG.equals(type);
    }

    public boolean isShort() {
        return Type.SHORT.equals(type);
    }

    public boolean isDouble() {
        return Type.DOUBLE.equals(type);
    }

    public boolean isBigInteger() {
        return Type.BIG_INT.equals(type);
    }

    public boolean isBigDecimal() {
        return Type.BIG_DEC.equals(type);
    }

    public boolean isList() {
        return Type.LIST.equals(type);
    }

    public boolean isSet() {
        return Type.SET.equals(type);
    }

    public boolean isMap() {
        return Type.MAP.equals(type);
    }

    public boolean isEntry() {
        return Type.ENTRY.equals(type);
    }

    public boolean isArray() {
        return Type.ARRAY.equals(type);
    }

    public boolean isObject() {
        return Type.OBJECT.equals(type);
    }

    public static Type typeOf(Class<?> o) {
        if (o == null) return Type.OBJECT;
        // TODO 需要实现
        return null;
    }


    public static Type typeOfValue(Object o) {
        if (o == null) return Type.OBJECT;
        return typeOf(o.getClass());
    }

    public static void main(String[] args) {
        NumberFormat nf = new DecimalFormat("000000");
        long a = 1001;
        long b = 1020010010;
        System.out.println("a=" + nf.format(a));
        System.out.println("b=" + nf.format(b));
    }
}
