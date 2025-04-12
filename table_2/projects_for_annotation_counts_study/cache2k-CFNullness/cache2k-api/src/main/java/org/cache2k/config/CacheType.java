package org.cache2k.config;
public interface CacheType<T> {
  String DESCRIPTOR_TO_STRING_PREFIX = "CacheType:";
  static <T> CacheType<T> of(Class<T> t) {
    return (CacheType<T>) of((Type) t);
  }
  static CacheType<?> of(Type t) {
    if (t instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) t;
      Class c = (Class) pt.getRawType();
      CacheType[] ta = new CacheType[pt.getActualTypeArguments().length];
      for (int i = 0; i < ta.length; i++) {
        ta[i] = of(pt.getActualTypeArguments()[i]);
      }
      return new CacheTypeCapture.OfGeneric(c, ta);
    } else if (t instanceof GenericArrayType) {
      GenericArrayType gat = (GenericArrayType) t;
      return new CacheTypeCapture.OfArray(of(gat.getGenericComponentType()));
    }
    if (!(t instanceof Class)) {
      throw new IllegalArgumentException("The run time type is not available, got: " + t);
    }
    Class c = (Class) t;
    if (c.isArray()) {
      return new CacheTypeCapture.OfArray(of(c.getComponentType()));
    }
    return new CacheTypeCapture.OfClass(c);
  }
   Class<T> getType();
  boolean hasTypeArguments();
  boolean isArray();
   CacheType<?> getComponentType();
   CacheType<?>[] getTypeArguments();
  String getTypeName();
}
