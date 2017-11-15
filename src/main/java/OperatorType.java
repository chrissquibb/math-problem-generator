public enum OperatorType
{
    UNKNOWN_OPERATOR("UNKNOWN_OPERATOR"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLICATION("*"),
    DIVISION("/");

    private String visual_;
    OperatorType(String aInVisual)
    {
        visual_ = aInVisual;
    }

    public String getVisual()
    {
        return visual_;
    }

    public static OperatorType convert(String aInVisual)
    {
        switch (aInVisual)
        {
            case "+": return PLUS;
            case "-": return MINUS;
            case "*": return MULTIPLICATION;
            case "/": return DIVISION;
            default: return UNKNOWN_OPERATOR;
        }
    }
}
