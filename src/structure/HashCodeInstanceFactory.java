package structure;

import grafo.optilib.structure.InstanceFactory;

public class HashCodeInstanceFactory extends InstanceFactory<HashCodeInstance> {
    @Override
    public HashCodeInstance readInstance(String s) {
        return new HashCodeInstance(s);
    }
}
