/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.newrelic;

import java.util.Objects;

/**
 *
 * @author dnbostw
 */
public class ParameterDescriptor implements Editable<ParameterDescriptor> {
    private String name;
    private String type;

    @Override
    public ParameterDescriptor copy(ParameterDescriptor c2) {
        if (c2 == null) {
            c2 = new ParameterDescriptor();
        }
        c2.setName(this.getName());
        c2.setType(this.getType());
        return c2;
    }

    public ParameterDescriptor() {
    }

    public ParameterDescriptor(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public boolean isShowable() {
        return name != null && name.length() > 0 && type != null && type.length() > 0;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // <type attributeName="?paramName?">?paramType?</type>

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<type attributeName=\"");
        sb.append(getName());
        sb.append("\">");
        sb.append(getType());
        sb.append("</type>");
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParameterDescriptor other = (ParameterDescriptor) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }
    
}
