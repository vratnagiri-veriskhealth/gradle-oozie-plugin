package org.github.mansur.oozie.beans

import groovy.xml.MarkupBuilder;

import java.io.Serializable;


abstract class XmlCapable extends GroovyObjectSupport implements Serializable {
	private static final long serialVersionUID = 1L

	public abstract void buildXml(MarkupBuilder xml)

	private Object valueOrClosure(Object val){
		if(val instanceof Closure){
			val.setDelegate(this)
			val.setResolveStrategy(Closure.DELEGATE_FIRST);
			return val.call();
		}else{
			return val;
		}
	}

	@Override
	public Object invokeMethod(String name, Object args) {
		String[] names=metaClass.properties*.name
		if(names.find { it==name } && args.length==1){
			setProperty(name, valueOrClosure(args[0]))
		}else{
			super.invokeMethod(name, args);
		}
	}
}
