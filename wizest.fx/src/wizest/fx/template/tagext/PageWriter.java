package wizest.fx.template.tagext;

import java.io.StringWriter;
import java.util.Vector;

/**
 * ActionTag�� ����� page�� ��� action�� data�� PageWriter�� ��������. PageWriter�� instance�� �� page�� ������ �� ���Ǵ� pageContext�� ���� out�̶�� �̸����� �����ȴ�.
 *
 * 
 * @see BodyContent
 * 
 * @see java.io.Writer
 * @see java.io.StringWriter
 * @see java.io.PrintWriter
 * 
 * @see javax.servlet.jsp.JspWriter
 */
public class PageWriter extends StringWriter {
    static String lineSeparator = System.getProperty("line.separator");

    public PageWriter() {
        super();
    }

    public void clear() {
        StringBuffer buff = getBuffer();
        buff.setLength(0);
    }

    private void _write(String s) {
        super.write(s);
        fireWrite(s);
    }

    public void newLine() {
        _write(lineSeparator);
    }

    public void print(boolean b) {
        _write(b ? "true" : "false");
    }

    public void print(char c) {
        _write(String.valueOf(c));
    }

    public void print(int i) {
        _write(String.valueOf(i));
    }

    public void print(long l) {
        _write(String.valueOf(l));
    }

    public void print(float f) {
        _write(String.valueOf(f));
    }

    public void print(double d) {
        _write(String.valueOf(d));
    }

    public void print(char s[]) {
        _write(new String(s));
    }

    public void print(String s) {
        if (s == null) {
            s = "null";
        }
        _write(s);
    }

    public void print(Object obj) {
        _write(String.valueOf(obj));
    }

    public void println() {
        newLine();
    }

    public void println(boolean x) {
        print(x);
        newLine();
    }

    public void println(char x) {
        print(x);
        newLine();
    }

    public void println(int x) {
        print(x);
        newLine();
    }

    public void println(long x) {
        print(x);
        newLine();
    }

    public void println(float x) {
        print(x);
        newLine();
    }

    public void println(double x) {
        print(x);
        newLine();
    }

    public void println(char x[]) {
        print(x);
        newLine();
    }

    public void println(String x) {
        print(x);
        newLine();
    }

    public void println(Object x) {
        print(x);
        newLine();
    }

    public String toString() {
        return super.toString();
    }
    private transient Vector pageWritingListeners;

    public synchronized void removePageWritingListener(PageWritingListener l) {
        if (pageWritingListeners != null && pageWritingListeners.contains(l)) {
            Vector v = (Vector) pageWritingListeners.clone();
            v.removeElement(l);
            pageWritingListeners = v;
        }
    }

    public synchronized void addPageWritingListener(PageWritingListener l) {
        Vector v = pageWritingListeners == null ? new Vector(2) : (Vector) pageWritingListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            pageWritingListeners = v;
        }
    }

    protected void fireWrite(String e) {
        if (pageWritingListeners != null) {
            Vector listeners = pageWritingListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((PageWritingListener) listeners.elementAt(i)).write(e);
            }
        }
    }
}