package wizest.fx.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import wizest.fx.template.parse.ParserFactory;
import wizest.fx.template.parse.TagParser;
import wizest.fx.util.DoubleKeyHashMap;
import wizest.fx.util.HttpUtils;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * @author wizest
 */
public class ExceptionTracer {
	// highlight
	private static final String COLOR_ERROR_FG = "yellow";
	private static final String COLOR_ERROR_BG = "red";
	private static final String COLOR_TAG_FG = "green";
	// private static final String COLOR_TAG_BG = "green";
	private static final String COLOR_LINE_FG = "blue";
	private static final String COLOR_LINE_BG = "#DDDDCC";
	private static final String COLOR_LINENUMBER_FG = "darkblue";
	private static final String COLOR_LINENUMBER_BG = "lightblue";
	private final ParserFactory parserFactory;

	public ExceptionTracer(ParserFactory parserFactory) {
		this.parserFactory = parserFactory;
	}

	public String traceTemplate(ExceptionCause[] cs) {
		StringBuffer buff = new StringBuffer();
		HashSet tplTextSet = new LinkedHashSet();
		DoubleKeyHashMap tplMarkMap = new DoubleKeyHashMap();
		// template text,mark 얻기
		for (int i = 0, length = cs.length; i < length; ++i) {
			String text = cs[i].getText();
			Marker mark = cs[i].getMark();
			tplTextSet.add(text);
			if (mark != null)
				tplMarkMap.put(text, mark, new Integer(i));
		}
		// highlight 와 함께 html로 바꾸기
		Iterator it = tplTextSet.iterator();
		while (it.hasNext()) {
			String tplText = (String) it.next();
			ArrayList markList = new ArrayList(tplMarkMap.key2Set(tplText));
			Marker[] marks = (Marker[]) markList.toArray(new Marker[0]);
			Arrays.sort(marks, new Comparator() {
				public int compare(Object o1, Object o2) {
					Marker m1 = (Marker) o1;
					Marker m2 = (Marker) o2;
					return m1.getBeginIndex() - m2.getBeginIndex();
				}

				public boolean equals(Object obj) {
					return true;
				}
			});
			ArrayList lineNumber = new ArrayList();
			StringBuffer newText = new StringBuffer();
			int pivot = 0;
			for (int i = 0; i < marks.length; ++i) {
				Marker mark = marks[i];
				// highlight and attach a message
				int causeIdx = ((Integer) tplMarkMap.get(tplText, mark)).intValue();
				String msg = HttpUtils.filterHTML(((Throwable) cs[causeIdx].getCause()).getMessage());
				// newText.append(highlightTag(HttpUtils.filterHTML(tplText.substring(pivot,
				// mark.getBeginIndex()))));
				newText.append(highlightTag(tplText.substring(pivot, mark.getBeginIndex())));
				newText.append("<a name=\"_t" + cs.hashCode() + "_");
				newText.append(causeIdx);
				newText.append("\"><span title=\"");
				newText.append(msg);
				newText.append("\">");
				newText.append("<b><font style='color:" + COLOR_ERROR_FG + ";background-color:" + COLOR_ERROR_BG + "'>");
				newText.append(HttpUtils.filterHTML(tplText.substring(mark.getBeginIndex(), mark.getEndIndex())));
				newText.append("</font></b>");
				newText.append("</span></a>");
				pivot = mark.getEndIndex();
				lineNumber.add(new Integer(getLineNumber(tplText, mark.getBeginIndex())));
			}
			// newText.append(highlightTag(HttpUtils.filterHTML(tplText.substring(pivot))));
			newText.append(highlightTag(tplText.substring(pivot)));
			// layout
			{
				// 줄단위로 분리
				tplText = newText.toString();
				ArrayList lineTextList = new ArrayList();
				int length = tplText.length();
				int newLineIdx = 0;
				for (int i = 0; i < length; ++i) {
					if (tplText.charAt(i) == '\n') {
						lineTextList.add(tplText.substring(newLineIdx, i));
						newLineIdx = Math.min(i + 1, length);
					}
				}
				lineTextList.add(tplText.substring(newLineIdx));
				String[] lineText = (String[]) lineTextList.toArray(new String[0]);
				StringBuffer unitTpl = new StringBuffer();
				unitTpl.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
				// unitTpl.append("<tr><td>Template</td></tr>");
				// unitTpl.append("<tr height='5'><td></td></tr>");
				// unitTpl.append("</td></tr>");
				unitTpl.append("<tr><td style='background-color:white'><table width='100%' border='0' cellspacing='0' cellpadding='0'>");
				for (int i = 0; i < lineText.length; ++i) {
					String line = lineText[i];
					unitTpl.append("<tr><td width='30' align='right' valign='top' style='background-color:" + COLOR_LINENUMBER_BG + "'><font style='color:" + COLOR_LINENUMBER_FG + "'>");
					unitTpl.append(i + 1); // line number
					unitTpl.append("</font>&nbsp</td>");
					// line highlight 결정
					if (lineNumber.contains(new Integer(i + 1))) {
						unitTpl.append("<td style='background-color:" + COLOR_LINE_BG + "'><span style='color:" + COLOR_LINE_FG + "'>&nbsp;");
					} else {
						unitTpl.append("<td><span>&nbsp;");
					}
					unitTpl.append(line);
					unitTpl.append("</span></td></tr>");
				}
				unitTpl.append("</table></td></tr></table>");
				// traced 된 template 한 개
				buff.append(unitTpl.toString());
			}
			buff.append("<br/>");
		}
		return buff.toString();
	}

	public static String traceCause(ExceptionCause[] cs) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < cs.length; ++i) {
			Throwable cause = cs[i].getCause();
			String errName = cause.getClass().getName();
			errName = errName.substring(errName.lastIndexOf('.') + 1);
			String errMsg = cause.getMessage();
			if (errMsg == null) {
				errMsg = HttpUtils.filterHTML(errName);
			} else {
				errMsg = HttpUtils.filterHTML(errName + " : " + errMsg);
			}
			// buf.append("<a href='#_t" + i + "' title=\"");
			// buf.append("**.Technical report\n");
			// buf.append(HttpUtils.filterHTML(StackTrace.trace(cause,2)));
			// buf.append("\">");
			buf.append("<a href='#_t" + cs.hashCode() + "_" + i + "' >");
			buf.append(errMsg);
			buf.append("</a><br/>");
		}
		return buf.toString();
	}

	private static int getLineNumber(String templateText, int beginIndex) {
		int length = Math.min(templateText.length(), beginIndex);
		int line = 1;
		for (int i = 0; i < length; ++i) {
			if (templateText.charAt(i) == '\n') {
				++line;
			}
		}
		return line;
	}

	private String highlightTag(String s) {
		StringBuffer buf = new StringBuffer();
		TagParser p = parserFactory.createTagParser();
		p.initialize(s);
		int pivot = 0;
		while (true) {
			Marker mark = null;
			try {
				mark = p.parseNext();
			} catch (ParserException ex) {
				break;
			}
			if (mark == null) {
				break;
			}
			buf.append(HttpUtils.filterHTML(s.substring(pivot, mark.getBeginIndex())));
			// buf.append("<b><font style='color:"+COLOR_TAG_FG+";background-color:"+COLOR_TAG_BG+"'>");
			buf.append("<b><font style='color:" + COLOR_TAG_FG + "'>");
			buf.append(HttpUtils.filterHTML(s.substring(mark.getBeginIndex(), mark.getEndIndex())));
			buf.append("</font></b>");
			pivot = mark.getEndIndex();
		}
		buf.append(HttpUtils.filterHTML(s.substring(pivot)));
		p.release();
		return buf.toString();
	}
}