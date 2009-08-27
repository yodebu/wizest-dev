package wizest.fx.res;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * �� / ������ �޼����� �����´�.
 * �޼��� ���ҽ� ���� ���´� java.util.PropertyResourceBundle �� ���İ� �����ϴ�.
 * .properties �� ����ϴ� �ٱ��� ���ڴ� �ݵ�� <b>UNICODE ��</b>���� ����� ���ڼ¿��� �߻��ϴ� ������ �ذ��� �� �ִ�.
 * <br><br>
 * <i>wizest.fx.res.StringConverter �� �����ϸ� UNICODE�Է��� ���� �� �� �ִ�.<br>
 * java -classpath . wizest.fx.res.StringConverter [ENTER]</i>
 *
 * <pre>
 * ex)
 *  ���� .properties ���Ͽ� ������ ������ ��� ���� ���
 *
 *      wizest/fx/res/text_en_US.properties
 *          welcome = welcome to {0}.
 *
 *      wizest/fx/res/text_ko_KR.properties
 *          welcome = {0} \uC5D0 \uC624\uC2E0 \uAC83\uC744 \uD658\uC601\uD569\uB2C8\uB2E4.
 *              ����� ���� ���ڴ� "�� ���� ���� ȯ���մϴ�." �� <b>UNICODE ��</b>���� ���� ���̴�.
 *
 *      wizest/fx/res/text_ja_JP.properties
 *          welcome = {0} �Ϻ���� �� ���� �����ڵ�ȭ ��Ų ���ڿ� ��.��
 *
 *
 *  Resources m = Resources.getResources("wizest.fx.res.text",Locale.KOREA);
 *  m.getResources("welcome","�� Ȩ������");
 *      -> �� Ȩ�������� ���� ���� ȯ���մϴ�.
 *
 *
 *
 *  Resources m = Resources.getResources("wizest.fx.res.text",Locale.US);
 *  m.getResources("welcome","My homepage");
 *      -> welcome to my homepage.
 *
 *
 *  Resources m = Resources.getResources("wizest.fx.res.text",Locale.JAPAN);
 *  m.getResources("welcome","�͵��ÿ� ��.��");
 *      -> �͵��ÿ� ��.�� [�Ϻ���� ��¼�� ��¼��...]
 * </pre>
 *
 * @see     java.util.ResourceBundle
 * @see     java.util.PropertyResourceBundle
 * @see     java.text.MessageFormat
 */
public class Resources
{
    private ResourceBundle bundle = null;

    protected Resources()
    {}

    public void setBundle( ResourceBundle bundle )
    {
        this.bundle = bundle;
    }

    /**
     * ResourceBundle �� Key�� ��ȯ
     *
     * @return
     */


    public Enumeration getKeys()
    {
        return bundle.getKeys();
    }

    public Locale getLocale()
    {
        return bundle.getLocale();
    }

    public String get( String key )
    {
        try
        {
            return bundle.getString( key );
        }
        catch(MissingResourceException e)
        {
//            e.printStackTrace();
            return "[[key="+key+"]]";
        }
    }

    public String get( String key, Object[] args )
    {
        try
        {
            String s=bundle.getString(key);
            return MessageFormat.format(s, args);
        }
        catch(MissingResourceException e)
        {
            String s="[[key="+key+",value=";
            for(int i=0;i<args.length;++i)
                s+=args[i].toString()+" ";

            s+="]]";
            return s;
        }
    }

    public String get( String key, Object arg0 )
    {
        return get( key, new Object[]
                           {arg0} );
    }

    public String get( String key, Object arg0, Object arg1 )
    {
        return get( key, new Object[]
                           {arg0, arg1} );
    }

    public String get( String key, Object arg0, Object arg1, Object arg2 )
    {
        return get( key, new Object[]
                           {arg0, arg1, arg2} );
    }

    /**
     * Resources Instance�� �����´�.
     *
     * <pre>
     * ex)
     *  Resources m = Resources.getResources("wizest.fx.res.text");
     * </pre>
     *
     * @param baseName java.util.ResourceBundle�� getBundle�� �����ϴ�.
     * @return
     */
    public static Resources getResources( String baseName )
    {
        Resources messages = new Resources();
        messages.bundle = PropertyResourceBundle.getBundle( baseName );

        return messages;
    }

    /**
     * Resources Instance�� �����´�.
     *
     * <pre>
     * ex)
     *  Resources m = Resources.getResources("wizest.fx.res.text",new Locale("ko","KR"));
     *  �� ��� /wizest/fx/res/text_ko_KR.properties ���� ResourceBundle�� �����´�.
     * </pre>
     *
     * @param baseName java.util.ResourceBundle�� getBundle�� �����ϴ�.
     * @param locale ������ resource bundle�� Locale
     * @return
     */
    public static Resources getResources( String baseName, Locale locale )
    {
        Resources messages = new Resources();
        messages.bundle = PropertyResourceBundle.getBundle( baseName, locale );

        return messages;
    }

    /**
     * @param baseName
     * @return
     */
    public static Resources getResources( String baseName, Locale locale, ClassLoader loader )
    {
        Resources messages = new Resources();
        messages.bundle = PropertyResourceBundle.getBundle( baseName, locale, loader );

        return messages;
    }

    /**
     * @param baseName
     * @param response HttpServletResponse �� Locale�� �����Ͽ� Resources�� �����´�.
     * @return
     */
	// public static Resources getResources( String baseName,
	// HttpServletResponse response )
	// {
	// Locale locale = response.getLocale();
	//
	// Resources messages = new Resources();
	// messages.bundle = PropertyResourceBundle.getBundle( baseName, locale );
	//
	// return messages;
	//    }
}
