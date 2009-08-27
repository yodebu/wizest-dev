/*
 * Created on 2004. 8. 16
 */
package wizest.fx.mesh;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import wizest.fx.logging.LogBroker;
import wizest.fx.util.Context;


/**
 * @author user
 */
public class MeshRuntime {
    private static Logger log = LogBroker.getLogger(MeshRuntime.class);

    private final Registries registries;
    private final MeshContext meshContext;
    private final Mesh mesh;
    private final ClassLoader loader;

    private MeshInitializer initiializer;

    public MeshRuntime(MeshInitializer initiializer, Registries registries, Context parent, ClassLoader partClsLoader) {
        this.initiializer = initiializer;
        this.registries = registries;
        this.loader = partClsLoader;
        this.mesh = new Mesh();
        this.meshContext = new MeshContext(parent, this);
    }

    public MeshContext getMeshContext() {
        return this.meshContext;
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public Registries getRegisteries() {
        return this.registries;
    }

    boolean started = false;

    public synchronized void start() throws OperationException {
        if (started)
            throw new OperationException("once started before");
        started = true;

        initiializer.initialize(this);

        fireAfterMeshRuntimeStart();
    }

    boolean disposed = false;

    public synchronized void dispose() {
        if ((started && disposed) || !started)
            return;

        disposed = true;

        fireBeforeMeshRuntimeDispose();

        Iterator iterator = getCurrentPartPacks();
        while (iterator.hasNext()) {
            PartPack partPack = (PartPack) iterator.next();
            removePartPack(partPack.partQName);
        }

        mesh.disconnectAll();

        meshContext.release();
    }

    public synchronized PartPack askPartPack(String partId, String partName) throws OperationException {
        PartPack partPack = getCurrentPartPack(PartPack.makePartQName(partId, partName));

        if (partPack == null) {
            try {
                PartDescriptor desc = registries.getPartDescriptor(partId);
                Class cls = desc.getPartClass(loader);
                Part part = (Part) cls.newInstance();
                PartContext partContext = new PartContext(meshContext, part.getPartId(), partName);
                part.start(partContext);

                partPack = new PartPack(part, partContext, partName);
                addPartPack(partPack);
            } catch (Exception e) {
                throw new OperationException(e);
            }
        }

        return partPack;
    }

    public synchronized void removePartPack(String partQName) {
        PartPack partPack = getCurrentPartPack(partQName);
        // 현재 있을 경우만
        if (partPack != null) {
            fireBeforeRemovePartPack(partPack);

            fPartPacks.remove(partPack.partQName);
            mesh.disconnectPartPacks(partPack);
            try {
                partPack.part.dispose();
            } catch (OperationException e) {
                log.severe(e.getMessage() + ":partQName=" + partPack.partQName + ",part=" + partPack.part + ",partContext=" + partPack.partContext);
            }
            partPack.partContext.release();
        }
    }

    public Iterator getCurrentPartPacks() {
        return fPartPacks.values().iterator();
    }

    public PartPack getCurrentPartPack(String partQName) {
        return (PartPack) fPartPacks.get(partQName);
    }

    private transient Map fPartPacks = new HashMap();

    private void addPartPack(PartPack partPack) {
        if (!fPartPacks.containsKey(partPack.partQName)) {
            fPartPacks.put(partPack.partQName, partPack);

            fireAfterAddNewPartPack(partPack);
        }
    }

    private transient Vector fMeshRuntimeListeners;

    public synchronized void addMeshRuntimeListener(MeshRuntimeListener l) {
        Vector v = fMeshRuntimeListeners == null ? new Vector(2) : (Vector) fMeshRuntimeListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            fMeshRuntimeListeners = v;
        }
    }

    public synchronized void removeMeshRuntimeListener(MeshRuntimeListener l) {
        if (fMeshRuntimeListeners != null && fMeshRuntimeListeners.contains(l)) {
            Vector v = (Vector) fMeshRuntimeListeners.clone();
            v.removeElement(l);
            fMeshRuntimeListeners = v;
        }
    }

    protected void fireAfterMeshRuntimeStart() {
        if (fMeshRuntimeListeners != null) {
            Vector listeners = fMeshRuntimeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshRuntimeListener) listeners.elementAt(i)).afterMeshRuntimeStart(this);
            }
        }
    }

    protected void fireBeforeMeshRuntimeDispose() {
        if (fMeshRuntimeListeners != null) {
            Vector listeners = fMeshRuntimeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshRuntimeListener) listeners.elementAt(i)).beforeMeshRuntimeDispose(this);
            }
        }
    }

    protected void fireAfterAddNewPartPack(PartPack partPack) {
        if (fMeshRuntimeListeners != null) {
            Vector listeners = fMeshRuntimeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshRuntimeListener) listeners.elementAt(i)).afterAddNewPartPack(this, partPack);
            }
        }
    }

    protected void fireBeforeRemovePartPack(PartPack partPack) {
        if (fMeshRuntimeListeners != null) {
            Vector listeners = fMeshRuntimeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshRuntimeListener) listeners.elementAt(i)).beforeRemovePartPack(this, partPack);
            }
        }
    }
}

