/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeclimber.newrelic;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import treeclimber.Core;
import treeclimber.components.EditExcludesPanel;

/**
 *
 * @author dnbostw
 */
public class ClassDescriptor implements Editable<ClassDescriptor> {

    private boolean transactionStartPoint; // true or false
    private String metricNameFormat;
    private boolean excludeFromTransactionTrace; // true or false
    private boolean ignoreTransaction; // true or false
    private String transactionType; // default='background', or 'web'
    private String nameTransaction;
    private String methodAnnotation;
    private String className;
    private String interfaceName;
    private Set<MethodDescriptor> methods;

    @Override
    public ClassDescriptor copy(ClassDescriptor c2) {
        if (c2 == null) {
            c2 = new ClassDescriptor();
        }
        c2.setTransactionStartPoint(this.isTransactionStartPoint());
        c2.setMetricNameFormat(this.getMetricNameFormat());
        c2.setExcludeFromTransactionTrace(this.isExcludeFromTransactionTrace());
        c2.setIgnoreTransaction(this.isIgnoreTransaction());
        c2.setTransactionType(this.getTransactionType());
        c2.setNameTransaction(this.getNameTransaction());
        c2.setMetricNameFormat(this.getMethodAnnotation());
        c2.setClassName(this.getClassName());
        c2.setInterfaceName(this.getInterfaceName());
        // Only a shared dup, no deep copy.  Main purpose of editable is to allow
        // editing of non-collectionfields.
        c2.setMethods(this.getMethods());
        return c2;
    }

    public ClassDescriptor() {
    }

    public ClassDescriptor(String className) {
        this.className = className;
    }
    
    public ClassDescriptor(String className, List<String> methodsInfo) {
        this(className);
        for (String method : methodsInfo) {
            String methodName = method.substring(0,method.indexOf("("));
            MethodDescriptor methodDescriptor = new MethodDescriptor(methodName);
            String p = method.substring(method.indexOf("(")+1, method.indexOf(")"));
            String[] params = p.split(",");
            for (int i = 0; i < params.length; i++) {
                String type = params[i];
                String name = "param".concat(""+i);
                methodDescriptor.addParameter(new ParameterDescriptor(name, type));
            }
            getMethods().add(methodDescriptor);
        }
    }

    public String getNameTransaction() {
        return nameTransaction;
    }

    public void setNameTransaction(String nameTransaction) {
        this.nameTransaction = nameTransaction;
    }

    public String getMethodAnnotation() {
        return methodAnnotation;
    }

    public void setMethodAnnotation(String methodAnnotation) {
        this.methodAnnotation = methodAnnotation;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public boolean isTransactionStartPoint() {
        return transactionStartPoint;
    }

    public void setTransactionStartPoint(boolean transactionStartPoint) {
        this.transactionStartPoint = transactionStartPoint;
    }

    public String getMetricNameFormat() {
        return metricNameFormat;
    }

    public void setMetricNameFormat(String metricNameFormat) {
        this.metricNameFormat = metricNameFormat;
    }

    public boolean isExcludeFromTransactionTrace() {
        return excludeFromTransactionTrace;
    }

    public void setExcludeFromTransactionTrace(boolean excludeFromTransaction) {
        this.excludeFromTransactionTrace = excludeFromTransaction;
    }

    public boolean isIgnoreTransaction() {
        return ignoreTransaction;
    }

    public void setIgnoreTransaction(boolean ignoreTransaction) {
        this.ignoreTransaction = ignoreTransaction;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Set<MethodDescriptor> getMethods() {
        if (methods == null) {
            methods = new HashSet<MethodDescriptor>();
        }
        return methods;
    }

    public void setMethods(Set<MethodDescriptor> methods) {
        this.methods = methods;
    }

    public void addMethod(MethodDescriptor method) {
        getMethods().add(method);
    }

    public void clearMethods() {
        this.methods.clear();
    }

    public String toString() {
        /*
    <pointcut transactionStartPoint="?transactionStartPoint?" 
        ignoreTransaction="?ignoreTransaction?"
        excludeFromTransactionTrace="?excludeFromTransactionTrace?"
        metricNameFormat="?metricNameFormat?">

        <className>?className?</className>
        #methodList#
    </pointcut>
         */
        StringBuilder sb = new StringBuilder();
        sb.append("      <pointcut ");
        sb.append("transactionStartPoint=\"");
        sb.append(isTransactionStartPoint());
        sb.append("\"\n");

        sb.append("        ignoreTransaction=\"");
        sb.append(isIgnoreTransaction());
        sb.append("\"\n");

        if (getMetricNameFormat() != null) {
            sb.append("        metricNameFormat=\"");
            sb.append(getMetricNameFormat());
            sb.append("\"\n");
        }

        if (getTransactionType() != null) {
            sb.append("        transactionType=\"");
            sb.append(getTransactionType());
            sb.append("\"\n");
        }

        sb.append("        excludeFromTransactionTrace=\"");
        sb.append(isExcludeFromTransactionTrace());
        sb.append("\">\n");
        
        if (getNameTransaction() != null) {
            sb.append("        <nameTransaction>");
            sb.append(getNameTransaction());
            sb.append("</nameTransaction>\n");
        }

        if (getMethodAnnotation() != null) {
            sb.append("        <methodAnnotaion>");
            sb.append(getMethodAnnotation());
            sb.append("</methodAnnotation>\n");
        }

        if (getClassName() != null && getInterfaceName() == null) {
            sb.append("        <className>");
            sb.append(getClassName());
            sb.append("</className>\n");
        }

        if (getInterfaceName() != null) {
            sb.append("        <interfaceName>");
            sb.append(getInterfaceName());
            sb.append("</interfaceName>\n");
        }
        
        if (getMethods().size() > 0) {
            for (MethodDescriptor method : getMethods()) {
                sb.append(method.toString());
            }
        }

        sb.append("      </pointcut>\n");
        return sb.toString();
    }

}
