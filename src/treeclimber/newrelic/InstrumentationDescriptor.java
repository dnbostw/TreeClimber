/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.newrelic;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author dnbostw
 */
public class InstrumentationDescriptor implements Editable<InstrumentationDescriptor> {
    private String extensionName;
    private String metricPrefix;
    private String name;
    private boolean enabled;
    private String version;
    private Set<ClassDescriptor> pointcuts;

    @Override
    public InstrumentationDescriptor copy(InstrumentationDescriptor c2) {
        if (c2 == null) {
            c2 = new InstrumentationDescriptor();
        }
        c2.setExtensionName(this.getExtensionName());
        c2.setMetricPrefix(this.getMetricPrefix());
        c2.setEnabled(this.isEnabled());
        c2.setVersion(this.getVersion());
        c2.setPointcuts(this.getPointcuts());
        return c2;
    }
    
    public InstrumentationDescriptor() {
    }

    public InstrumentationDescriptor(String extensionName, boolean enabled, String version) {
        this.extensionName = extensionName;
        this.enabled = enabled;
        this.version = version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getMetricPrefix() {
        return metricPrefix;
    }

    public void setMetricPrefix(String metricPrefix) {
        this.metricPrefix = metricPrefix;
    }

    public Set<ClassDescriptor> getPointcuts() {
        if (pointcuts == null) {
            pointcuts = new HashSet<ClassDescriptor>();
        }
        return pointcuts;
    }

    public void setPointcuts(Set<ClassDescriptor> pointcuts) {
        this.pointcuts = pointcuts;
    }
    
    public void addPointcut(ClassDescriptor pointcut) {
        getPointcuts().add(pointcut);
    }
    
    public void clearPointcuts() {
        this.pointcuts.clear();
    }

    public String toString() {
        /*
        <?xml version="1.0" encoding="UTF-8"?>
          <extension xmlns="https://newrelic.com/docs/java/xsd/v1.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="newrelic-extension extension.xsd "
                name="?extensionName?" version="?version?">
            <instrumentation metricPrefix="?metricPrefix?">
                #classInstrumentationList#
            </instrumentation>
          </extension>

        */
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("  <extension xmlns=\"https://newrelic.com/docs/java/xsd/v1.0\"\n");
        sb.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        sb.append("    xsi:schemaLocation=\"newrelic-extension extension.xsd\"\n");
        if (getExtensionName() != null) {
            sb.append("    name=\""+getExtensionName()+"\"\n");
        }
        sb.append("    version=");
        if (getVersion() == null) {
            sb.append("\"1.0\"");
        } else {
            sb.append("\""+getVersion()+"\"");
        };
        sb.append("    enabled=\""+isEnabled()+"\">\n");
        sb.append("    <instrumentation");
        if (getMetricPrefix() != null) {
            sb.append(" metricPrefix=\""+getMetricPrefix()+"\"");
        }
        sb.append(">\n");
        if (getPointcuts() != null) {
            for (ClassDescriptor clazz : getPointcuts()) {
                sb.append(clazz.toString());
            }
        }
        sb.append("    </instrumentation>\n");
        sb.append("  </extension>\n");
        return sb.toString();
        
    }

}
