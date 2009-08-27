package wizest.fx.license;

import java.util.Date;

import wizest.fx.util.SerializedString;
import wizest.fx.util.SerializedStringException;


/**
 * Target License를 이 클래스로 포장하고 serialization 해버리면 별도의 Target License class 의 class loading
 * 처리를 하지 않고(이 클래스 자체가 처리해줌) License를 받은 컴퓨터에서 deserializing할 수 있다.
 */
public final class PortableLicense implements License
{
    private final String innerLicenseName;
    private final String innerLicenseCls;
    private final String innerLicenseObj;

    private transient License innerLicense;

    public PortableLicense(License targetLicense) throws LicenseException {
        try {
            // super
            Class from = targetLicense.getClass();
            innerLicenseName = from.getName();
            innerLicenseCls = SerializedString.saveResource(from.getName(),Thread.currentThread().getContextClassLoader());
            innerLicenseObj=SerializedString.serialize(targetLicense);
        }
        catch(Exception ex) {
            throw new LicenseException("could not create a license",ex);
        }
    }

    private License getInnerLicense() {
        if(innerLicense == null) {

            Class cls=SerializedString.loadClass(innerLicenseName,innerLicenseCls);
            // deserialize licenseObj
            try {
                this.innerLicense=(License)SerializedString.deserialize(cls,innerLicenseObj,false);
            }
            catch(SerializedStringException ex) {
                throw new RuntimeException("could not deserialize a license",ex);
            }
        }
        return innerLicense;
    }

    public Date getBeginDate() {
        return getInnerLicense().getBeginDate();
    }

    public Date getEndDate() {
        return getInnerLicense().getEndDate();
    }

    public Date getIssueDate() {
        return getInnerLicense().getIssueDate();
    }

    public String getLicenseDescription() {
        return getInnerLicense().getLicenseDescription();
    }

    public String getLicenseFrom() {
        return getInnerLicense().getLicenseFrom();
    }

    public String getLicenseTo() {
        return getInnerLicense().getLicenseTo();
    }

    public String getProduct() {
        return getInnerLicense().getProduct();
    }

    public Object getValue(String key) {
        return getInnerLicense().getValue(key);
    }

    public int getValueAsInt(String key) {
        return getInnerLicense().getValueAsInt(key);
    }

    public String getValueAsString(String key) {
        return getInnerLicense().getValueAsString(key);
    }

    public String[] getValueKeys() {
        return getInnerLicense().getValueKeys();
    }

    public String getVersion() {
        return getInnerLicense().getVersion();
    }

    public boolean hasValue(String key) {
        return getInnerLicense().hasValue(key);
    }

    public void validate() throws LicenseException {
        getInnerLicense().validate();
    }

    public String toString() {
        return getInnerLicense().toString();
    }
}
