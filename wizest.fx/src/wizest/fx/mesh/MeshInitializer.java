/*
 * Created on 2004. 8. 18
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface MeshInitializer {
    /**
     * MeshRuntime.start() �� �ҷ����� �� ȣ�� �˴ϴ�. afterMeshRuntimeStart �̺�Ʈ ���� ������
     * ����˴ϴ�.
     * 
     * @param runtime
     * @throws OperationException
     */
    void initialize(MeshRuntime runtime) throws OperationException;
}