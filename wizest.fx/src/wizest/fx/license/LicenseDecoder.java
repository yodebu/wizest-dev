package wizest.fx.license;

public interface LicenseDecoder
{
//    String encode(final String serialNumber,final License license) throws LicenseException ;
    License decode(final String serialNumber,final String activationKey) throws LicenseException ;
}
