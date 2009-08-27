package wizest.fx.license;

import java.io.Serializable;
import java.util.Date;

/**
 * 조건: 모든 license 클래스는 인자 없는 생성자를 public으로 열어놔야한다!!
 */
public interface License extends Serializable // <-- 이거 라이센스 클래스에서 굉장히 중요한 속성이다!!! (재미로 붙인 속성이 아니란 말이다. 시리얼라이이제이션에 관련된 메소드를 확실하게 구현시켜줘야한다!)
{
    /**
     * LicenseEncoder가 License를 decode한 후 invoke된다.
     * @throws LicenseException 유효한 라이센스가 아닐 경우 발생
     */
    void validate() throws LicenseException;

    // 제품 관련
    String getProduct();

    String getVersion();

    // 발행 관련
    String getLicenseFrom();

    String getLicenseTo();

    Date getIssueDate();

    /**
     * 라이센스에 대한 전체적인 설명을 적는다.
     * display용 text로 사용한다.
     * @return String
     */
    String getLicenseDescription();

    // 유효 기간
    Date getBeginDate();

    Date getEndDate();

    String[] getValueKeys();
    boolean hasValue(String key);

    /**
     * @param function String null if invalid or inaccessable key
     * @return Object
     */
    Object getValue(String key);

    // 위에 것의 다양한 형태
    String getValueAsString(String key);

    /**
     * @param key String
     * @return int 0 if error
     */
    int getValueAsInt(String key);
}
