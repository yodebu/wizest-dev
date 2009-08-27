/**
 * Created on 2004. 8. 16
 */
package wizest.fx.mesh;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * @author Sanghoon, Kim
 */
public class Mesh {

    private Map forwardLinkMap; // forward link: key=partQName, value=partPack
    private Map reverseLinkMap; // reverse link: key=partQName, value=partPack

    private final static Vector EMPTY_VECTOR = new Vector();

    public Mesh() {
        forwardLinkMap = new HashMap();
    }

    public synchronized void connectPartPack(PartPack from, PartPack to) {
        // forward
        Vector v = (Vector) forwardLinkMap.get(from.partQName);
        if (v == null) {
            v = new Vector();
            forwardLinkMap.put(from.partQName, v);
        }

        // 이전에 연결이 없을 경우에만
        if (!v.contains(to)) {
            v.add(to);

            // reverse
            v = (Vector) reverseLinkMap.get(to.partQName);
            if (v == null) {
                v = new Vector();
                reverseLinkMap.put(to.partQName, v);
            }
            v.add(from);

            firePartConnected(new MeshEvent(this, from, to));
        }
    }

    public synchronized void disconnectPartPack(PartPack from, PartPack to) {
        // forward
        Vector v = (Vector) forwardLinkMap.get(from.partQName);
        if (v != null) {
            // 이전에 연결이 있을 경우에만
            if (v.contains(to)) {
                v.remove(to);
                if (v.isEmpty())
                    forwardLinkMap.remove(from.partQName);

                // reverse
                v = (Vector) reverseLinkMap.get(to.partQName);
                if (v != null) {
                    v.remove(from);
                    if (v.isEmpty())
                        reverseLinkMap.remove(to.partQName);
                }

                firePartDisconnected(new MeshEvent(this, from, to));

                if (isIsolated(from.partQName))
                    firePartIsolated(new MeshEvent(this, from));
                if (isIsolated(to.partQName))
                    firePartIsolated(new MeshEvent(this, to));
            }
        }

    }

    public synchronized void disconnectPartPacks(PartPack partPack) {
        // from
        Iterator i = getListenPartPacks(partPack.partQName);
        while (i.hasNext()) {
            PartPack to = (PartPack) i.next();
            disconnectPartPack(partPack, to);
        }

        // to
        i = getFirePartPacks(partPack.partQName);
        while (i.hasNext()) {
            PartPack from = (PartPack) i.next();
            disconnectPartPack(from, partPack);
        }
    }

    public synchronized void disconnectAll() {
        Iterator i = getMeshedPartPacks();
        while (i.hasNext()) {
            disconnectPartPacks((PartPack) i.next());
        }
    }

    public synchronized Iterator getListenPartPacks(String fromPartQName) {
        Object to = forwardLinkMap.get(fromPartQName);
        return (to == null) ? EMPTY_VECTOR.iterator() : ((Vector) to).iterator();
    }

    public synchronized Iterator getFirePartPacks(String toPartQName) {
        Object from = reverseLinkMap.get(toPartQName);
        return (from == null) ? EMPTY_VECTOR.iterator() : ((Vector) from).iterator();
    }

    public synchronized Iterator getMeshedPartPacks() {
        return new HashSet(forwardLinkMap.values()).iterator();
    }

    public synchronized boolean isIsolated(String partQName) {
        return (!getListenPartPacks(partQName).hasNext()) && (!getFirePartPacks(partQName).hasNext());
    }

    public synchronized boolean isAllDisconnected() {
        return !getMeshedPartPacks().hasNext();
    }

    private transient Vector fMeshListeners;

    public synchronized void addMeshListener(MeshListener l) {
        Vector v = fMeshListeners == null ? new Vector(2) : (Vector) fMeshListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            fMeshListeners = v;
        }
    }

    public synchronized void removeMeshListener(MeshListener l) {
        if (fMeshListeners != null && fMeshListeners.contains(l)) {
            Vector v = (Vector) fMeshListeners.clone();
            v.removeElement(l);
            fMeshListeners = v;
        }
    }

    protected void firePartConnected(MeshEvent e) {
        if (fMeshListeners != null) {
            Vector listeners = fMeshListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshListener) listeners.elementAt(i)).partConnected(e);
            }
        }
    }

    protected void firePartDisconnected(MeshEvent e) {
        if (fMeshListeners != null) {
            Vector listeners = fMeshListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshListener) listeners.elementAt(i)).partDisconnected(e);
            }
        }
    }

    protected void firePartIsolated(MeshEvent e) {
        if (fMeshListeners != null) {
            Vector listeners = fMeshListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((MeshListener) listeners.elementAt(i)).partIsolated(e);
            }
        }
    }
}