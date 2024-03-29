package wizest.fx.license;

import wizest.fx.util.SerializedString;

/**
 * 인코더의 내부가 어떻게 생겼는지 모르게...
 * 역컴파일 해봐도 골때리겠지~.. 므흣~ s--v
 *
 */

public final class LicenseCodec implements LicenseDecoder
{
    /**
     * LicenseEncoderImpl 클래스를 마샬링 한 것
     * SerializedString.saveResource("net/ion/framework/license/LicenseEncoderImpl.class",null);
     * 의 값임
     */
    private final static String licenseEncoderImplClassString=
        "yv66vgAAADAAiQoAIQBDCgAVAEQHAEUIAEYKAAMARwoASABJCgBKAEsKABUATAcATQoACQBDCgAJAE4K"+
        "AAkATwoAFQBQCgBRAFIKAFEAUwcAVAgAVQoAAwBWBwBXCgATAFgHAFkKAFEAWgoAFQBbCgAVAFwIAF0K"+
        "ABUAXgoASABfBwBgCABhCwAcAGIIAGMHAGQHAGUHAGYHAGcBAAY8aW5pdD4BAAMoKVYBAARDb2RlAQAP"+
        "TGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEALkxuZXQvaW9uL2ZyYW1l"+
        "d29yay9saWNlbnNlL0xpY2Vuc2VFbmNvZGVySW1wbDsBAAdfZW5jb2RlAQBJKExqYXZhL2xhbmcvU3Ry"+
        "aW5nO0xuZXQvaW9uL2ZyYW1ld29yay9saWNlbnNlL0xpY2Vuc2U7KUxqYXZhL2xhbmcvU3RyaW5nOwEA"+
        "DHNlcmlhbE51bWJlcgEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAB2xpY2Vuc2UBACNMbmV0L2lvbi9mcmFt"+
        "ZXdvcmsvbGljZW5zZS9MaWNlbnNlOwEACmxpY2Vuc2VPYmoBAARzS2V5AQANYWN0aXZhdGlvbktleQEA"+
        "AmV4AQAyTG5ldC9pb24vZnJhbWV3b3JrL3V0aWwvU2VyaWFsaXplZFN0cmluZ0V4Y2VwdGlvbjsBAAFl"+
        "AQAVTGphdmEvbGFuZy9FeGNlcHRpb247AQAKRXhjZXB0aW9ucwEABmRlY29kZQEASShMamF2YS9sYW5n"+
        "L1N0cmluZztMamF2YS9sYW5nL1N0cmluZzspTG5ldC9pb24vZnJhbWV3b3JrL2xpY2Vuc2UvTGljZW5z"+
        "ZTsBAAR0ZXh0AQAFc0tleTIBAAFvAQASTGphdmEvbGFuZy9PYmplY3Q7AQACbGUBACxMbmV0L2lvbi9m"+
        "cmFtZXdvcmsvbGljZW5zZS9MaWNlbnNlRXhjZXB0aW9uOwEAClNvdXJjZUZpbGUBABdMaWNlbnNlRW5j"+
        "b2RlckltcGwuamF2YQwAJAAlDABoAGkBACpuZXQvaW9uL2ZyYW1ld29yay9saWNlbnNlL0xpY2Vuc2VF"+
        "eGNlcHRpb24BADR0b28gc2hvcnQgc2VyaWFsIGtleSwgYXQgbGVhc3QgZWlnaHQgY2hhcmFjdGVycyBs"+
        "b25nDAAkAGoHAGsMAGwAbQcAbgwAbwBwDABxAHIBABZqYXZhL2xhbmcvU3RyaW5nQnVmZmVyDABzAHQM"+
        "AHUAdgwAdwB4BwB5DAB6AHsMAHwAfQEAMG5ldC9pb24vZnJhbWV3b3JrL3V0aWwvU2VyaWFsaXplZFN0"+
        "cmluZ0V4Y2VwdGlvbgEAFnVuc2VyaWFsaXphYmxlIGxpY2Vuc2UMACQAfgEAE2phdmEvbGFuZy9FeGNl"+
        "cHRpb24MAH8AdgEAEGphdmEvbGFuZy9TdHJpbmcMAIAAgQwAJACCDACDAIQBABRpbmNvcnJlY3Qgc2Vy"+
        "aWFsIGtleQwAcQCFDACGAIcBACFuZXQvaW9uL2ZyYW1ld29yay9saWNlbnNlL0xpY2Vuc2UBABRpbnZh"+
        "bGlkIGxpY2Vuc2UgdGV4dAwAiAAlAQApaW5jb3JyZWN0IHNlcmlhbCBudW1iZXIgb3IgYWN0aXZhdGlv"+
        "biBrZXkBACxuZXQvaW9uL2ZyYW1ld29yay9saWNlbnNlL0xpY2Vuc2VFbmNvZGVySW1wbAEAEGphdmEv"+
        "bGFuZy9PYmplY3QBAChuZXQvaW9uL2ZyYW1ld29yay9saWNlbnNlL0xpY2Vuc2VEZWNvZGVyAQAUamF2"+
        "YS9pby9TZXJpYWxpemFibGUBAAZsZW5ndGgBAAMoKUkBABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBACdu"+
        "ZXQvaW9uL2ZyYW1ld29yay91dGlsL1NlcmlhbGl6ZWRTdHJpbmcBAAlzZXJpYWxpemUBACYoTGphdmEv"+
        "bGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvU3RyaW5nOwEAHG5ldC9pb24vZnJhbWV3b3JrL3V0aWwvQ3J5"+
        "cHQBAAVjcnlwdAEAOChMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFu"+
        "Zy9TdHJpbmc7AQAJc3Vic3RyaW5nAQAVKEkpTGphdmEvbGFuZy9TdHJpbmc7AQAGYXBwZW5kAQAsKExq"+
        "YXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZ0J1ZmZlcjsBAAh0b1N0cmluZwEAFCgpTGph"+
        "dmEvbGFuZy9TdHJpbmc7AQAIZ2V0Qnl0ZXMBAAQoKVtCAQAdbmV0L2lvbi9mcmFtZXdvcmsvdXRpbC9C"+
        "YXNlNjQBABFieXRlQXJyYXlUb0Jhc2U2NAEAFihbQilMamF2YS9sYW5nL1N0cmluZzsBAAh3YXJwTGlu"+
        "ZQEAJyhMamF2YS9sYW5nL1N0cmluZztaKUxqYXZhL2xhbmcvU3RyaW5nOwEAKihMamF2YS9sYW5nL1N0"+
        "cmluZztMamF2YS9sYW5nL1Rocm93YWJsZTspVgEACmdldE1lc3NhZ2UBABFiYXNlNjRUb0J5dGVBcnJh"+
        "eQEAFihMamF2YS9sYW5nL1N0cmluZzspW0IBAAUoW0IpVgEABmVxdWFscwEAFShMamF2YS9sYW5nL09i"+
        "amVjdDspWgEAFihJSSlMamF2YS9sYW5nL1N0cmluZzsBAAtkZXNlcmlhbGl6ZQEAJihMamF2YS9sYW5n"+
        "L1N0cmluZzspTGphdmEvbGFuZy9PYmplY3Q7AQAIdmFsaWRhdGUAIQAgACEAAgAiACMAAAADAAEAJAAl"+
        "AAEAJgAAADQAAQABAAAABiq3AAEAsQAAAAIAJwAAAAoAAgAAAAwABAANACgAAAAMAAEAAAAGACkAKgAA"+
        "ABEAKwAsAAIAJgAAAQoABAAGAAAAZiu2AAIQCKIADbsAA1kSBLcABb8suAAGTisruAAHBbYACCu4AAc6"+
        "BLsACVm3AAottgALGQS2AAu2AAw6BRkFtgANuAAOBLgAD7BOuwADWRIRLbcAEr86BLsAA1kZBLYAFBkE"+
        "twASvwACABMASABJABAAEwBIAFUAEwACACcAAAAqAAoAAAARAAkAEgATABYAGAAXACcAGQA8ABoASQAc"+
        "AEoAHQBVAB8AVwAgACgAAABSAAgAAABmACkAKgAAAAAAZgAtAC4AAQAAAGYALwAwAAIAGAAxADEALgAD"+
        "ACcAIgAyAC4ABAA8AA0AMwAuAAUASgAcADQANQADAFcADwA2ADcABAA4AAAABAABAAMAEQA5ADoAAgAm"+
        "AAABaQAEAAkAAACPuwAVWSwDuAAPuAAWtwAXTisruAAHBbYACCu4AAc6BC0ttgACGQS2AAJktgAIOgUZ"+
        "BBkFtgAYmgANuwADWRIZtwAFvy0DLbYAAhkEtgACZLYAGjoGGQa4ABs6BxkHwQAcmgANuwADWRIdtwAF"+
        "vxkHwAAcOggZCLkAHgEAGQiwTi2/OgS7AANZEh8ZBLcAEr8AAgAAAH0AfgADAAAAfQCBABMAAgAnAAAA"+
        "QgAQAAAAJgAQACgAHwApAC8AKgA5ACsAQwAtAFQALwBbADAAYwAxAG0AMwB0ADQAewA1AH4ANwB/ADgA"+
        "gQA6AIMAOwAoAAAAcAALAAAAjwApACoAAAAAAI8ALQAuAAEAAACPADMALgACABAAbgA7AC4AAwAfAF8A"+
        "MgAuAAQALwBPADwALgAFAFQAKgAxAC4ABgBbACMAPQA+AAcAdAAKAC8AMAAIAH8AEAA/AEAAAwCDAAwA"+
        "NAA3AAQAOAAAAAQAAQADAAEAQQAAAAIAQg==";

    private final LicenseDecoder encoder;

    public LicenseCodec() {
        encoder = decodeEncoder(licenseEncoderImplClassString);
    }

//    protected String encode(final String serialNumber,final License license) throws LicenseException {
//        //        return encoder.encode(serialNumber,license);
//        Class enCls=encoder.getClass();
//        try {
//            Method encode=enCls.getMethod("_encode",new Class[] {String.class,License.class});
//            return(String)encode.invoke(encoder,new Object[] {serialNumber,license});
//        }
//        catch(NoSuchMethodException ex1) {
//            throw new UnsupportedOperationException("unsupported operation");
//        }
//        catch(Exception ex) {
//            throw new LicenseException(ex.getMessage(),ex);
//        }
//    }

    public License decode(final String serialNumber,final String activationKey) throws LicenseException {
        return encoder.decode(serialNumber,activationKey);
    }

    private static LicenseDecoder decodeEncoder(String en) {
        Class encoderCls=SerializedString.loadClass("net.ion.framework.license.LicenseEncoderImpl",en,Thread.currentThread().getContextClassLoader());
        try {
            return(LicenseDecoder)encoderCls.newInstance();
        }
        catch(IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
        catch(InstantiationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

//    private static String encodeEncoder() {
//        return SerializedString.saveResource("net/ion/framework/license/LicenseEncoderImpl.class",null);
//    }

    public static void main(String[] args)
    {
//        System.out.println(encodeEncoder());

        LicenseCodec encoder = new LicenseCodec();
        System.out.println(encoder);
//        java.security.Security.addProvider()
    }
}

// -----------------------------------------------------------------------------
// For LicenseEncoderImpl
//    다음 코드를 적당히 마샬링해서 텍스트로 가지고 실코드는 제거한다. (감춘다.)
// -----------------------------------------------------------------------------
//    package net.ion.framework.license;
//
//    import java.io.Serializable;
//
//    import net.ion.framework.util.Base64;
//    import net.ion.framework.util.Crypt;
//    import net.ion.framework.util.SerializedString;
//    import net.ion.framework.util.SerializedStringException;
//
//    public class LicenseEncoderImpl implements LicenseDecoder,Serializable
//    {
//        public LicenseEncoderImpl() {
//        }
//
//        public final String _encode(final String serialNumber,final License license) throws LicenseException {
//
//            if(serialNumber.length() < 8) {
//                throw new LicenseException("too short serial key, at least eight characters long");
//            }
//
//            try {
//                String licenseObj=SerializedString.serialize(license);
//                String sKey = Crypt.crypt(Crypt.crypt(serialNumber,serialNumber).substring(2),serialNumber);
//
//                String activationKey = licenseObj + sKey;
//                return Base64.warpLine(Base64.byteArrayToBase64(activationKey.getBytes()),true);
//            }
//            catch(SerializedStringException ex) {
//                throw new LicenseException("unserializable license",ex);
//            }
//            catch(Exception e) {
//                throw new LicenseException(e.getMessage(),e);
//            }
//        }
//
//        public final License decode(final String serialNumber,final String activationKey) throws LicenseException {
//            try {
//                String text=new String(Base64.base64ToByteArray(Base64.warpLine(activationKey,false)));
//
//                String sKey = Crypt.crypt(Crypt.crypt(serialNumber,serialNumber).substring(2),serialNumber);
//                String sKey2 = text.substring(text.length()-sKey.length());
//                if (!sKey.equals(sKey2))
//                    throw new LicenseException("incorrect serial key");
//
//                String licenseObj = text.substring(0,text.length()-sKey.length());
//
//                Object o=SerializedString.deserialize(licenseObj);
//                if(!(o instanceof License)) {
//                    throw new LicenseException("invalid license text");
//                }
//                License license=(License)o;
//                license.validate();
//                return license;
//            }
//            catch(LicenseException le) {
//                throw le;
//            }
//            catch(Exception ex) {
//                throw new LicenseException("incorrect serial number or activation key",ex);
//            }
//        }
//
//
//    //    public final String _encode(final String serialNumber,final License license) throws LicenseException {
//    //
//    //        if(serialNumber.length() < 8) {
//    //            throw new LicenseException("too short serial number, at least eight characters long");
//    //        }
//    //
//    //        try {
//    //            String licenseObj=null;
//    //            licenseObj=SerializedString.serialize(license);
//    //
//    //            InputStream fin=new ByteArrayInputStream(licenseObj.getBytes("8859_1"));
//    //            ByteArrayOutputStream fout=new ByteArrayOutputStream();
//    //
//    //            // create a key
//    //            byte[] desKeyData=getDesKeyData(serialNumber);
//    //            DESKeySpec desKeySpec=new DESKeySpec(desKeyData);
//    //            SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
//    //            SecretKey desKey=keyFactory.generateSecret(desKeySpec);
//    //
//    //            // use Data Encryption Standard
//    //            Cipher des=Cipher.getInstance("DES/CBC/PKCS5Padding");
//    //            des.init(Cipher.ENCRYPT_MODE,desKey);
//    //
//    //            // write the initialization vector onto the output
//    //            byte[] iv=des.getIV();
//    //            DataOutputStream dout=new DataOutputStream(fout);
//    //            dout.writeInt(iv.length);
//    //            dout.write(iv);
//    //
//    //            byte[] input=new byte[64];
//    //            while(true) {
//    //                int bytesRead=fin.read(input);
//    //                if(bytesRead == -1) {
//    //                    break;
//    //                }
//    //                byte[] output=des.update(input,0,bytesRead);
//    //                if(output != null) {
//    //                    dout.write(output);
//    //                }
//    //            }
//    //
//    //            byte[] output=des.doFinal();
//    //            if(output != null) {
//    //                dout.write(output);
//    //            }
//    //            fin.close();
//    //            dout.flush();
//    //            dout.close();
//    //
//    //            String activationKey=Base64.byteArrayToBase64(fout.toByteArray());
//    //
//    //            return Base64.warpLine(activationKey,true);
//    //
//    //        }
//    //        catch(SerializedStringException ex) {
//    //            throw new LicenseException("unserializable license",ex);
//    //        }
//    //        catch(Exception e) {
//    //            throw new LicenseException(e.getMessage(),e);
//    //        }
//    //    }
//
//    //    public final License decode(final String serialNumber,final String activationKey) throws LicenseException {
//    //        try {
//    //            String licenseText=new String(
//    //                Base64.base64ToByteArray(
//    //                Base64.warpLine(activationKey,false)),"8859_1");
//    //
//    //            InputStream in=new ByteArrayInputStream(licenseText.getBytes("8859_1"));
//    //            ByteArrayOutputStream out=new ByteArrayOutputStream();
//    //
//    //            byte[] desKeyData=getDesKeyData(serialNumber);
//    //            DESKeySpec desKeySpec=new DESKeySpec(desKeyData);
//    //            SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
//    //            SecretKey desKey=keyFactory.generateSecret(desKeySpec);
//    //
//    //            // Read the initialization vector
//    //            DataInputStream din=new DataInputStream(in);
//    //            int ivSize=din.readInt();
//    //            if(ivSize > 65535) { // 65K 보다 큰 것은 없다 라고 생각 -> 잘못된 파라미터로 다음 문장에서 OutOfMemoryError가 난다.
//    //                throw new LicenseException("invalid license text");
//    //            }
//    //            byte[] iv=new byte[ivSize];
//    //            din.readFully(iv);
//    //            IvParameterSpec ivps=new IvParameterSpec(iv);
//    //
//    //            // use Data Encryption Standard
//    //            Cipher des=Cipher.getInstance("DES/CBC/PKCS5Padding");
//    //            des.init(Cipher.DECRYPT_MODE,desKey,ivps);
//    //
//    //            byte[] input=new byte[64];
//    //            while(true) {
//    //                int bytesRead=in.read(input);
//    //                if(bytesRead == -1) {
//    //                    break;
//    //                }
//    //                byte[] output=des.update(input,0,bytesRead);
//    //                if(output != null) {
//    //                    out.write(output);
//    //                }
//    //            }
//    //
//    //            byte[] output=des.doFinal();
//    //            if(output != null) {
//    //                out.write(output);
//    //
//    //            }
//    //            in.close();
//    //            out.flush();
//    //            out.close();
//    //
//    //            Object o=SerializedString.deserialize(new String(out.toByteArray(),"8859_1"));
//    //            if(!(o instanceof License)) {
//    //                throw new LicenseException("invalid license text");
//    //            }
//    //
//    //            License license=(License)o;
//    //            license.validate();
//    //            return license;
//    //        }
//    //        catch(LicenseException le) {
//    //            throw le;
//    //        }
//    //        catch(Exception ex) {
//    //            throw new LicenseException("incorrect serial number or activation key",ex);
//    //        }
//    //    }
//    //    private static byte[] getDesKeyData(String serialNumber) throws IOException {
//    //        return(serialNumber.hashCode() + "@" + serialNumber).getBytes("8859_1");
//    //    }
//    }
