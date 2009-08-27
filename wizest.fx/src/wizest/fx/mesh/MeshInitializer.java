/*
 * Created on 2004. 8. 18
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface MeshInitializer {
    /**
     * MeshRuntime.start() 가 불러졌을 때 호출 됩니다. afterMeshRuntimeStart 이벤트 보다 이전에
     * 실행됩니다.
     * 
     * @param runtime
     * @throws OperationException
     */
    void initialize(MeshRuntime runtime) throws OperationException;
}