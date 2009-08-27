package wizest.fx.util;


@SuppressWarnings("unused")

/**
 * HttpServlet���� �����ϰ� ����� �� �ִ� ��ƿ��Ƽ �޼ҵ带 ����
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
     * HttpServletRequest.getQueryString()�� �̰��� �����ϱ� ���� request.getParameter() ���� ����
     * �ҷ� ���� ������ request character encoding�� ���� ���� ���� �����Ѵ�.
     * ���� request character encoding�� ���� �Ǿ� ���� ������ ISO-8859-1�� �����ϴ� �������� �ִ�.
     * �� �޼ҵ��
     * request�� character encoding�� locale�� ����� encoding�� query string�� ���� �ָ�
     * ������ request.getParameter() �� ���� �޼ҵ尡 �ҷ����� �Ͱ� ���� ���� �׻� �����ϰ�
     * ����ȭ �� ���ڿ��� �����ϰ� �Ѵ�.
     *
     * @param request
     * @param doURLDecoding query string�� URL decoding�� �ұ��?
     * @return encoding�� query string�� �����ش�.
     */
//    public static String getLocalizedQueryString(HttpServletRequest request,boolean doURLDecoding)
//    {
//        // request.getQueryString() �� �б� ����
//        // request.getParameter() �� �� �� �̻� ���� ���
//        // request�� character encoding�� �����Ǿ� �ִ� ��� encoding �� ���� ����
//        try {
//            request.getParameter("JuSt TrYiNg! DoN't CaRe.");
//        }
//        catch(Exception e) {}
//
//        String charEncodeing=request.getCharacterEncoding();
//        String queryString=request.getQueryString();
//
//        // quest string�� ������ null
//        if(queryString == null) {
//            return null;
//        }
//
//        // request�� encoding �� �����Ǿ� ���� ���� ���
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
     * JSP ���� �ٱ��� ������ ���� ��ܿ� �Է��ϴ� ������ �ڵ带 capsulation��
     *
     *  <%response.setContentType("text/html");%>
     *  <%response.setLocale(java.util.Locale.KOREA);%>
     *  <%session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,java.util.Locale.KOREA);%>
     *
     * @param locale HTTP response �� ������ locale, �̰Ϳ� ���� ���������� character set�� �����ȴ�.
     * @param pageContext
     */
//    public static void setHttpResponseLocale(Locale locale,PageContext pageContext)
//    {
//        setHttpResponseLocale(locale,pageContext.getSession(),pageContext.getResponse());
//    }

    /**
     * �ٱ��� ������ ���� ������ ���� �Է��ϴ� ���� capsulation��
     *
     *  <%response.setContentType("text/html");%>
     *  <%response.setLocale(java.util.Locale.KOREA);%>
     *  <%session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,java.util.Locale.KOREA);%>
     *
     * @param locale HTTP response �� ������ locale, �̰Ϳ� ���� ���������� character set�� �����ȴ�.
     * @param session struts�� i18n������ ���� �����ϰ��� �ϴ� locale�� ������ session�� org.apache.struts.Globals.LOCALE_KEY �� key�� ����Ѵ�.
     * @param response
     */
    private static String strutsLocaleKey=null;
    static
    {
        // �����Ͻÿ� struts library�� �ʿ����� �ʵ���
        // session.setAttribute(org.apache.struts.Globals.LOCALE_KEY,locale) �� ������ ǥ��
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

    /* ���� �޴������� ���� �޼ҵ� */
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
        value=value.replaceAll("\\s+"," ");  // ���ӵ� �����̽� ����

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
