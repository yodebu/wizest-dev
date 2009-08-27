/*
 * Created on 2004. 8. 11
 */
package wizest.fx.mesh;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author user
 */
public class Registries {
    private LinkedHashMap descriptors;

    public Registries() {
        this.descriptors = new LinkedHashMap();
    }

    public Registries(PartDescriptor[] dess) {
        this();
        for (int i = 0, length = dess.length; i < length; ++i)
            registerPartDescriptor(dess[i]);
    }

    public synchronized void registerPartDescriptor(PartDescriptor descriptor) {
        descriptors.put(descriptor.getPartId(), descriptor);
    }

    public synchronized PartDescriptor[] getPartDescriptors() {
        return (PartDescriptor[]) descriptors.values().toArray(new PartDescriptor[0]);
    }

    public PartDescriptor[] findPartDescriptors(PartFilter filter) {
        ArrayList list = new ArrayList();
        PartDescriptor[] ds = getPartDescriptors();
        for (int i = 0, length = ds.length; i < length; ++i) {
            if (filter.filterPart(ds[i])) {
                list.add(ds[i]);
            }
        }
        return (PartDescriptor[]) list.toArray(new PartDescriptor[0]);
    }

    public PartDescriptor getPartDescriptor(String partId) {
        return (PartDescriptor) descriptors.get(partId);
    }
}

