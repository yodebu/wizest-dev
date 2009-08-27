package wizest.fx.util;


@SuppressWarnings("unused")

/**
 * HttpServlet에서 유용하게 사용할 수 있는 유틸리티 메소드를 제공
 *
 */
public class HttpUtils
{
    private static String START_DELIM = "@#s#@";
    private static String END_DELIM = "@#e#@";

    private HttpUtils()
    {
    }

    /**
     * HttpServletRequest.getQueryString()는 이것을 실행하기 전에 request.getParameter() 같은 것이
     * 불려 지지 않으면 request character encoding을 하지 않은 값을 리턴한다.
     * 또한 request character encoding가 지정 되어 있지 않으면 ISO-8859-1로 리턴하는 문제점이 있다.
     * 이 메소드는
     * request의 character encoding과 locale을 참고로 encoding한 query string을 돌려 주며
     * 이전에 request.getParameter() 와 같은 메소드가 불려지는 것과 관계 없이 항상 동일하게
     * 지역화 된 문자열을 리턴하게 한다.
     *
     * @param request
     * @param doURLDecoding query string을 URL decoding을 할까요?
     * @return encoding한 query string을 돌려준다.
     */
//    public static String getLocalizedQueryString(HttpServletRequest request,boolean doURLDecoding)
//    {
//        // request.getQueryString() 을 읽기 전에
//        // request.getParameter() 를 한 번 이상 읽을 경우
//        // request의 character encoding이 지정되어 있는 경우 encoding 된 값이 리턴
//        try {
//            request.getParameter("JuSt TrYiNg! DoN't CaRe.");
//        }
//        catch(Exception e) {}
//
//        String charEncodeing=request.getCharacterEncoding();
//        String queryString=request.getQueryString();
//
//        // quest string이 없으면 null
//        if(queryString == null) {
//            return null;
//        }
//
//        // request의 encoding 이 지정되어 있지 않을 경우
//        if(request.getCharacterEncoding() == null) {
//            charEncodeing=CharsetMapper.getDefaultCharset(request.getLocale());
//            try {
//                queryString=new String(queryString.getBytes("ISO-8859-1"),charEncodeing);
//            }
//            catch(UnsupportedEncodingException ex1) {
//            }
//        }
//
//        if(doURLDecoding) {
//            try {
//                queryString=URLDecoder.decode(queryString,charEncodeing);
//            }
//            catch(Exception ex) {}
//        }
//        return queryString;
//    }

    /**
     * JSP 에서 다국어 지원을 위해 상단에 입력하는 다음의 코드를 capsulation함
     *
     *  <%response.setContentType("text/html");%>
     *  <%response.setLocale(java.util.Locale.KOREA);%>
     *  <%session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,java.util.Locale.KOREA);%>
     *
     * @param locale HTTP response 에 지정할 locale, 이것에 따라 웹브라우저의 character set이 결정된다.
     * @param pageContext
     */
//    public static void setHttpResponseLocale(Locale locale,PageContext pageContext)
//    {
//        setHttpResponseLocale(locale,pageContext.getSession(),pageContext.getResponse());
//    }

    /**
     * 다국어 지원을 위해 다음과 같이 입력하는 것을 capsulation함
     *
     *  <%response.setContentType("text/html");%>
     *  <%response.setLocale(java.util.Locale.KOREA);%>
     *  <%session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,java.util.Locale.KOREA);%>
     *
     * @param locale HTTP response 에 지정할 locale, 이것에 따라 웹브라우저의 character set이 결정된다.
     * @param session struts의 i18n지원을 위해 지정하고자 하는 locale을 설정을 session에 org.apache.struts.Globals.LOCALE_KEY 의 key로 등록한다.
     * @param response
     */
    private static String strutsLocaleKey=null;
    static
    {
        // 컴파일시에 struts library가 필요하지 않도록
        // session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,locale) 를 돌려서 표현
        try {
            Class org_apache_struts_Globals=Class.forName("org.apache.struts.Globals");
//            strutsLocaleKey=(String)org_apache_struts_Globals.getDeclaredField("LOCALE_KEY").get(new Object());
            strutsLocaleKey=(String)org_apache_struts_Globals.getDeclaredField("LOCALE_KEY").get(org_apache_struts_Globals.
                newInstance());
        }
        catch(Throwable t) {
            strutsLocaleKey=null;
        }
    }

//	    public static void setHttpResponseLocale(Locale locale,HttpSession session,ServletResponse response)
//	    {
//	        response.setLocale(locale);
//	
//	//        response.setContentType("text/html");
//	        String charset = CharsetMapper.getDefaultCharset(locale);
//	        setHttpResponseCharset(response,charset);
//	
//	        if(strutsLocaleKey != null) {
//	            session.setAttribute(strutsLocaleKey,locale);
//	        }
//	    }

//    public static void setHttpResponseCharset(ServletResponse response, String charset)
//    {
//        if (charset==null)
//            response.setContentType("text/html");
//        else
//            response.setContentType("text/html; charset="+charset);
//    }

    /* 어드민 메뉴때문에 만든 메소드 */
    public static String alterString(boolean chkvalue,String yesString,String noString)
    {
        if(chkvalue) {
            return yesString;
        }
        else {
            return noString;
        }
    }

    public static String htmlEntityDecode(String value)
    {
        if(value == null) {
            return(null);
        }

        value=value.replaceAll("<!--.*?-->","");
        //str = str.replaceAll("\n","");
//        str=str.replaceAll("<BR>","\n");
//        str=str.replaceAll("<br>","\n");
//        str=str.replaceAll("<STYLE.*?</STYLE>","");
//        str=str.replaceAll("<style.*?</style>","");
//        str=str.replaceAll("<script.*?</script>","");
//        str=str.replaceAll("<SCRIPT.*?</SCRIPT>","");
//        str=str.replaceAll("<TITLE>.*?</TITLE>","");
//        str=str.replaceAll("<title>.*?</title>","");
//        return str.trim();
//        str=str.replaceAll("<.*?>","");

        value=value.replaceAll("\n"," ");
        value=value.replaceAll("&nbsp;"," ");
        value=value.replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>","");
        value=value.replaceAll("\\s+"," ");  // 연속된 스페이스 제거

        return value.trim();
    }


    public static String htmlspecialchars(String str)
    {
//        str=str.replaceAll("&", "&amp;");
//        str=str.replaceAll("\"", "&quot;");
//        str=str.replaceAll("'", "&#039;");
//        str=str.replaceAll("<", "&lt;");
//        str=str.replaceAll(">", "&gt;");
//        return str.trim();

        return filterHTML(str);
    }

    public static String replaceBlock(String startBlock,String endBlock,String value,String startReplaceStr,
                                      String endReplaceStr)
    {
        if(value == null) {
            return(null);
        }

        int beginIndex=0;
        int endIndex=0;
        while(true) {
            beginIndex=value.indexOf(startBlock,beginIndex);
            if(beginIndex == -1) {
                break;
            }
            endIndex=value.indexOf(endBlock,beginIndex);
            if(endIndex == -1) {
                break;
            }
            String strReturn="";
            strReturn+=value.substring(0,beginIndex);
            strReturn+=startReplaceStr;
            strReturn+=value.substring(beginIndex + startBlock.length(),endIndex);
            strReturn+=endReplaceStr;
            strReturn+=value.substring(endIndex + endBlock.length(),value.length());
            beginIndex=beginIndex + startReplaceStr.length() +  (endIndex - (beginIndex + startBlock.length())) + endReplaceStr.length();
            value=strReturn;
        }
        return value;
    }

    public static String replaceBlockAll(String startBlock,String endBlock,String value,String replaceStr)
    {
        if(value == null) {
            return(null);
        }

        int beginIndex=0;
        int endIndex=0;
        while(true) {
            beginIndex=value.indexOf(startBlock,beginIndex);
            if(beginIndex == -1) {
                break;
            }
            endIndex=value.indexOf(endBlock,beginIndex);
            if(endIndex == -1) {
                break;
            }
            String strReturn="";
            strReturn+=value.substring(0,beginIndex);
            strReturn+=replaceStr;
            strReturn+=value.substring(endIndex + endBlock.length(),value.length());
            beginIndex=beginIndex + replaceStr.length();
            value=strReturn;
        }
        return value;
    }

    public static String abstractFirstBlock(String startBlock,String endBlock,String value,String startReplaceStr,
                                      String endReplaceStr)
    {
        if(value == null) {
            return(null);
        }

        int beginIndex=0;
        int endIndex=0;
        while(true) {
            beginIndex=value.indexOf(startBlock,beginIndex);
            if(beginIndex == -1) {
                break;
            }
            endIndex=value.indexOf(endBlock,beginIndex);
            if(endIndex == -1) {
                break;
            }
            String strReturn="";
            strReturn+=startReplaceStr;
            strReturn+=value.substring(beginIndex + startBlock.length(),endIndex-1);
            strReturn+=endReplaceStr;
            value=strReturn;
        }
        return value;
    }

    public static String replaceNewLine(String value)
    {
        if(value == null) {
            return(null);
        }

        StringBuffer buf=new StringBuffer();
        int length=value.length();
        for(int i=0;i < length;++i) {
            if(value.charAt(i) == '\n') {
                buf.append("<br>");
            }
            else {
                buf.append(value.charAt(i));
            }
        }
        return buf.toString().replaceAll("\r", "");
    }

    public static String cutString(String value, int length)
    {
        if(value == null) {
            return(null);
        }

    	if(value.length() > length )
        	return value.substring(0, length)+"...";
        else
		    return value;
    }

    public static String filterHTML(String value)
    {
        if(value == null) {
            return(null);
        }

        char content[]=new char[value.length()];
        value.getChars(0,value.length(),content,0);
        StringBuffer result=new StringBuffer(content.length + 50);
        for(int i=0;i < content.length;i++) {
            switch(content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                case ' ':
                    result.append("&nbsp;");
                    break;
                case '\t':
                    result.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return(result.toString());
    }

    public static String filterHTMLNotSpace(String value)
    {
        if(value == null) {
            return(null);
        }

        char content[]=new char[value.length()];
        value.getChars(0,value.length(),content,0);
        StringBuffer result=new StringBuffer(content.length + 50);
        for(int i=0;i < content.length;i++) {
            switch(content[i]) {
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return(result.toString());
    }

    public static String selectDeleteString(String value, String startBlock, String endBlock)
    {
        if(value == null) {
            return(null);
        }

        int spos = value.indexOf(startBlock);
        if(spos != -1)
        {
            String tmpString = "";
            tmpString = value.substring(0, spos);
            tmpString += value.substring(value.indexOf(endBlock, spos)+endBlock.length());
            return tmpString ;
        }
        else
        {
            return value;
        }
    }

    public static String filterWML(String text) {
        if(text == null) {
            return(null);
        }

       text = text.replaceAll("<!--.*?-->", "");
       text = text.replaceAll("</[p|P]>",
                              START_DELIM + "br/" + END_DELIM);
       text = text.replaceAll("<br>", START_DELIM + "br/" + END_DELIM);
       text = text.replaceAll("<BR>", START_DELIM + "br/" + END_DELIM);
       text = text.replaceAll("<[a|A]", START_DELIM + "a");
       text = text.replaceAll("</[a|A]>", START_DELIM + "/a" + END_DELIM);

       text = text.replaceAll("<.*?>", "");

       // wap filtering
       text = text.replaceAll("'", "&apos;");
       text = text.replaceAll("\"", "&quot;");
       text = text.replaceAll("<", "&lt;");
       text = text.replaceAll(">", "&gt;");
       text = text.replaceAll("&", "&amp;");
       text = text.replaceAll("-", "&shy;");
       text = text.replaceAll(" ", "&nbsp;");
       text = text.replaceAll("\\$", "\\$\\$");

       text = text.replaceAll(START_DELIM, "<");
       text = text.replaceAll(END_DELIM, ">");

       return text.trim();
   }


   public static String filterSimpleText(String text) {
       if(text == null) {
           return(null);
       }

        text=text.replaceAll("<!--.*?-->","");
        text=text.replaceAll("<[p|P].*?>",START_DELIM+"br"+END_DELIM+START_DELIM+"br"+END_DELIM);
        text=text.replaceAll("</[p|P]>",START_DELIM+"br"+END_DELIM+START_DELIM+"br"+END_DELIM);
        text=text.replaceAll("<br>",START_DELIM+"br"+END_DELIM);
        text=text.replaceAll("<BR>",START_DELIM+"BR"+END_DELIM);
//        text=text.replaceAll("<[a|A]",START_DELIM+"a");
//        text=text.replaceAll("</[a|A]>",START_DELIM+"/a"+END_DELIM);

        text=text.replaceAll("<.*?>","");

        text=text.replaceAll(START_DELIM,"<");
        text=text.replaceAll(END_DELIM,">");

        return text.trim();
    }

    public static String addStartSlush(String text) {
        if(text == null) {
            return(null);
        }

        return (text.startsWith("/") ? text : "/" + text);
    }

    public static String filterHTMLdecode(String value)
    {
        if(value == null) {
            return(null);
        }

       value = value.replaceAll("&amp;" ,"&");
       value = value.replaceAll("&lt;","<");
       value = value.replaceAll("&gt;",">");
       value = value.replaceAll("&quot;","\"");

       return(value);
    }
}
