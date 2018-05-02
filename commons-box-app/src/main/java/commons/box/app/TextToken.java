package commons.box.app;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class TextToken<T> {
    /**
     * 文本变量解析开始
     */
    public static final String QS_OPEN = "${"; // 用于表达式解析过程

    /**
     * 文本变量解析结束
     */
    public static final String QS_CLOSE = "}";

    private final String openToken;
    private final String closeToken;
    private final Handler<T> handler;

    public TextToken(Handler<T> handler) {
        this(QS_OPEN, QS_CLOSE, handler);
    }

    public TextToken(String openToken, String closeToken, Handler<T> handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    /**
     * 解析字符串
     *
     * @param text
     * @return
     */
    public String parse(String text) {
        return this.parse(null, text);
    }

    /**
     * 带上下文解析条件的解析过程
     *
     * @param context
     * @param text
     * @return
     */
    public String parse(T context, String text) {
        StringBuilder builder = new StringBuilder();
        if (text != null && text.length() > 0) {
            char[] src = text.toCharArray();
            int offset = 0;
            int start = text.indexOf(this.openToken, offset);
            while (start > -1) {
                if (start > 0 && src[start - 1] == '\\') {
                    builder.append(src, offset, start - 1).append(this.openToken);
                    offset = start + this.openToken.length();
                } else {
                    int end = text.indexOf(this.closeToken, start);
                    if (end == -1) {
                        builder.append(src, offset, src.length - offset);
                        offset = src.length;
                    } else {
                        builder.append(src, offset, start - offset);
                        offset = start + this.openToken.length();
                        String content = new String(src, offset, end - offset);
                        builder.append(this.handler.handler(context, content));
                        offset = end + this.closeToken.length();
                    }
                }
                start = text.indexOf(openToken, offset);
            }
            if (offset < src.length) {
                builder.append(src, offset, src.length - offset);
            }
        }

        return builder.toString();
    }

    /**
     * 标记解析器
     */
    public static interface Handler<T> {
        /**
         * 处理解析过程
         * 如果内容不需要使用上下文，那么context可以为空
         *
         * @param context
         * @param token 原始文本中出现的token
         * @return 根据Token解析出来的字符串 将在原始文本用用此返回值替换掉token
         */
        String handler(T context, String token);
    }
}
