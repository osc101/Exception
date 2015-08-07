package com.ldd600.exception.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class AnnotationClassVisitor implements ClassVisitor {
    private List<String> visibleAnnotationNames;

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (visible) {
            if (visibleAnnotationNames == null) {
                visibleAnnotationNames = new ArrayList<String>(1);
            }
            Type t = Type.getType(desc);
            visibleAnnotationNames.add(t.getClassName());
        }
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitEnd() {
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return null;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return null;
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public void visitSource(String source, String debug) {
    }

    public List<String> getVisibleAnnotationNames() {
        if(null == visibleAnnotationNames) {
            visibleAnnotationNames = Collections.<String>emptyList();
        }
        return visibleAnnotationNames;
    }
}
