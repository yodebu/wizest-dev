package wizest.fx.util;



public class StackTrace
{
    private static final int TRACING_DEPTH=5;

    /**
     * ���ܿ� ���� �� ������ e.printStackTrace() ó�� �ۼ��Ͽ� �����Ѵ�.
     * @param t
     * @return
     */
    public static String trace(Throwable t)
    {
        return trace(t,TRACING_DEPTH);
    }

    /**
     * ���ܿ� ���� �� ������ e.printStackTrace() ó�� �ۼ��Ͽ� �����Ѵ�.
     * @param t
     * @param tracingDepth tracing�ϴ� ���� (�ʹ� ���� ������ �ʿ� ����)
     * @return
     */
    public static String trace(Throwable t,int tracingDepth)
    {
        if(t == null) {
            return "null";
        }

        int length;
        StringBuffer buff=new StringBuffer();

        buff.append(t.toString());
//        buff.append( "\n" );

        StackTraceElement[] trace=null;

        trace=t.getStackTrace();
        length=Math.min(trace.length,tracingDepth);
        for(int i=0;i < length;++i) {
            buff.append("\n    at ");
            buff.append(trace[i].toString());
        }
        if(trace.length != length) {
            buff.append("\n    ...");

            // cause
        }
        while(t.getCause() != null) {
            t=t.getCause();

            buff.append("\nCaused by: ");
            buff.append(t.toString());
//            buff.append( "\n" );

            trace=t.getStackTrace();
            length=Math.min(trace.length,tracingDepth);
            for(int i=0;i < length;++i) {
                buff.append("\n    at ");
                buff.append(trace[i].toString());
            }
            if(trace.length != length) {
                buff.append("\n    ...");
            }
        }

        return buff.toString();
    }
//    public static void main(String[] args)
//    {
//
//    }
}