package wizest.fx.license;

import java.io.Serializable;
import java.util.Date;

/**
 * ����: ��� license Ŭ������ ���� ���� �����ڸ� public���� ��������Ѵ�!!
 */
public interface License extends Serializable // <-- �̰� ���̼��� Ŭ�������� ������ �߿��� �Ӽ��̴�!!! (��̷� ���� �Ӽ��� �ƴ϶� ���̴�. �ø�����������̼ǿ� ���õ� �޼ҵ带 Ȯ���ϰ� ������������Ѵ�!)
{
    /**
     * LicenseEncoder�� License�� decode�� �� invoke�ȴ�.
     * @throws LicenseException ��ȿ�� ���̼����� �ƴ� ��� �߻�
     */
    void validate() throws LicenseException;

    // ��ǰ ����
    String getProduct();

    String getVersion();

    // ���� ����
    String getLicenseFrom();

    String getLicenseTo();

    Date getIssueDate();

    /**
     * ���̼����� ���� ��ü���� ������ ���´�.
     * display�� text�� ����Ѵ�.
     * @return String
     */
    String getLicenseDescription();

    // ��ȿ �Ⱓ
    Date getBeginDate();

    Date getEndDate();

    String[] getValueKeys();
    boolean hasValue(String key);

    /**
     * @param function String null if invalid or inaccessable key
     * @return Object
     */
    Object getValue(String key);

    // ���� ���� �پ��� ����
    String getValueAsString(String key);

    /**
     * @param key String
     * @return int 0 if error
     */
    int getValueAsInt(String key);
}
