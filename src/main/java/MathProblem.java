public class MathProblem
{

    private int firstOperand_;
    private int secondOperand_;
    private OperatorType operatorType_;

    public MathProblem(int aInFirstOperand, int aInSecondOperand, OperatorType aInOperatorType)
    {
        firstOperand_ = aInFirstOperand;
        secondOperand_ = aInSecondOperand;
        operatorType_ = aInOperatorType;
    }

    public int getFirstOperand_()
    {
        return firstOperand_;
    }

    public int getSecondOperand_()
    {
        return secondOperand_;
    }

    public OperatorType getOperatorType_()
    {
        return operatorType_;
    }

    @Override
    public String toString()
    {
        return getFirstOperand_()+getOperatorType_().getVisual()+getSecondOperand_()+"=_______";
    }
}
