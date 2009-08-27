package wizest.fx.util;



public class InfixExpress
{
    private String origin;

    private String[] express;
    private boolean[] operator;

    public InfixExpress(String origin,String[] express,boolean[] operator)
    {
        this.origin=origin;

        this.express=express;
        this.operator=operator;
    }

    public String getOrigin()
    {
        return this.origin;
    }

    public String[] getExpressions()
    {
        return express;
    }

    public boolean[] getIsOperator()
    {
        return operator;
    }

    public String toString()
    {
        StringBuffer buf=new StringBuffer();
        buf.append("{");
        for(int i=0;i < express.length;++i) {
            if(operator[i]) {
                buf.append("Op:");
            }
            buf.append(express[i]);
            buf.append(",");
        }
        buf.append("}");

        return buf.toString();
    }

}