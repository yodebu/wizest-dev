/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.ref;

import java.io.PrintStream;

import wizest.fx.template.tagext.BodyTagSupport;
import wizest.fx.template.tagext.TagException;

/**
 * @author wizest
 */
public class Test3Tag extends BodyTagSupport {
    private String ATTR1;

    public void setATTR1(String ATTR1) {
//        System.out.println(ATTR1);
        this.ATTR1 = ATTR1;
    }
    public void release() {
        ATTR1 = null;
        super.release();
    }    

    public int doStartTag() throws TagException {
        PrintStream out = System.out;
        out.println();
        out.print("start: "+getId()+" ");
        out.print(this.getTagMarker());
        out.print(", " + getAttributeMap());
        out.print(", tagId=" + this.getId());
        out.println(", ATTR1=" + this.ATTR1);
        return super.doStartTag();
    }
    
    public int doEndTag() throws TagException {
        PrintStream out = System.out;
        out.println("end: "+getId()+" ");
        
        return super.doEndTag();
    }

}