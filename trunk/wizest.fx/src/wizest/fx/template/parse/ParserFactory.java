/*
 * Created on 2004. 10. 1
 */
package wizest.fx.template.parse;
/**
 * @author wizest
 */
public abstract class ParserFactory {
    public final Class getTagParserClass() {
        if (TagParser.class.isAssignableFrom(_getTagParserClass()))
            //                _getTagParserClass().isAssignableFrom(TagParser.class))
            return _getTagParserClass();
        else
            throw new IllegalArgumentException("incorrect class type");
    }

    public final Class getTagNameParserClass() {
        //        if (_getTagNameParserClass().isAssignableFrom(TagNameParser.class))
        if (TagNameParser.class.isAssignableFrom(_getTagNameParserClass()))
            return _getTagNameParserClass();
        else
            throw new IllegalArgumentException("incorrect class type");
    }

    public final Class getAttributeParserClass() {
        //        if (_getAttributeParserClass().isAssignableFrom(AttributeParser.class))
        if (AttributeParser.class.isAssignableFrom(_getAttributeParserClass()))
            return _getAttributeParserClass();
        else
            throw new IllegalArgumentException("incorrect class type");
    }

    public final Class getAttributeNameParserClass() {
        //        if (_getAttributeNameParserClass().isAssignableFrom(AttributeNameParser.class))
        if (AttributeNameParser.class.isAssignableFrom(_getAttributeNameParserClass()))
            return _getAttributeNameParserClass();
        else
            throw new IllegalArgumentException("incorrect class type");
    }

    public final Class getAttributeValueParserClass() {
        //        if (_getAttributeValueParserClass().isAssignableFrom(AttributeValueParser.class))
        if (AttributeValueParser.class.isAssignableFrom(_getAttributeValueParserClass()))
            return _getAttributeValueParserClass();
        else
            throw new IllegalArgumentException("incorrect class type");
    }

    public final Class getAttributeValueEvalParserClass() {
        //        if (_getAttributeValueEvalParserClass().isAssignableFrom(AttributeValueEvalParser.class))
        if (AttributeValueEvalParser.class.isAssignableFrom(_getAttributeValueEvalParserClass()))
            return _getAttributeValueEvalParserClass();
        else
            throw new IllegalArgumentException("incorrect class type");
    }

    public final AttributeValueEvalParser createAttributeValueEvalParser() {
        Class cls = getAttributeValueEvalParserClass();
        try {
            return (AttributeValueEvalParser) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final AttributeValueParser createAttributeValueParser() {
        Class cls = getAttributeValueParserClass();
        try {
            return (AttributeValueParser) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final AttributeParser createAttributeParser() {
        Class cls = getAttributeParserClass();
        try {
            return (AttributeParser) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final AttributeNameParser createAttributeNameParser() {
        Class cls = getAttributeNameParserClass();
        try {
            return (AttributeNameParser) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final TagParser createTagParser() {
        Class cls = getTagParserClass();
        try {
            return (TagParser) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final TagNameParser createTagNameParser() {
        Class cls = getTagNameParserClass();
        try {
            return (TagNameParser) cls.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Class _getTagParserClass();

    protected abstract Class _getTagNameParserClass();

    protected abstract Class _getAttributeParserClass();

    protected abstract Class _getAttributeNameParserClass();

    protected abstract Class _getAttributeValueParserClass();

    protected abstract Class _getAttributeValueEvalParserClass();
}