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
public class MethodDescriptor implements Editable<MethodDescriptor> {

    private String name;
    private Set<ParameterDescriptor> parameters;

    @Override
    public MethodDescriptor copy(MethodDescriptor c2) {
        if (c2 == null) {
            c2 = new MethodDescriptor();
        }
        c2.setName(this.getName());
        c2.setParameters(this.getParameters());
        return c2;
    }
    
    
    public MethodDescriptor() {
    }

    public MethodDescriptor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ParameterDescriptor> getParameters() {
        if (parameters == null) {
            parameters = new HashSet<ParameterDescriptor>();
        }
        return parameters;
    }

    public void setParameters(Set<ParameterDescriptor> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(ParameterDescriptor param) {
        getParameters().add(param);
    }

    public void clearParameters() {
        parameters.clear();
    }

    public String toString() {
        /*
              <method>
                <name>?methodName?</name>
                <parameters>
                #parameterList#
                </parameters>
              </method>
         */
        StringBuilder sb = new StringBuilder();
        sb.append("          <method>\n");
        sb.append("            <name>");
        sb.append(getName());
        sb.append("</name>\n");
        if (getParameters().size() > 0) {
            boolean show = false;
            for (ParameterDescriptor param  : getParameters()) {
                if (param.isShowable()) {
                    show = true;
                    break;
                }
            }
            if (show) {
                sb.append("            <parameters>\n");
                for (ParameterDescriptor param : getParameters()) {
                    sb.append("              " + param.toString() + "\n");
                }
                sb.append("            </parameters>\n");
            }
        }
        sb.append("          </method>\n");
        return sb.toString();
    }
}
