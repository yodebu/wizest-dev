package wizest.fx.res;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 언어별 / 국가별 메세지를 가져온다.
 * 메세지 리소스 저장 형태는 java.util.PropertyResourceBundle 의 형식과 동일하다.
 * .properties 에 기록하는 다국어 문자는 반드시 <b>UNICODE 값</b>으로 적어야 문자셋에서 발생하는 문제를 해결할 수 있다.
 * <br><br>
 * <i>wizest.fx.res.StringConverter 를 실행하면 UNICODE입력을 쉽게 할 수 있다.<br>
 * java -classpath . wizest.fx.res.StringConverter [ENTER]</i>
 *
 * <pre>
 * ex)
 *  각각 .properties 파일에 다음의 내용이 들어 있을 경우
 *
 *      wizest/fx/res/text_en_US.properties
 *          welcome = welcome to {0}.
 *
 *      wizest/fx/res/text_ko_KR.properties
 *          welcome = {0} \uC5D0 \uC624\uC2E0 \uAC83\uC744 \uD658\uC601\uD569\uB2C8\uB2E4.
 *              참고로 위의 문자는 "에 오신 것을 환영합니다." 을 <b>UNICODE 값</b>으로 적은 것이다.
 *
 *      wizest/fx/res/text_ja_JP.properties
 *          welcome = {0} 일본어로 쓸 글을 유니코드화 시킨 문자열 ㅡ.ㅡ
 *
 *
 *  Resources m = Resources.getResources("wizest.fx.res.text",Locale.KOREA);
 *  m.getResources("welcome","내 홈페이지");
 *      -> 내 홈페이지에 오신 것을 환영합니다.
 *
 *
 *
 *  Resources m = Resources.getResources("wizest.fx.res.text",Locale.US);
 *  m.getResources("welcome","My homepage");
 *      -> welcome to my homepage.
 *
 *
 *  Resources m = Resources.getResources("wizest.fx.res.text",Locale.JAPAN);
 *  m.getResources("welcome","와따시와 ㅡ.ㅡ");
 *      -> 와따시와 ㅡ.ㅡ [일본어로 어쩌구 저쩌구...]
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
     * ResourceBundle 의 Key를 반환
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
     * Resources Instance를 가져온다.
     *
     * <pre>
     * ex)
     *  Resources m = Resources.getResources("wizest.fx.res.text");
     * </pre>
     *
     * @param baseName java.util.ResourceBundle의 getBundle과 동일하다.
     * @return
     */
    public static Resources getResources( String baseName )
    {
        Resources messages = new Resources();
        messages.bundle = PropertyResourceBundle.getBundle( baseName );

        return messages;
    }

    /**
     * Resources Instance를 가져온다.
     *
     * <pre>
     * ex)
     *  Resources m = Resources.getResources("wizest.fx.res.text",new Locale("ko","KR"));
     *  이 경우 /wizest/fx/res/text_ko_KR.properties 에서 ResourceBundle을 가져온다.
     * </pre>
     *
     * @param baseName java.util.ResourceBundle의 getBundle과 동일하다.
     * @param locale 가져올 resource bundle의 Locale
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
     * @param response HttpServletResponse 의 Locale을 적용하여 Resources를 가져온다.
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
