package wizest.fx.template;
/**
 * @author wizest
 */
public interface Template {
    static final int TYPE_OUTER_TEMPLATE = 0;
    static final int TYPE_INNER_TEMPLATE = 1;

    String getTemplateText();

    int getTemplateType();
}